package com.unilock.activator.view;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;

import com.unilock.activator.config.SpringFXMLLoader;
import com.unilock.activator.controller.FXMLController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

    private static final Logger logger = getLogger(StageManager.class);
    private final Stage primaryStage;
	private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
    	logger.info("> create StageManager");
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    	logger.info("< create StageManager");
    }

    public void switchScene(final FXMLView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }
    
    public void showDialog(final FXMLView view) {
    	Stage dialog = new Stage();
    	Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
    	dialog.initStyle(StageStyle.UTILITY);
    	Scene scene = prepareScene(viewRootNodeHierarchy);
    	FXMLController controller = (FXMLController)viewRootNodeHierarchy.getUserData();
    	controller.setStage(dialog);
    	dialog.setTitle(view.getTitle());
    	dialog.setScene(scene);
    	dialog.showAndWait();
    }
    
    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);
        
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        
        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }
    
    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
        	FXMLLoader loader = springFXMLLoader.getLoader(fxmlFilePath);
            rootNode = loader.load();
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
            FXMLController controller = loader.getController();
            rootNode.setUserData(controller);
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }
    
    
    private void logAndExit(String errorMsg, Exception exception) {
        logger.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

    public Stage getPrimaryStage() {
		return primaryStage;
	}
}