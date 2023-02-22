import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;



 
public class ImageEnhancePythagorean implements PlugInFilter {
	ImagePlus imp;
	private final int flags = DOES_ALL;
    private int width;
	private int height;
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
        return flags;
    }

    
    @Override
	public void run(ImageProcessor ip) {
    	(new ImagePlus("Source Image", ip.convertToByte(false))).show();
    	PythagoreanRun(ip);
    }


	private void PythagoreanRun(ImageProcessor ip) {

		width = ip.getWidth();
		height = ip.getHeight();
		ImageProcessor I= ip.duplicate();		
		I = I.convertToFloat();  			
		float drang = ((float) I.getMax()-(float) I.getMin());
		System.out.println("Dynamic range: " + drang);
		
		//-------fuzzification---------------
		
		float[] pixelf = new float[width*height];
		pixelf = (float[]) I.getPixels();
		float ent = (float) 0.0;
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);
				float pixel = pixelf[pix];	
				pixelf[pix] = (float) ((pixel-(float) I.getMin())/((float) I.getMax()
						-(float) I.getMin()));
				float a = pixelf[pix] ;
				ent =  ent + (float)(Math.max(a,(1.0-a))/(width*height));
			}
		}
		
		System.out.println("Fuzzy entropy: " + ent);

		
		//-------intutionistic hesitancy degreen---------------
		float[] pixelimf = new float[width*height];
		float[] pixelnmf = new float[width*height];
		float[] pixelhmf = new float[width*height];
		pixelf = (float[]) I.getPixels();
		
		float factor = ent;
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);
				float pixel = pixelf[pix];
				pixelimf[pix] = (float) (1.0 - Math.pow((1.0-pixel),factor));
				pixelnmf[pix] = (float) (Math.pow((1.0-pixel),0.5*factor*(1+factor)));
				pixelhmf[pix] = (float) (1.0 - pixelimf[pix] - pixelnmf[pix]);
				
				
			}			
		}
		
		//------------Pythagorean fmg-------------------
		float[] pixelps = new float[width*height];		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);				
				float a = pixelimf[pix];
				float b = pixelnmf[pix];
				//float c = pixelhmf[pix];				
				float d = (float) (1.0 -(Math.pow(a,2) + Math.pow(b,2)));
				pixelps[pix] =  (float) (Math.pow(d,1.275));
			}
		}
		
		 
		//------------type2 fuzzification-------------------
	
		float[] pixelsl = new float[width*height];
		float[] pixelsu = new float[width*height];	
		float findx = (float) 0.0;
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);
				float pixel = pixelps[pix];
				pixelsu[pix] = (float) (Math.pow(pixel,factor));
				pixelsl[pix] = (float) (Math.pow(pixel,(1/factor)));
				findx = findx + Math.abs(pixelsu[pix] - pixelsl[pix])/(width*height);
				
			}
		}
		
		System.out.println("Fuzzy index: " + findx);
		
		//-----------Hamacher T conorm-----
		float lamda = (float) ((ent + findx)/2);
		System.out.println("Fuzzy Hamacher index: " + lamda);
		
		float[] pixelse = new float[width*height];		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);
				float pixelu = pixelsu[pix];
				float pixell = pixelsl[pix];				
				pixelse[pix] = (float) (pixelu+pixell+(lamda-2)*pixelu*pixell)
						/(1-(1-lamda)*pixelu*pixell);
				
			}
		}
		
		//-----------fuzzy modification ---------------
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);
				float pixv =pixelse[pix];	
				if(pixv<=0.5f) {
				pixelse[pix] = (float)(2*pixv*pixv);
				}
				else {
				pixelse[pix] = (float)( 1-(2*(1-pixv)*(1-pixv)));
				}

			}
		}

				
		//-----------defuzzification---------------
		float[] pixeld = new float[width*height];
		
		for (int j = 0; j < height; j++ ) {
			for (int i = 0; i < width; i++)	{				
				//..........retrive pixels.............
				int pix = i + (j * width);
				float pixv =pixelse[pix];				
				pixeld[pix] = (float)( (float) I.getMin() + pixv*drang);	

			}
		}
		
		I.setPixels(pixeld);			
		I.setInterpolationMethod( ImageProcessor.BILINEAR );
		ip = I.convertToByte(false);
		imp.setProcessor(ip);		


	}
	

    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass=ImageEnhancePythagorean.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcij = IJ.openImage();
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	

		 
	}

}