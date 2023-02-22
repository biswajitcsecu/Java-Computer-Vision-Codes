import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import boofcv.alg.enhance.EnhanceImageOps;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;



public class PixelProImage implements PlugInFilter
{
	private ImagePlus im=null;

	
	public int setup(String arg, ImagePlus im)
	{
		this.im = im;
		return DOES_RGB;
	}
	
	public void run(ImageProcessor ip) 
	{
		rundemo(ip);
	}
	
	public void rundemo(ImageProcessor ip)
	{
		ip.convertToFloat();		
		int w = ip.getWidth();
		int h = ip.getHeight();		
		
		int[] pixels = (int[])ip.getPixels();
		int r, g, b;
		
		int[] pixel = new int[pixels.length];	
		
		
		//color RGB processing-------------
		for(int i = 0; i < pixels.length; i++){		
			//position----------	
		
			int idx = (int) pixels[i];			
			
			//rgb channel------------
			r = (idx & 0xff0000) >> 16;
			g = (idx & 0x00ff00) >> 8;
			b =  idx & 0x0000ff;
		
			//combine rgb------------------
			int p = ((r & 0xFF) << 16) |((g & 0xFF) << 8)  | ((b & 0xFF) << 0);
			pixel[i] = p;
            
			}		

		ColorProcessor ipc = new ColorProcessor(w, h, pixel);
		ImagePlus imc = new ImagePlus("Color Image", ipc);
		imc.setProcessor(ipc);
		//imc.show();

		//--------------------------------------------------------//


		
		//----BfCv------Stages------		
		BufferedImage res = ipc.getBufferedImage();
		
		//Gaussian Coefficients
		float[] weights = {};
		double alpha= 2.0f;
		double sigma= 3.0f;
		weights = getGaussianCoefficients(alpha, sigma);
		Kernel kernel = new Kernel(3,3,weights);
		ConvolveOp op = new ConvolveOp(kernel);		
		BufferedImage filtered = op.filter(res, null) ;	
		
		//BfCv display------------
		ListDisplayPanel mainPanel = new ListDisplayPanel();
		ListDisplayPanel panel = new ListDisplayPanel();
		
		//source image----------
		GrayU8 gray = ConvertBufferedImage.convertFromSingle(res, null, GrayU8.class);		
		panel.addImage(ConvertBufferedImage.convertTo(gray, null), "Source");
		
		//sharpen---------4 and 8---------
		GrayU8 adjusted = gray.createSameShape();
		EnhanceImageOps.sharpen4(gray, adjusted);
		panel.addImage(ConvertBufferedImage.convertTo(adjusted, null), "Sharpen-4");
		
		EnhanceImageOps.sharpen8(gray, adjusted);
		panel.addImage(ConvertBufferedImage.convertTo(adjusted, null), "Sharpen-8");

		
		//Filtered image----------
		GrayU8 gfgray = ConvertBufferedImage.convertFromSingle(filtered, null, GrayU8.class);		
		panel.addImage(ConvertBufferedImage.convertTo(gfgray, null), "Gaussian Filtered");
		
		
		//panel show--------------
		panel.setPreferredSize(new Dimension(gray.width, gray.height));
		mainPanel.addItem(panel, "Filtering");
		mainPanel.setVisible(true);
		ShowImages.showWindow(mainPanel, "Filtering", true);
		
	}
	
	
	float[] getGaussianCoefficients(double alpha, double sigma){
		int w = (int)Math.ceil(alpha*sigma);
		float result [] = new float[w*2+1];
		for(int n=0; n<=w; n++){
			double coefficienet =Math.exp(-(n*n)/(2*sigma*sigma))/(Math.sqrt(2*Math.PI)*sigma);
			result[w+n] = (float)coefficienet;
			result[w-n] = (float)coefficienet;
			}
		return  result;
		}
	

	//-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass=PixelProImage.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcij = IJ.openImage();
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcij.setActivated();
		 srcij.setTitle("Source Image");
		 srcij.updateAndRepaintWindow();
		 
	}
}