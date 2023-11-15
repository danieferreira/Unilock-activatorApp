package com.unilock.activator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Keys")
public class Key {
	@Id
	private long keyNumber;
	private byte keyMode;
	private long keyTimestamp;
	
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
	
	
}
