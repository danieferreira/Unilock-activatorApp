package com.unilock.activator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.unilock.activator.model.Account;
import com.unilock.activator.service.SerialPortService;
import com.unilock.activator.view.FXMLView;
import com.unilock.activator.view.StageManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
	
	@Autowired
	@Lazy
	SerialPortService serialPortService;
		
	private final StageManager stageManager;
	private final Account account;

	public MainController(StageManager stageManager) {
		this.stageManager = stageManager;
		account = new Account();
		account.setFirstname("Danie");
		account.setSurname("Ferreira");
	}

	@Override
	public void initialize() {
		updateTitle(account);
		serialPortService.Start();
	}

	@FXML
	public void Logout(ActionEvent event) {
		stageManager.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
		stageManager.switchScene(FXMLView.LOGIN);
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
	
}
