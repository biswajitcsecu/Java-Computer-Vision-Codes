import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.plugin.tool.RoiRotationTool;

 
public class ImageEnhanceType implements PlugInFilter {
	ImagePlus imp;
	private final int flags = DOES_RGB;
    private int width;
	private int height;
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
        return flags;
    }

    
    @Override
	public void run(ImageProcessor ip) {
    	ImageEnhanceRun(ip);
    }


	private void ImageEnhanceRun(ImageProcessor ip) {
		
		//(new ImagePlus("Enhanced Image", ip.convertToColorProcessor())).show();
		
		width = ip.getWidth();
		height = ip.getHeight();
		ImageProcessor I = ip.convertToFloatProcessor();  			
		float drang = ((float) I.getMax()-(float) I.getMin());
		System.out.println("Dynamic range: " + drang);
		
		//-------fuzzification---------------
		
		float[] pixels = new float[width*height];
		pixels = (float[]) I.getPixels();
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int curpix = i + (j * width);
				float pixel = pixels[curpix];	
				pixels[curpix] = (float) ((pixel-(float) I.getMin())/((float) I.getMax()
						-(float) I.getMin()));
			}
		}
		
		
		//------------type2 fuzzification-------------------
		float factor = (float) 0.685;
		float[] pixelsl = new float[width*height];
		float[] pixelsu = new float[width*height];	
		float findx = (float) 0.0;
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int curpix = i + (j * width);
				float pixel = pixels[curpix];
				pixelsu[curpix] = (float) (Math.pow(pixel,factor));
				pixelsl[curpix] = (float) (Math.pow(pixel,(1/factor)));
				findx = findx + Math.abs(pixelsu[curpix] - pixelsl[curpix])/(width*height);
				
			}
		}
		
		System.out.println("Fuzzy index: " + findx);
		
		//-----------Hamacher T conorm-----
		float lam = (float) (((float) I.getMax()-(float) I.getMin())
				/Math.abs(6*(float) I.getMax()));	
		
		lam = (float)(lam + findx);
		float[] pixelse = new float[width*height];
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int curpix = i + (j * width);
				float pixelu = pixelsu[curpix];
				float pixell = pixelsl[curpix];				
				pixelse[curpix] = (float) (pixelu+pixell+(lam-2)*pixelu*pixell)
						/(1-(1-lam)*pixelu*pixell);
				
			}
		}
				
		//-----------defuzzification---------------
		float[] pixelsd = new float[width*height];
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int curpix = i + (j * width);
				float pixelm =pixelse[curpix];				
				pixelsd[curpix] = (float)( (float) I.getMin()+ pixelm*drang);	
				
			       
				//determine color component values
				//int r = ((int)pixelsd[curpix] & 0xff0000) >> 16;
				//int g = ((int)pixelsd[curpix] & 0x00ff00) >> 8;
				//int b = (int)pixelsd[curpix]  & 0x0000ff;
				//pixelsd[curpix] = ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
		}
					
		I.setPixels(pixelsd);			
		I.setInterpolationMethod( ImageProcessor.BILINEAR );
		
		String title = imp.getShortTitle() + "-Enhanced-";
		
		ImagePlus result = new ImagePlus(title, ip);	
		new ImageConverter(result).convertToRGB();
		result.show();
		
		///EdgeDetector ced = new GrayscaleEdgeDetector(ip);
		//FloatProcessor edgeMagnitude = ced.getEdgeMagnitude();
		//(new ImagePlus("Edge Magnitude (Gray)", edgeMagnitude)).show();


	
	
   
	}
	

    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass=ImageEnhanceType.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcij = IJ.openImage();
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 RoiRotationTool v= new RoiRotationTool();
		 //v.showOptionsDialog();
		 srcij.setAntialiasRendering(true);
		 srcij.setActivated();
		 srcij.setTitle("Source Image");
		 srcij.updateAndRepaintWindow();
		 
	}

}
