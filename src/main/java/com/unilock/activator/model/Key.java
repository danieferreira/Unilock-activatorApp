package com.unilock.activator.model;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

@Entity
@Table(name = "Keys")
public class Key {
	@Id
	private long keyNumber;
	private byte keyMode;
	private long keyTimestamp;
	private LocalDateTime activated;
	private Long activeTime;
	private Boolean enabled;
	
	public byte getKeyMode() {
		return keyMode;
	}
	public void setKeyMode(byte keyMode) {
		this.keyMode = keyMode;
	}
	public long getKeyNumber() {
		return keyNumber;
	}
	public void setKeyNumber(long keyNumber) {
		this.keyNumber = keyNumber;
	}
	public long getKeyTimestamp() {
		return keyTimestamp;
	}
	public void setKeyTimestamp(long keyTimestamp) {
		this.keyTimestamp = keyTimestamp;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public LocalDateTime getActivated() {
		return activated;
	}
	public void setActivated(LocalDateTime activated) {
		this.activated = activated;
	}
	public Long getActiveTime() {
		return activeTime;
	}
	public ObjectProperty<LocalDateTime> getActivatedProperty() {
		 return  new SimpleObjectProperty<LocalDateTime>(activated);
	}
	public void setActiveTime(Long activeTime) {
		this.activeTime = activeTime;
	}
	
	public Long getRemaining() {
		if (activated != null) {
			Duration timeDiff = Duration.between(activated, LocalDateTime.now());
			Long timeDiffMinutes = timeDiff.getSeconds()/60;
			if (timeDiffMinutes > activeTime) {
				return 0L;
			} else {
				return (activeTime - timeDiffMinutes);
			}
		} else {
			return 0L;
		}
	}	
	
	public ObjectProperty<String> getRemainingProperty() {
		String msg = "";
		Long diff = getRemaining(); 
		msg = String.format("%d:", diff/60);
		msg += String.format("%02d", diff % 60);
		return new SimpleObjectProperty<String>(msg);
	}
	
	public String getStatus() {
		if (enabled) {
			if (getRemaining() > 0) {
				return "Active";
			} else {
				return "Expired";
			}
		} else {
			return "Disabled";
		}
	}

	public ObjectProperty<String> getStatusProperty() {
		return new SimpleObjectProperty<String>(getStatus());
	}

	public int getStatusEnum() {
		if (enabled) {
			if (getRemaining() > 0) {
				return 2;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}
}
