package se.oxidev.animtools64;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.Control;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CommonFunctions {

	public static String browseDirectory(ActionEvent event) {
		Stage stage = Stage.class.cast(Control.class.cast(event.getSource()).getScene().getWindow());
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(stage);
		return (selectedDirectory != null) ? selectedDirectory.getAbsolutePath() : "";
	}

	public static String browseDirectory(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(stage);
		return (selectedDirectory != null) ? selectedDirectory.getAbsolutePath() : "";
	}

	public static String getAbsolutePathUsingFileChooser(Stage stage, String title) {		
		File selectedFile = getFileUsingFileChooser(stage, title);
		return (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
	}

	public static File getFileUsingFileChooser(Stage stage, String title) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		// Show dialog
		File selectedFile = fileChooser.showSaveDialog(stage);
		return selectedFile;
	}
	
	public static String showFileChooserWithExtensionFilter(Stage stage, String title, String extensionFilter) {
		// Filter example:
		// "AT64 project files (*.at64proj)", "*.at64proj"
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		//Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensionFilter);
		fileChooser.getExtensionFilters().add(extFilter);
		// Show dialog
		File selectedFile = fileChooser.showSaveDialog(stage);
		return (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
	}

	public static boolean DirectoryExists(String dir) {
		return (new File(dir)).exists();
	}
	
	public static boolean MkDirs(String dir) {
		return (new File(dir)).mkdirs();
	}

	public static boolean EmptyDirectoryAndDelete(String dir) {
		File index = new File(dir);
		String[]entries = index.list();
		for (String s: entries) {
		    File currentFile = new File(index.getPath(), s);
		    currentFile.delete();
		}
		return (new File(dir)).delete();
	}

    public static boolean SaveStringAsFile(String content, String pathName){
        try {
        		File file = new File(pathName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException ex) {
        		return false;
        }         
    }    
}
