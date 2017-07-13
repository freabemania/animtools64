package se.oxidev.animtools64;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		TestController controller = (TestController)Utils.showDialog("main.fxml", "animtools64", Integer.valueOf(1));
		//controller.postInit(primaryStage);
		controller.getStage().showAndWait();

		System.out.println("x: " + String.valueOf(controller.getWhatever()));
		
	}
}
