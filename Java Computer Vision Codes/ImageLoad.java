import java.awt.image.BufferedImage;
import java.io.IOException;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;



public class ImageLoad  implements PlugInFilter{
	
	ImagePlus imp;	
	int redIncrement =20;
	int greenIncrement =10;
	int blueIncrement = -64;
	
	//------------setup----------
	@Override
	public int setup(String arg0, ImagePlus arg1) {
		this.imp=arg1;
		return DOES_ALL;
	}

	//-------run---------------------
	public void run(ImageProcessor ip) {
		int w= ip.getWidth();
		int h= ip.getHeight();
		
		BufferedImage bfsrc =ip.getBufferedImage();
		for (int p = 0;  p<w;  p++) {
			for (int q = 0;  q<h; q++ ) {
                int rgb = bfsrc.getRGB(p, q);
                
                int red = (rgb >> 16) & 255;
                int green = (rgb >> 8) & 255;
                int blue = rgb & 255;
                
                red = Math.max(0, Math.min(0xff, red + redIncrement));
                green = Math.max(0, Math.min(0xff, green + greenIncrement));
                blue = Math.max(0, Math.min(0xff, blue + blueIncrement));
                rgb= ( (red << 16) | (green << 8) | blue);
                
                bfsrc.setRGB(p, q, rgb);
			}
		}
		
		imp= new ImagePlus("BufferedImage", new ColorProcessor(bfsrc) );
		imp.setProcessor(ip);
		
		
		

	}
	
	//-------------------main entry.......................
	public static void main(String[] args) throws IOException {
		try {
			Class<?>clavar= ImageLoad.class;
			new ImageJ();
			ImagePlus imgj = IJ.openImage();
			imgj.show();
			IJ.runPlugIn(clavar.getName(), null);
			imgj.updateAndDraw();
			
		}catch( Exception e) {
			System.out.println("Error found" + e.getMessage());
			}
		}

	}


