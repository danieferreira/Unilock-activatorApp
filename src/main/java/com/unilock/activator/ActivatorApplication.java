package com.unilock.activator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.unilock.activator.view.FXMLView;
import com.unilock.activator.view.StageManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

@SpringBootApplication
public class ActivatorApplication extends Application {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ConfigurableApplicationContext springContext; 
	private StageManager stageManager;
	
    public static void main( String[] args )
    {
        launch(args);
    }

	@Override
	public void init() throws Exception {
		logger.info("> init");
		logger.info("	Start Spring");
		springContext = bootstrapSpringApplication();
		logger.info("< init");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		logger.info("> start");
		stageManager = springContext.getBean(StageManager.class, primaryStage);
		primaryStage.setTitle("UniLock Activator Login");
		displayInitialScene();
		logger.info("< start");
	}
	
	@Override
	public void stop() throws Exception  {
		springContext.close();
		Platform.exit();
	}

	protected void displayInitialScene() {
		stageManager.switchScene(FXMLView.LOGIN);
	}
	
	private ConfigurableApplicationContext bootstrapSpringApplication() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ActivatorApplication.class);
		String[] args = getParameters().getRaw().stream().toArray(String[]::new);
		builder.headless(false);
		return builder.run(args);
	}


}
