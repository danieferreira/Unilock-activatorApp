package com.unilock.activator.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.unilock.activator.model.Account;
import com.unilock.activator.model.Key;
import com.unilock.activator.service.KeyService;
import com.unilock.activator.service.SerialPortService;
import com.unilock.activator.view.FXMLView;
import com.unilock.activator.view.StageManager;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

@Lazy
@Component
public class MainController implements FXMLController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnLocks;
	@FXML
	private Button btnKeys;
	@FXML
	private Button btnAccounts;
	@FXML
	private Button btnLogs;
	@FXML
	private Label lblTitle;
	
	@FXML
	private TableView<Key> tableKeys;
	@FXML
	private TableColumn<Key, Long> colKey;
	@FXML
	private TableColumn<Key, String> colActive;
	@FXML
	private TableColumn<Key, LocalDateTime> colActivated;
	@FXML
	private TableColumn<Key, String> colRemaining;
	@FXML
	private TableColumn<Key, Key> colEnable;
	
//	@Autowired
//	@Lazy
//	SerialPortService serialPortService;

	@Autowired
	@Lazy
	KeyService keyService;
	
	private final StageManager stageManager;
	private final Account account;
	private Timer timer = new Timer();
	
	public MainController(StageManager stageManager) {
		this.stageManager = stageManager;
		account = new Account();
		account.setFirstname("Danie");
		account.setSurname("Ferreira");
	}

	@Override
	public void initialize() {
		updateTitle(account);
		DebugAddKeys();
//		serialPortService.Start();
		InitialiseKeyTable();
		TimerTask task = new TimerTask()
		{
		        public void run()
		        {
		            UpdateKeyTable();       
		        }
		};
		timer.scheduleAtFixedRate(task, 200l, 20000l);
		stageManager.getPrimaryStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        timer.cancel();
		    }
		});
	}

	private void InitialiseKeyTable() {
		tableKeys.setRowFactory(obj -> new TableRow<Key>() {
				@Override
				protected void updateItem(Key item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null) {
		        		if (item.getStatusEnum() == 2) {
		        			setStyle("-fx-background-color:lightgreen");
		        		} else if (item.getStatusEnum() == 0) {
		        			setStyle("-fx-background-color:lightgrey");
		        		} else {
		        			//setStyle("");
		        		}
					}
				}
		});
		
		colKey.setCellValueFactory(new PropertyValueFactory<Key, Long>("keyNumber"));
		colKey.setStyle("-fx-alignment: CENTER-RIGHT;");
		
		colActive.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
		colActive.setStyle("-fx-alignment: CENTER;");
		colActive.setCellFactory(col -> new TableCell<Key, String>() {
		    @Override
		    protected void updateItem(String item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty)
		            setText(null);
		        else
		        	if (item != null) {
		        		setText(item);
		        	} else {
			            setText(null);
		        	}
		    }
		});
	
		colActivated.setCellValueFactory(cellData -> cellData.getValue().getActivatedProperty());
		colActivated.setPrefWidth(130);
		colKey.setStyle("-fx-alignment: CENTER;");
		colActivated.setCellFactory(col -> new TableCell<Key, LocalDateTime>() {
		    @Override
		    protected void updateItem(LocalDateTime item, boolean empty) {
		    	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
		        super.updateItem(item, empty);
		        if (empty)
		            setText(null);
		        else
		        	if (item != null) {
		        		setText(String.format(item.format(formatter)));
		        	} else {
			            setText(null);
		        	}
		    }
		});

		colRemaining.setCellValueFactory(cellData -> cellData.getValue().getRemainingProperty());
		colRemaining.setStyle("-fx-alignment: CENTER;");
		colRemaining.setCellFactory(col -> new TableCell<Key, String>() {
		    @Override
		    protected void updateItem(String item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty)
		            setText(null);
		        else
		        	if (item != null) {
		        		setText(item.toString());
		        	} else {
			            setText(null);
		        	}
		    }
		});

		
		colEnable.setStyle("-fx-alignment: CENTER;");
		colEnable.setCellValueFactory(
				new Callback<CellDataFeatures<Key, Key>, ObservableValue<Key>>() {
					@Override
					public ObservableValue<Key> call(
							CellDataFeatures<Key, Key> features) {
						return new ReadOnlyObjectWrapper<Key>(features.getValue());
					}
				});
		colEnable.setCellFactory(
			new Callback<TableColumn<Key, Key>, TableCell<Key, Key>>() {
				@Override
				public TableCell<Key, Key> call(
						TableColumn<Key, Key> btnCol) {
					return new TableCell<Key, Key>() {
						final Button btEnable = new Button("Enable");
						final Button btDisable = new Button("Disable");
	
						@Override
						public void updateItem(final Key key, boolean empty) {
							super.updateItem(key, empty);
							if (key != null) {
								if (key.getEnabled() == false) {
									btEnable.setPrefWidth(65);
									btEnable.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											key.setEnabled(true);
											keyService.add(key);
											UpdateKeyTable();
										}
									});
									setGraphic(btEnable);
								} else {
									btDisable.setPrefWidth(65);
									btDisable.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											key.setEnabled(false);
											keyService.add(key);
											UpdateKeyTable();
										}
									});
									setGraphic(btDisable);
								}
							} else {
								setGraphic(null);
							}
						}
					};
				}
		});
	}

	private void DebugAddKeys() {
		Key newKey = new Key();
		LocalDateTime activatedTime = LocalDateTime.now();
		
		newKey.setActivated(activatedTime.plusMinutes(-10));
		newKey.setActiveTime(30L);
		newKey.setEnabled(true);
		newKey.setKeyNumber(1001);
		keyService.add(newKey);
		
		newKey.setActivated(activatedTime.plusMinutes(-50));
		newKey.setActiveTime(30L);
		newKey.setEnabled(true);
		newKey.setKeyNumber(1002);
		keyService.add(newKey);
		
		newKey.setActivated(activatedTime.plusMinutes(-60));
		newKey.setActiveTime(30L);
		newKey.setEnabled(false);
		newKey.setKeyNumber(1003);
		keyService.add(newKey);
		
		newKey.setActivated(activatedTime.plusMinutes(-1));
		newKey.setActiveTime(30L);
		newKey.setEnabled(true);
		newKey.setKeyNumber(1004);
		keyService.add(newKey);

		newKey.setActivated(activatedTime.plusMinutes(-50));
		newKey.setActiveTime(30L);
		newKey.setEnabled(true);
		newKey.setKeyNumber(1005);
		keyService.add(newKey);

		newKey.setActivated(activatedTime.plusMinutes(-50));
		newKey.setActiveTime(30L);
		newKey.setEnabled(true);
		newKey.setKeyNumber(1006);
		keyService.add(newKey);
}

	private void UpdateKeyTable() {
		ObservableList<Key> lstKeys = FXCollections.observableArrayList();
		List<Key> keys =  keyService.getAll();
		for (Key key : keys) {
			lstKeys.add(key);
		}
		tableKeys.setItems(lstKeys);
	}

	@FXML
	public void Logout(ActionEvent event) {
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		stageManager.switchScene(FXMLView.LOGIN);
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
	}

	@FXML
	public void AddKey(ActionEvent event) {
//		stageManager.showDialog(FXMLView.KEY);
//		UpdateKeyTable();
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		stageManager.switchScene(FXMLView.KEY);
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
	}
	
	private void updateTitle(Account account) {
		String title = "";
		if ((account.getFirstname() != null) || (!account.getFirstname().isEmpty())) {
			title = account.getFirstname();
		}
		if ((account.getSurname() != null) || (!account.getSurname().isEmpty())) {
			if (title.isEmpty()) {
				title = account.getSurname();
			} else {
				title += " " + account.getSurname();
			}
		}

		lblTitle.setText("Welcome " + title);
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}
	
}
