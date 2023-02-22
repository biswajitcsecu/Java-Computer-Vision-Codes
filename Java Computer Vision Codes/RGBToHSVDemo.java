import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;




public class RGBToHSVDemo implements PlugInFilter
{
    public int setup(String arg, ImagePlus im)
    {
        return DOES_RGB;
    }
    
    public void run(ImageProcessor ip)
    {
    	RGBToHSrun(ip);
    	HSVtoRGBrun(ip);
    }
    
    public void RGBToHSrun(ImageProcessor ip)
    {
        int width = ip.getWidth(), height = ip.getHeight();
        int r, g, b, hi, lo; // RBG color components
        int[] pixels = (int[])ip.getPixels();
        float[] h = new float[pixels.length];
        float[] s = new float[pixels.length];
        float[] v = new float[pixels.length]; // arrays for hue / saturation / value 
        float hue = 0.0f, sat = 0.0f, val = 0.0f;
        float rNorm, gNorm, bNorm, rng;
        final float max = 255.0f;
        
        for(int i = 0; i < pixels.length; i++)
        {
            // determine color component values
            r = (pixels[i] & 0xff0000) >> 16;
            g = (pixels[i] & 0x00ff00) >> 8;
            b = pixels[i] & 0x0000ff;
            
            // find maximum component value and set
            // range, saturation, value accordingly
            hi = Math.max(r, Math.max(g, b));
            lo = Math.min(r, Math.max(g, b));
            rng = (float)(hi - lo);
            if(hi > 0)
                sat = rng / (float)hi;
            else
                sat = 0.0f;
            val = (float)hi / max;
            
            if(rng > 0)
            {
                rNorm = (float)(hi - r) / rng;
                gNorm = (float)(hi - g) / rng;
                bNorm = (float)(hi - b) / rng;
                
                // preliminary (NOT ACTUAL) value for H 
                if(hi == r)
                    hue = bNorm - gNorm;
                else if(hi == g)
                    hue = rNorm - bNorm + 2;
                else
                    hue = gNorm - rNorm + 4;
            
                // normalized value for H (if necessary)
                if(hue < 0)
                    hue += 6;
                
                    hue = hue / 6;
            }
            else
            {
                rNorm = 0.0f;
                gNorm = 0.0f;
                bNorm = 0.0f;
            }
            
            
            
            
            // insert HSV values into their respective arrays
            h[i] = hue;
            s[i] = sat;
            v[i] = val;
            
        }
        
        // set the new image array values 
        FloatProcessor fp1 = new FloatProcessor(width, height, h, null);
        FloatProcessor fp2 = new FloatProcessor(width, height, s, null);
        FloatProcessor fp3 = new FloatProcessor(width, height, v, null);
        
        // display the new images
        ImagePlus temp1 = new ImagePlus("H", fp1);
        ImagePlus temp2 = new ImagePlus("S", fp2);
        ImagePlus temp3 = new ImagePlus("V", fp3);
        temp1.show();
        temp2.show();
        temp3.show();
    }
    
    public void HSVtoRGBrun(ImageProcessor ip) { 
        int div = 255 * 255 * 3; 
        int add = div >> 1;  // add = div /2 by rightshift

        int[] pixels = (int[]) ip.getPixels();

        //iterate over all pixels
        for (int j = 0; j < pixels.length; j++) {

            //get int-packed color pixel
            int c = pixels[j];

            //extract RGB components from color pixel
            int i = (c & 0xff0000) >> 16;
            int h = (c & 0x00ff00) >> 8;
            int s = (c & 0x0000ff);

            i = 3 * i;
            h = 3 * h;
                
            //red, green, blue
            int r = 0, g = 0, b = 0, e = 0;
            int d = 255 * (255 - s);

            if (h <= 255) {
               e = 3 * s * h;
               r = i * (65025 + 510 * s - e);
               g = i * (d + e);
               b = i * d;
            }   
            else if (h <= 510) {
               e = 3 * s * (h - 255);
               r = i * d;
               g = i * (65025 + 510 * s - e);
               b = i * (d + e);
            }
            else {
               e = 3 * s * (h - 510);
               r = i * (d + e);
               g = i * d;
               b = i * (65025 + 510 * s - e);
            }

            //re-scale r, g, b to [0-255]; add half of divisor to keep round-off errors small
            r = Math.min(255, Math.max(0, ((r + add) / div)));
            g = Math.min(255, Math.max(0, ((g + add) / div)));
            b = Math.min(255, Math.max(0, ((b + add) / div)));

            // reassemble color pixel
            c = ((r & 0xff)<<16) | ((g & 0xff)<<8) | b & 0xff; 
            pixels[j] = c;
        }
        ip.setPixels(pixels);
        ImagePlus temp = new ImagePlus("H", ip);
        temp.show();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass=RGBToHSVDemo.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcij = IJ.openImage();
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcij.setTitle("Color Pixel Image");
			
		 
	}
}
