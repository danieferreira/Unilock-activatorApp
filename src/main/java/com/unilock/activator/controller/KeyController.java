package com.unilock.activator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.unilock.activator.model.Key;
import com.unilock.activator.service.KeyService;
import com.unilock.activator.view.FXMLView;
import com.unilock.activator.view.StageManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Lazy
@Component
public class KeyController implements FXMLController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@FXML
	private TextField txtNumber;
	@FXML
	private Spinner<Integer> txtTime;	

	@Autowired
	@Lazy
	KeyService keyService;
	
	private Stage modalStage;
	private StageManager stageManager;
	
	public KeyController(StageManager stageManager) {
		this.stageManager = stageManager;
	}
	
	@Override
	public void initialize() {

		txtTime.setEditable(true);
		txtTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 30, 5));
	}
	
	@FXML
	public void cancelAction(ActionEvent event) {
//		if (modalStage != null) {
//			modalStage.hide();
//		}
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		stageManager.switchScene(FXMLView.MAIN);
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	public void applyAction(ActionEvent event) {
		logger.info("> new key");
		Key newKey = new Key();
		newKey.setKeyNumber(Long.parseLong(txtNumber.getText()));
		newKey.setActiveTime(txtTime.getValue() * 1L);
		newKey.setEnabled(true);
		keyService.add(newKey);
//		if (modalStage != null) {
//			modalStage.hide();
//		}
		logger.info("< new key");
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		stageManager.switchScene(FXMLView.MAIN);
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
	}

	@Override
	public void setStage(Stage stage) {
		modalStage = stage;
	}
	

}
