import ij.*;
import ij.process.*;
import ij.plugin.filter.*;



public class ColorPixelPro  implements PlugInFilter {
	
	int flags = DOES_RGB;
	ImagePlus imp;
	boolean isRGB;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		isRGB = imp!=null && imp.getType()==ImagePlus.COLOR_RGB;
		
		if (!isRGB)
			flags = flags|CONVERT_TO_FLOAT;
		return flags;
	}
	

	public void run(ImageProcessor ip) {	
		filterRGBImage(ip);				
	}

	public void filterRGBImage(ImageProcessor ip) {
		ImageProcessor dip = ip.duplicate();
		int width = ip.getWidth();
		int height = ip.getHeight();
		
		
		int[] pixels = (int[])dip.getPixels();
		
		float[][] pixelsf = new float[width*height][3];

		for (int i = 0; i < pixelsf.length; i++) {
			int argb = pixels[i];

			int r = (argb >> 16) & 0xff;
			int g = (argb >>  8) & 0xff;
			int b = (argb) & 0xff;
			
			//RGB to YIQ

			pixelsf[i][0] = 0.2990f *r + 0.5870f *g + 0.1140f *b;  
			pixelsf[i][1] = 0.5959f *r - 0.2744f *g - 0.3216f *b;  
			pixelsf[i][2] = 0.2115f *r - 0.5229f *g - 0.3114f *b;  
		}

		//fuzzify--------------
		for (int i = 0; i < pixelsf.length; i++) {
			pixelsf[i][0] = (255.0f-pixelsf[i][0])/255.0f;  
			pixelsf[i][1] = (255.0f-pixelsf[i][1])/255.0f;  
			pixelsf[i][2] = (255.0f-pixelsf[i][2])/255.0f;  
		}
		
		//intensitify------------
		for (int i = 0; i < pixelsf.length; i++) {
			if (pixelsf[i][0]<0.5f) 
				pixelsf[i][0]= 2*(pixelsf[i][0]*pixelsf[i][0]);  
			else
				pixelsf[i][0]= 1-2*(1-pixelsf[i][0])*(1-pixelsf[i][0]); 
			
			if (pixelsf[i][1]<0.5f) 
				pixelsf[i][1]= 2*(pixelsf[i][1]*pixelsf[i][1]);  
			else
				pixelsf[i][1]= 1-2*(1-pixelsf[i][1])*(1-pixelsf[i][1]); 			
			
			if (pixelsf[i][2]<0.5f) 
				pixelsf[i][2]= 2*(pixelsf[i][2]*pixelsf[i][2]);  
			else
				pixelsf[i][2]= 1-2*(1-pixelsf[i][2])*(1-pixelsf[i][2]); 
		}
		
		
		//defuzzification-----------
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int pos =  x + y*width;
				float[] yiq = pixelsf[pos];
			
				float Yc = 255.f*(float) (1.0f-yiq[0]);
				float Ic = 255.f*(float) (1.0f-yiq[1]);
				float Qc = 255.f*(float) (1.0f-yiq[2]);
				
				
				//YIQ to RGB
				float r = (float)(1.0002f*Yc + 0.9560f*Ic + 0.6211f*Qc);
				float g = (float)(1.0001f*Yc - 0.2720f*Ic - 0.6470f*Qc);
				float b = (float)(1.0000f*Yc - 1.1060f*Ic + 1.7030f*Qc);			
				
				//cast---
				int r_ = to255(r);
				int g_ = to255(g);
				int b_ = to255(b);
				
				pixels[pos] = ((r_ & 0xff)<<16) | ((g_ & 0xff)<<8) | (b_ & 0xff) ;
				}
			}
		
		dip.setPixels(pixels);	
	    ImagePlus im = new ImagePlus("Result Image", dip);
		im.show();
		imp.updateAndDraw();
		
	}

	public static int to255(float v) {
        return (v < 255.f ? (int) v : 255);
    }
	
	
    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass=ColorPixelPro.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcij = IJ.openImage();
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcij.setTitle("Color Pixel Image");
			
		 
	}
}