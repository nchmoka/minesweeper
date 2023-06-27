package mines;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MinesFX extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	Controller fxController;
	//Mines minesController;
	@Override
	public void start(Stage stage) {
		GridPane mainScreen;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/MinesFX.fxml"));
			mainScreen = loader.load();
			fxController = loader.getController();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
		fxController.setGrid();
		Scene scene = new Scene(mainScreen);
		stage.sizeToScene();
		stage.setScene(scene);
		stage.setTitle("The Amazing Mine Sweeper");
		fxController.setStage(stage, mainScreen);
		stage.show();
	}
}
