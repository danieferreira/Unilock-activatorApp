package com.unilock.activator.model;

public class Key {
	private char keyMode;
	private long keyNumber;
	private long keyTimestamp;
	
	public char getKeyMode() {
		return keyMode;
	}
	public void setKeyMode(char keyMode) {
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
