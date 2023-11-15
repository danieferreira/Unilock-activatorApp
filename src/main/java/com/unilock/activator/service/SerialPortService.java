package com.unilock.activator.service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.unilock.activator.model.Key;
import com.unilock.activator.model.Message;

@Service
public class SerialPortService implements DisposableBean, Runnable {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SerialPort serialPort;
	private volatile boolean portFound;
    private ConcurrentLinkedQueue<Integer>  queueRX;
	private HandleMessages handleMessage;
	private Thread thread;
	private boolean enabled;
	private Key currentKey;

	
	public SerialPortService() {
		super();
		currentKey = new Key();
		setEnabled(true);
		thread = new Thread(this, "SearchPort");
	}

	public void Start() {
		thread.start();
	}
	
	@Override
	public void run() {
		String[] strProbeNames = {"PROGRAMMER"};
		
		while (enabled) {
			while ((!portFound) && (enabled)) {
				SerialPort[] ports = SerialPort.getCommPorts();
				for (SerialPort port : ports) {
					logger.debug(port.getDescriptivePortName() + "," + port.getPortDescription() + "," + port.getSystemPortName());
					for (String name : strProbeNames) {
						if (port.getDescriptivePortName().contains(name)) {
							logger.info(String.format("Probe found at %s (%s)", port.getSystemPortName(), port.getDescriptivePortName()));
							if (Open(port)) {
								String message = String.format("%s Connected", port.getSystemPortName());
								logger.info(message);
								portFound = true;
//								ProbeConnectEvent event = new ProbeConnectEvent(this, message);
//								Platform.runLater(new Runnable(){
//									public void run() {
//										applicationEventPublisher.publishEvent(event);
//									}
//								});
							}
						}
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
			
			while ((enabled) && (serialPort != null) && (serialPort.isOpen())) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	@Override
	public void destroy() throws Exception {
		setEnabled(false);
		if (handleMessage != null)
		{
			handleMessage.setEnabled(false);
		}
		if (serialPort != null) {
			serialPort.closePort();
		}
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
	}


	private boolean Open(SerialPort port) {
		serialPort = port;
		if (serialPort != null) {
			queueRX  = new ConcurrentLinkedQueue<Integer>();
			serialPort.setBaudRate(9600);
			serialPort.setNumStopBits(SerialPort.TWO_STOP_BITS);
			serialPort.setParity(SerialPort.NO_PARITY);
			serialPort.setNumDataBits(8);
			if (serialPort.openPort()) {
				serialPort.addDataListener(new SerialPortDataListener() {
					   @Override
					   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
					   @Override
					   public void serialEvent(SerialPortEvent event)
					   {
					      if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
						      byte[] newData = new byte[serialPort.bytesAvailable()];
						      int numRead = serialPort.readBytes(newData, newData.length);
						      for (int i = 0; i < numRead; i++) {
						    	  int tmp = newData[i];
						    	  if (tmp < 0) {
						    		  tmp += 128;
						    	  }
						    	  queueRX.add(tmp);
						      }
					      }
					   }
					});
				
				//Start the consumer thread
				handleMessage = new HandleMessages(queueRX);
				return true;
			}
		}
		return false;
	}

		
	private class HandleMessages implements DisposableBean, Runnable {
		private final Logger logger = LoggerFactory.getLogger(this.getClass());
	    private LinkedBlockingQueue<Message> queueMessages;
	    private Thread thread;
		private boolean enabled;
		private ConcurrentLinkedQueue<Integer> queueRX;
	
		public HandleMessages(ConcurrentLinkedQueue<Integer> queueRX) {
			super();
			this.queueRX = queueRX;
			queueMessages  = new LinkedBlockingQueue<Message>();
			enabled = true;
			thread = new Thread(this, "HandleMessages");
			thread.start();
		}
	
		@Override
		public void run() {
			String msg = "";
			while (enabled) {
				//Read bytes from queue
				if ((queueRX != null) && (!queueRX.isEmpty())) {
					//Try and find a good message
					while (queueRX.size() > 0) {
						if (queueRX.peek() == 0) {
							queueRX.remove();
							if (!msg.isEmpty()) {
								logger.debug("RX: " + msg);
								String txtMsg = checkMessage(msg);
								if (txtMsg != null) {
									logger.debug("TX: " + txtMsg);
									serialPort.writeBytes(txtMsg.getBytes(), msg.length());
								}
							}
							msg = "";
						} else {
							msg += String.format("%c", queueRX.remove());
						}
					}
				} else {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
			}
		}
	
		private String checkMessage(String msg) {
			String txMessage = null;
			
			switch (msg.charAt(0)) {
				case 'S':
					currentKey.setKeyMode((byte)msg.charAt(8+1));
					currentKey.setKeyNumber(Long.parseLong(msg.substring(1, 8), 16));
					//currentKey.setKeyTimestamp(Long.parseLong(msg.substring(8 + 2), 16));
					txMessage = "]a99999999\n\0";
					break;

				default:
					break;
			}
			return txMessage;
		}
	
		@Override
		public void destroy() throws Exception {
			setEnabled(false);
		}
	
		public void setEnabled(boolean enable) {
			enabled = enable;
		}
	
	}

}
