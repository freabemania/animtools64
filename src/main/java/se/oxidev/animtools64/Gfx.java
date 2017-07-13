package se.oxidev.animtools64;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Gfx {

	private int selectedColor;
	private int zoom;
	
	public Gfx() {

		selectedColor = 0;
		zoom = 1;
		
	}

	public void setPixel(Canvas canvas, double x, double y) {
		
		this.setPixel(canvas,  this.selectedColor,  x, y);
		
	}
	
	public void setPixel(Canvas canvas, int color, double x, double y) {
		
		int width = 4 * this.zoom;
		int height = 2 * this.zoom;
		
		x = Math.floor(x / 4);
		y = Math.floor(y / 2);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(this.getColor(color));
		
		gc.fillRect(x * width, y * height, width, height);
		
	}

	public Color getColor(int color) {
		
		if (color == 0) {
			return Color.web("000000");
		}
		else if (color == 1) {
			return Color.web("ffffff");
		}
		else if (color == 2) {
			return Color.web("894036");
		}
		else if (color == 3) {
			return Color.web("7abfc7");
		}
		else if (color == 4) {
			return Color.web("8a46ae");
		}
		else if (color == 5) {
			return Color.web("68a941");
		}
		else if (color == 6) {
			return Color.web("3e31a2");
		}
		else if (color == 7) {
			return Color.web("d0dc71");
		}
		else if (color == 8) {
			return Color.web("905f25");
		}
		else if (color == 9) {
			return Color.web("5c4700");
		}
		else if (color == 10) {
			return Color.web("bb776d");
		}
		else if (color == 11) {
			return Color.web("555555");
		}
		else if (color == 12) {
			return Color.web("808080");
		}
		else if (color == 13) {
			return Color.web("acea88");
		}
		else if (color == 14) {
			return Color.web("7c70da");
		}
		else {
			return Color.web("ababab");
		}
		
	}

	public void drawPalette(Canvas canvas) {
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.rgb(0, 0, 0));		
		gc.fillRect(0, 0, 32, 32);		
		gc.setFill(Color.rgb(255, 255, 255));		
		gc.fillRect(32, 0, 32, 32);
		gc.setFill(Color.web("894036"));		
		gc.fillRect(64, 0, 32, 32);
		gc.setFill(Color.web("7abfc7"));		
		gc.fillRect(96, 0, 32, 32);

		gc.setFill(Color.web("8a46ae"));		
		gc.fillRect(0, 32, 32, 32);		
		gc.setFill(Color.web("68a941"));		
		gc.fillRect(32, 32, 32, 32);
		gc.setFill(Color.web("3e31a2"));		
		gc.fillRect(64, 32, 32, 32);
		gc.setFill(Color.web("d0dc71"));		
		gc.fillRect(96, 32, 32, 32);

		gc.setFill(Color.web("905f25"));		
		gc.fillRect(0, 64, 32, 32);		
		gc.setFill(Color.web("5c4700"));		
		gc.fillRect(32, 64, 32, 32);
		gc.setFill(Color.web("bb776d"));		
		gc.fillRect(64, 64, 32, 32);
		gc.setFill(Color.web("555555"));		
		gc.fillRect(96, 64, 32, 32);

		gc.setFill(Color.web("808080"));		
		gc.fillRect(0, 96, 32, 32);		
		gc.setFill(Color.web("acea88"));		
		gc.fillRect(32, 96, 32, 32);
		gc.setFill(Color.web("7c70da"));		
		gc.fillRect(64, 96, 32, 32);
		gc.setFill(Color.web("ababab"));		
		gc.fillRect(96, 96, 32, 32);

	}

	public Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
		
	    ImageView imageView = new ImageView(source);
	    imageView.setPreserveRatio(preserveRatio);
	    imageView.setFitWidth(targetWidth);
	    imageView.setFitHeight(targetHeight);
	    return imageView.snapshot(null, null);
	    
	}

}
