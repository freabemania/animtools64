package se.oxidev.animtools64;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class TestController extends BaseController {

	Preferences prefs = Preferences.userNodeForPackage(TestController.class);

	private final ArrayList<AnimFrame> animFrames = new ArrayList<AnimFrame>();

	private int currentFrameIndex = 0;
	private AnimFrame currentAnimFrame = new AnimFrame();

	private int DOUBLE_PIXELS = 2;
	private int X1_ZOOM_IS_DOUBLED = 2;

	private int whatever = 0;
	private boolean mousePressed = false;
	private int selectedColor = 15;
	private int selectedColorRight = 0;
	private String loadedProject = "";
	private int zoom = 1;
	private double opacity = 0.3;
	private Gfx gfx = new Gfx();

	@FXML
	private Label labelStatus;

	@FXML
	private TextField textFrame, textFrameUnderlay;

	@FXML
	private Button buttonBrowse, buttonNextFrame, buttonPrevFrame;

	@FXML
	private Canvas canvasPaint, canvasColors;

	@FXML
	private Pane mainPane, stackPanePaint, paneColors;

	@FXML
	private CheckBox checkGrid;

	@FXML
	private ColorPicker colorPickerGrid;

	@FXML
	private ComboBox<String> comboZoom;

	@FXML
	private ScrollBar scrollBarX, scrollBarY;

	public void init(Integer i) {

		System.out.println("->" + i);

		canvasPaint.setWidth(stackPanePaint.getPrefWidth());
		canvasPaint.setHeight(stackPanePaint.getPrefHeight());
		canvasColors.setWidth(paneColors.getPrefWidth());
		canvasColors.setHeight(paneColors.getPrefHeight());

		// ---------------------------------------------
		// Restore preferences
		// ---------------------------------------------
		Preferences prefs = Preferences.userNodeForPackage(TestController.class);
		// settingOpacity
		this.opacity = Double.valueOf(prefs.get("settingOpacity", "0.3"));
		// settingGridColor
		String value = prefs.get("settingGridColor", "444444");
		colorPickerGrid.setValue(Color.web(value));
		System.out.println("settingGridColor: " + value);
		// ---------------------------------------------

		// ---------------------------------------------
		// Restore previous project
		// ---------------------------------------------
		loadedProject = prefs.get("settingLoadedProject", "");
		System.out.println("loadedProject: " + loadedProject);
		if (loadedProject == "") {
			animFrames.add(new AnimFrame(1, ""));
			this.getStage().setTitle("animtools64 - [no project]");
		}

		for (AnimFrame a : animFrames) {
			System.out.println("frame: " + a.frameNo);
		}
		// ---------------------------------------------

		this.currentAnimFrame = animFrames.get(this.currentFrameIndex);

		
		// Zoom combobox
		ObservableList<String> listZoom = FXCollections.observableArrayList("1X", "2X", "4X");
		comboZoom.setItems(listZoom);
		comboZoom.setValue(prefs.get("settingZoom", "1X"));

		// Grid checkbox
		checkGrid.setSelected(prefs.getBoolean("settingGrid", false));

		// Scrollbars
		scrollBarX.setDisable(true);
		scrollBarY.setDisable(true);

		scrollBarX.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				scrollBarX.setValue(Math.floor((double) new_val));
				System.out.println("Scrolled. Value: " + scrollBarX.getValue());
				redrawAll();
			}
		});
		scrollBarY.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				scrollBarY.setValue(Math.floor((double) new_val));
				System.out.println("Scrolled. Value: " + scrollBarY.getValue());
				redrawAll();
			}
		});

		// Initial drawing
		gfx.drawPalette(this.canvasColors);

		// ---------------------------------------------
		// Menu
		// ---------------------------------------------
		MenuBar menuBar = new MenuBar();

		// --- Span entire width
		menuBar.setPrefWidth(mainPane.getPrefWidth());

		// --- Menu bar
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
		Menu menuOptions = new Menu("Options");
		menuBar.getMenus().addAll(menuFile, menuEdit, menuOptions);

		// --- FILE > NEW PROJECT
		MenuItem menuItemNewProject = new MenuItem("New project");
		menuItemNewProject.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				newProject();
			}
		});
		menuFile.getItems().add(menuItemNewProject);

		// --- FILE > EXIT
		MenuItem menuItemExit = new MenuItem("Exit");
		menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				getStage().close();
			}
		});
		menuFile.getItems().add(menuItemExit);

		// --- OPTIONS > UNDERLAY OPACITY       
        Menu menuUnderlayOpacity = new Menu("Underlay opacity");
        
		Slider slider = new Slider();
		slider.setMin(0.1);
        slider.setValue(this.opacity);
        slider.setMax(1.0);
        slider.setMajorTickUnit(0.1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        //slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setMinHeight(Slider.USE_PREF_SIZE);
        
		CustomMenuItem customMenuItem = new CustomMenuItem(slider);
		
		slider.valueProperty().addListener((obs, oldValue, newValue) -> {
			double r = Math.round(newValue.doubleValue()*10);
			r = r / 10;
            System.out.println("...sliding: " + r);
            this.opacity = r;
			prefs.put("settingOpacity", String.valueOf(r));
			redrawAll();
        });
		
		customMenuItem.setHideOnClick(false);
		menuUnderlayOpacity.getItems().add(customMenuItem);

		menuOptions.getItems().add(menuUnderlayOpacity);

		// Add menuBar to main pane
		mainPane.getChildren().add(menuBar);
		// ---------------------------------------------

		zoomUpdate();

		showFrame(0); // Includes redrawAll();

	}

	/*
	 * ------------ * EVENTS * ------------
	 */

	public void handleComboOnAction(ActionEvent event) {

		if (event.getSource() == comboZoom) {
			prefs.put("settingZoom", comboZoom.getValue().toString());
			zoomUpdate();

		} 

	}

	public void handleButtonOnAction(ActionEvent event) {

		if (event.getSource() == buttonBrowse) {
			String tmp = CommonFunctions.browseDirectory(event);
		} else if (event.getSource() == buttonNextFrame) {
			showFrame(currentFrameIndex + 1);
		} else if (event.getSource() == buttonPrevFrame) {
			showFrame(currentFrameIndex - 1);
		}

	}

	public void handleCheckOnAction(ActionEvent event) {

		if (event.getSource() == checkGrid) {
			prefs.putBoolean("settingGrid", checkGrid.isSelected());
			redrawAll();
		}

	}

	public void handleColorPickerOnAction(ActionEvent event) {

		if (event.getSource() == colorPickerGrid) {

			prefs.put("settingGridColor", colorPickerGrid.getValue().toString());

			if (checkGrid.isSelected()) {
				redrawAll();
			}
		}

	}

	public void onMouseMoved(MouseEvent event) {

		updateLabel(event.getX(), event.getY());

	}

	public void onMouseDragged(MouseEvent event) {

		updateLabel(event.getX(), event.getY());

		if (this.mousePressed) {
			if (event.getButton() == MouseButton.PRIMARY) {
				gfx.setPixel(this.canvasPaint, this.selectedColor, event.getX(), event.getY());
			} 
			else if (event.getButton() == MouseButton.SECONDARY) {
				gfx.setPixel(this.canvasPaint, this.selectedColorRight, event.getX(), event.getY());
			} 
		}

	}

	public void onMouseReleased(MouseEvent event) {

		this.mousePressed = false;

	}

	public void onMousePressed(MouseEvent event) {

		this.mousePressed = true;

		if (event.getButton() == MouseButton.PRIMARY) {
			gfx.setPixel(this.canvasPaint, this.selectedColor, event.getX(), event.getY());
		} 
		else if (event.getButton() == MouseButton.SECONDARY) {
			gfx.setPixel(this.canvasPaint, this.selectedColorRight, event.getX(), event.getY());
		} 


	}

	public void onColorPressed(MouseEvent event) {

		double x = Math.floor(event.getX() / 32);
		double y = Math.floor(event.getY() / 32);

		if (event.getButton() == MouseButton.PRIMARY) {
			this.selectedColor = (int) ((y * 4) + x);
		} 
		else if (event.getButton() == MouseButton.SECONDARY) {
			this.selectedColorRight = (int) ((y * 4) + x);
		} 
		else if (event.getButton() == MouseButton.MIDDLE) {
			//
		}

		System.out.println("Left color: " + String.valueOf(this.selectedColor) + ", right color: "
				+ String.valueOf(this.selectedColorRight));

		redrawColorSelector();

	}

	public void onClick(MouseEvent event) {

		this.whatever = 1;
		getStage().close();

	}

	public void showFrame(int index) {

		if (index < animFrames.size()) {
			currentAnimFrame = animFrames.get(index);
		}
		currentFrameIndex = index;

		buttonPrevFrame.setDisable((index <= 0));
		buttonNextFrame.setDisable((index >= animFrames.size() - 1));

		textFrame.setText((currentFrameIndex + 1) + " / " + animFrames.size());
		textFrameUnderlay.setText(animFrames.get(currentFrameIndex).imageName);

		redrawAll();

	}

	public void zoomUpdate() {

		try {
			String z = comboZoom.getValue();
			System.out.println("combo changed to " + z);

			scrollBarX.setDisable(z == "1X");
			scrollBarY.setDisable(z == "1X");

			if (z == "1X") {
				scrollBarX.setMax(0);
				scrollBarX.setValue(0);
				scrollBarY.setMax(0);
				scrollBarY.setValue(0);
				zoom = 1;
			} else if (z == "2X") {

				// imgwidth = 1280
				// x = visible
				//
				// |xxxxxxxxxxxxxxxx| x = 100
				// |xxxxxxxxxxxxxxxx| x
				// |xxxxxxxxxxxxxxxx| | 2X = 100
				// |xxxxxxxxxxxxxxxx| | 2X
				//
				// |xxxxxxxx |
				// | ---2X---|
				// 160 160
				//
				// Halva pixlar -> 80

				if (scrollBarX.getValue() > 80) {
					scrollBarX.setValue(80);
				}
				scrollBarX.setMax(80);

				if (scrollBarY.getValue() > 100) {
					scrollBarY.setValue(100);
				}
				scrollBarY.setMax(100);

				zoom = 2;
			} else if (z == "4X") {

				// imgwidth = 2560
				// x = visible
				//
				// |xxxxxxxxxxxxxxxx| x = 50
				// |xxxxxxxxxxxxxxxx| |
				// |xxxxxxxxxxxxxxxx| | 4X = 150
				// |xxxxxxxxxxxxxxxx| |
				//
				// |xxxx |
				// | -----4X-----|
				// 80 240
				//
				// Halva pixlar -> 120

				if (scrollBarX.getValue() > 120) {
					scrollBarX.setValue(120);
				}
				scrollBarX.setMax(120);

				if (scrollBarY.getValue() > 150) {
					scrollBarY.setValue(150);
				}
				scrollBarY.setMax(150);

				zoom = 4;
			}

			scrollBarX.setVisibleAmount(scrollBarX.getMax() * canvasPaint.getWidth() / (640 * zoom));
			scrollBarY.setVisibleAmount(scrollBarY.getMax() * canvasPaint.getHeight() / (400 * zoom));

			redrawAll();

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void updateLabel(double x, double y) {

		x = Math.floor(x / (4 * zoom));
		y = Math.floor(y / (2 * zoom));

		boolean invalid = (x < 0 || x > 159 || y < 0 || y > 199);
		if (zoom == 2) {
			invalid = (x < 0 || x > 79 || y < 0 || y > 99);
		} else if (zoom == 4) {
			invalid = (x < 0 || x > 39 || y < 0 || y > 39);
		}

		x += Math.floor(scrollBarX.getValue());
		y += Math.floor(scrollBarY.getValue());

		if (!invalid) {
			this.labelStatus.setText("x: " + String.valueOf(x) + ", y: " + String.valueOf(y));
		} else {
			this.labelStatus.setText("");
		}

		this.labelStatus.setText(this.labelStatus.getText() + ", col:" + String.valueOf(this.selectedColor));

	}

	public void redrawAll() {

		double scrollx = scrollBarX.getValue();
		double scrolly = scrollBarY.getValue();

		double x = Math.floor(scrollx * zoom * 4 * -1);
		double y = Math.floor(scrolly * zoom * 2 * -1);

		GraphicsContext gc = this.canvasPaint.getGraphicsContext2D();

		// Set background color
		gc.setFill(Color.rgb(0, 0, 0));
		gc.fillRect(0, 0, 640, 400);

		// ---------------------------------------------
		// Add background image
		// ---------------------------------------------
		// ImageView imgView = new
		// ImageView("https://cdn0.iconfinder.com/data/icons/toys/256/teddy_bear_toy_6.png");
		String pic = this.currentAnimFrame.imageName;

		if (pic != "") {
			Image bgimage = new Image(Main.class.getResourceAsStream(pic));

			int imgWidth = 640 * zoom;
			int imgHeight = 400 * zoom;
			bgimage = gfx.scale(bgimage, imgWidth, imgHeight, true);

			gc.setGlobalAlpha(this.opacity);
			gc.drawImage(bgimage, x, y);
			gc.setGlobalAlpha(1.0);
		}
		// ---------------------------------------------

		// ---------------------------------------------
		// Grid
		// ---------------------------------------------
		if (checkGrid.isSelected()) {

			gc.setStroke(colorPickerGrid.getValue());
			gc.setLineWidth(1);

			double modvalx = (scrollBarX.getValue() % 4) * zoom * DOUBLE_PIXELS * X1_ZOOM_IS_DOUBLED;
			double modvaly = (scrollBarY.getValue() % 8) * zoom * X1_ZOOM_IS_DOUBLED;

			for (int i = 1; i <= (40 / zoom); i++) {
				gc.strokeLine(16 * i * zoom - modvalx, 0, 16 * i * zoom - modvalx, 399);
			}
			for (int i = 1; i <= (25 / zoom); i++) {
				gc.strokeLine(0, 16 * i * zoom - modvaly, 639, 16 * i * zoom - modvaly);
			}

		}
		// ---------------------------------------------

		redrawColorSelector();

	}

	public void redrawColorSelector() {

		GraphicsContext gcol = this.canvasColors.getGraphicsContext2D();

		// ---------------------------------------------
		// Draw clean palette
		// ---------------------------------------------
		gfx.drawPalette(canvasColors);
		// ---------------------------------------------

		// ---------------------------------------------
		// Mark selected color
		// ---------------------------------------------

		// List all font names

		/*
		 * List<String> l = f.getFontNames(); for (String s : l) {
		 * System.out.println(s); }
		 */

		// Font
		Font f = new Font("System Bold", 22);
		gcol.setFont(f);

		// LMB
		gcol.setFill((selectedColor == 1) ? Color.BLACK : Color.WHITE);
		double l_x = Math.floor(selectedColor % 4);
		double l_y = Math.floor((selectedColor / 4) % 4) + 1;
		double xpos = (l_x * 32) + 10;
		double ypos = (l_y * 32) - 8;
		if (selectedColor == selectedColorRight) {
			xpos -= 7;
			gcol.fillText("LR", xpos, ypos);
		} else {
			gcol.fillText("L", xpos, ypos);
		}

		// RMB
		if (selectedColor != selectedColorRight) {
			gcol.setFill((selectedColorRight == 1) ? Color.BLACK : Color.WHITE);
			l_x = Math.floor(selectedColorRight % 4);
			l_y = Math.floor((selectedColorRight / 4) % 4) + 1;
			xpos = (l_x * 32) + 8;
			ypos = (l_y * 32) - 8;
			gcol.fillText("R", xpos, ypos);
		}
		// ---------------------------------------------

	}

	public void newProject() {
		
		File f = CommonFunctions.getFileUsingFileChooser(this.getStage(), "New project");
		
		String newProjectPathOnly = (f.getAbsolutePath() != null) ? f.getAbsolutePath() : "";
		String newProjectName = f.getName();
		String newProjectFile = newProjectPathOnly + File.separator + newProjectName + ".at64proj";

		if (newProjectPathOnly != "") {

			if (CommonFunctions.DirectoryExists(newProjectPathOnly)) {

				// New project, but folder exists. Clear!
				
				CommonFunctions.EmptyDirectoryAndDelete(newProjectPathOnly);
				System.out.println("Directory emptied and deleted");
				
			}
			
			if (CommonFunctions.MkDirs(newProjectPathOnly)) {
				
				System.out.println("Empty directory created: " + newProjectPathOnly);
				
				if (CommonFunctions.SaveStringAsFile("hej o hå", newProjectFile)) {
					
					System.out.println("Created file: " + newProjectFile);
					
				}
			}
			else {
				
				// TODO: ALERT Directory creation failed.			
				System.out.println("Directory creation failed");

			}

		}
		else {
			System.out.println("Nothing was selected");
		}
				
	}
	
	public int getWhatever() {
		return whatever;
	}

}
