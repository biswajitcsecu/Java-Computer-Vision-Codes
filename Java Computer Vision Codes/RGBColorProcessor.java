import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.Blitter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;


public class RGBColorProcessor implements PlugInFilter {
	// component indices
	static final int R = 0, G = 1, B = 2; 
	ImagePlus imp;
	
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;		
		return DOES_RGB; 
		// RGB images
		}
	
	public void run(ImageProcessor ip) {
		//type ColorProcessor
		(new ImagePlus("Src Image", ip.convertToColorProcessor())).show();	
		rgbrun(ip);
		}
	
	
	public void rgbrun(ImageProcessor ip) {
		//type ColorProcessor		
		ColorProcessor cp = (ColorProcessor) ip;
		int w= cp.getWidth();
		int h = cp.getHeight();
		int[] color = new int[3];
		final int  bias = 25;
		imp.trimProcessor();
		
		//color components-------------
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				cp.getPixel(i, j, color);				
				color[R] = Math.min((color[R]+bias), 255);
				color[G] = Math.min((color[G]+bias), 255); 
				color[B] = Math.min((color[B]+bias), 255);
				cp.putPixel(i, j, color);
				}
			}	
		cp.convertToRGB();	
		cp.snapshot();
		double sigma =2.0;
		double a = 3.0;
		unsharpMask(cp,sigma,a);
		imp.updateAndDraw();

		}
	
	public void unsharpMask(ImageProcessor ip,double sigma, double a) {		
		ImageProcessor I = ip.convertToFloat(); 
		ImageProcessor J = I.duplicate();
		float[] H = GaussKernel1d(sigma);
		Convolver cv = new Convolver();
		cv.setNormalize(true);
		cv.convolve(J, H, 1, H.length);
		cv.convolve(J, H, H.length, 1);
		I.multiply(1+a);
		J.multiply(a);
		I.copyBits(J,0,0,Blitter.SUBTRACT); 
		ip.insert(I.convertToByteProcessor(), 0, 0);
		
		//rgbColorrun(ip);
		ip.convertToRGB();
	
	}
	
	float[] GaussKernel1d(double sigma) {
		// create the kernel
		int center = (int) (3.0*sigma);
		float[] kernel = new float[2*center+1]; 
		double sigma2 = sigma * sigma;
		for (int i=0; i<kernel.length; i++) {
			double r = center - i;
			kernel[i] = (float) Math.exp(-0.5 * (r*r) / sigma2);
			}
		return kernel;
		}
	
	
	public void rgbColorrun(ImageProcessor ip){
		int width = ip.getWidth();
		int height = ip.getHeight();
		int[] pixels = (int[])ip.getPixels();		
		int[] y = new int[pixels.length];
		
		for(int i = 0; i < pixels.length; i++)
		{
			int pix = pixels[i];			
			y[i] = pix << 16 | pix << 8 | pix;		
        }
		ColorProcessor fp = new ColorProcessor(width, height, y);		
		ImagePlus temp = new ImagePlus("Color", fp);		
		temp.show();
	
	}
	
	//-----------main rotuine............
	public static void main(String[] args) {
		Class<?> colass=RGBColorProcessor.class;
		new ImageJ();
		ImageJ.main(args);
		String var=ImageJ.VERSION;
		System.out.print("ImageJ version" + var);
		ImagePlus cimg = IJ.openImage();
		cimg.show();
		cimg.setTitle("Colorupdate");
		cimg.updateAndDraw();
		IJ.runPlugIn(colass.getName(), null);
		
	}
}