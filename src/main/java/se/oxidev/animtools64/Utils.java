package se.oxidev.animtools64;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Utils {

	public static <T extends BaseController> T showDialog(String fxmlName, String title,Object...params) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlName));
		
		Stage stage = new Stage();
		
		Parent root = null;
		root = (Parent) loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(title);
		
		T controller = loader.getController();
		controller.setStage(stage);

		Optional<Method> possibleInit = Arrays.stream(controller.getClass().getMethods()).filter(item -> item.getName().equals("init")).findFirst();
		if (possibleInit.isPresent()) {
			Method method = possibleInit.get();
			try {
				method.invoke(controller,params);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		//controller.init();
		
		return controller;		
		
	}
}
