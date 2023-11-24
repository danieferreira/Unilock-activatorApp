package com.unilock.activator.controller;

import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.unilock.activator.view.FXMLView;
import com.unilock.activator.view.StageManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Lazy
@Component
public class LoginController implements FXMLController {

	@FXML
	private Label lblStatus;
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Button btnLogin;

	private final StageManager stageManager;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public LoginController(StageManager stageManager) {
		this.stageManager = stageManager;
	}

	@FXML
	public void Login(ActionEvent event) {
		if ((getUsername() != null) && (getPassword() != null)) {
			stageManager.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
			Platform.runLater(new Runnable() {
	            @Override public void run() {
			
					logger.info("> Login Event");
					try {
						stageManager.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
						stageManager.switchScene(FXMLView.MAIN);
					} catch (ResourceAccessException err1) {
						logger.info("	" + err1.getMessage());
						clearCredentials();
						lblStatus.setText(err1.getMessage());
					}
					logger.info("< Login Event");
					stageManager.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
				}

			});
		}
	}

	@Override
	public void initialize() {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
	}

	private Authentication createAuthenticationToken(String username, String password) {
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	private void clearCredentials() {
		txtUsername.setText("");
		txtPassword.clear();
	}

	@FXML
	private void usernameTextFieldMouseClicked(MouseEvent event) {
		clearStatusLabel();
	}

	@FXML
	private void passwordTextFieldMouseClicked(MouseEvent event) {
		clearStatusLabel();
	}

	private void clearStatusLabel() {
		lblStatus.setText("");
	}

	public TextField getUserField() {
		return txtUsername;
	}

	public PasswordField getPasswordField() {
		return txtPassword;
	}

	public String getUsername() {
		return txtUsername.getText();
	}

	public String getPassword() {
		return txtPassword.getText();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}
}
