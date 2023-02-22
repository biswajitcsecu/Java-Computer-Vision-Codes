import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import boofcv.alg.color.ColorHsv;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.Planar;
import georegression.metric.UtilAngle;



public class RustSegmentColor {

	public static void printClickedColor( final BufferedImage image ) {
		var gui = new ImagePanel(image);
		gui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				float[] color = new float[3];
				int rgb = image.getRGB(e.getX(), e.getY());
				ColorHsv.rgbToHsv((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, color);
				System.out.println("H = " + color[0] + " S = " + color[1] + " V = " + color[2]);

				showSelectedColor("Selected", image, color[0], color[1]);
			}
		});
		ShowImages.showWindow(gui, "Color Selector");
	}

	public static void showSelectedColor( String name, BufferedImage image, float hue, float saturation ) {
		Planar<GrayF32> input = ConvertBufferedImage.convertFromPlanar(image, null, true, GrayF32.class);
		Planar<GrayF32> hsv = input.createSameShape();

		// Convert into HSV
		ColorHsv.rgbToHsv(input, hsv);

		// Euclidean distance squared threshold the selected set
		float maxDist2 = 0.4f*0.4f;

		// Extract hue and saturation bands which are independent of intensity
		GrayF32 H = hsv.getBand(0);
		GrayF32 S = hsv.getBand(1);

		// Adjust the relative importance of Hue and Saturation.
		float adjustUnits = (float)(Math.PI/2.0);

		// step through each pixel and mark how close it is to the selected color
		var output = new BufferedImage(input.width, input.height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < hsv.height; y++) {
			for (int x = 0; x < hsv.width; x++) {
				// Hue is an angle in radians, so simple subtraction doesn't work
				float dh = UtilAngle.dist(H.unsafe_get(x, y), hue);
				float ds = (S.unsafe_get(x, y) - saturation)*adjustUnits;

				// this distance measure is a bit naive
				float dist2 = dh*dh + ds*ds;
				if (dist2 <= maxDist2) {
					output.setRGB(x, y, image.getRGB(x, y));
				}
			}
		}

		ShowImages.showWindow(output, "Showing " + name);
	}

	public static void main( String[] args ) {
		String file = "/home/picox/Projects/Eclipse/RustSegmentColor/src/corn_leaf_in49.jpg";
		BufferedImage image = UtilImageIO.loadImageNotNull(UtilIO.path(file));

		// Let the user select a color
		printClickedColor(image);
		// Display pre-selected colors
		showSelectedColor("Yellow", image, 1f, 1f);
		showSelectedColor("Green", image, 1.5f, 0.65f);
	}
}