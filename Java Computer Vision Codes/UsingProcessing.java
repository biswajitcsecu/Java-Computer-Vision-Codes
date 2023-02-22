
import processing.core.PApplet;
import processing.*;
import processing.core.PApplet;
import processing.core.PImage;

public class UsingProcessing extends PApplet {
	PImage img;
	
    public void settings() 
	{
		size(800, 700);
	}
	
	public void setup()
	{
		
		background(255);
		stroke(0);
		String  file = "/home/picox/Projects/Eclipse/UsingProcessingJava/src/corn_leaf_h1.jpg";
		img	= loadImage(file);
		img.resize(0, height);
		image(img, 0, 0);
	}

	public void draw()
	{
		int[] color = sunColorSec(second());
		fill(color[0], color[1], color[2]);
		// Draw the sun
		ellipse(width / 4, height / 5,width / 4, height / 5);
	}



	public int[] sunColorSec(float seconds)
	{
		int[] rgb = new int[3];
		float diffFrom30= Math.abs(30 - seconds);

		// RGB in an array
		float ratio = diffFrom30 / 30;
		rgb[0] = (int)(255 * ratio);
		rgb[1] = (int)(255 * ratio);
		rgb[2] = 0;

		return rgb;
	}

	// Driver code
 public static void main(String[] args) {
        PApplet.main("UsingProcessing");
       
    }
}
