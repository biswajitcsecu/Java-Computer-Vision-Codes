


import ij.*;
import ij.process.*;
import ij.plugin.filter.*;




public class FilterAverageJ implements PlugInFilter{
	ImagePlus imp;
	final int K = 4;
	int flags = DOES_8G;

	
	@Override
	public int setup(String arg0, ImagePlus imp) {
		// TODO Auto-generated method stub
		this.imp = imp;
		return flags;
	}
	
	
	@Override
	public void run(ImageProcessor orig) {
		int w = orig.getWidth();
		int h = orig.getHeight();
		new ImagePlus("Original", orig).show();
		ImageProcessor copy = orig.duplicate();
		
		//----------------
		double sigma= 0.25;
		float[] Kernel = new float[2*K+1];
		Kernel= makeGaussKernel1d(sigma);
		
		for (int v = 1; v <= h-2; v++) {
			for (int u = 1; u <= w-2; u++) {
				//compute ﬁlter result for position (u, v)
				int sum = 0;
				for (int j = -1; j <= 1; j++) {
					for (int i = -1; i <= 1; i++) {
						int p = copy.getPixel(u+i, v+j);
						//double c = Kernel[j+1][i+1];
						sum = sum + p;
						}
					}
				int q = (int) Math.round(sum/9.0);
				orig.putPixel(u, v, q);
				}
			}
		
		
		

		//----------
		double[][] filter = {
				{0.075, 0.125, 0.075},
				{0.125, 0.200, 0.125},
				{0.075, 0.125, 0.075}
				};
		
		for (int v = 1; v <= h-2; v++) {
			for (int u = 1; u <= w-2; u++) {
				// compute ﬁlter result for position (u, v)
				double sum = 0;
				for (int j = -1; j <= 1; j++) {
					for (int i = -1; i <= 1; i++) {
						int p = copy.getPixel(u+i, v+j);
						// get the corresponding ﬁlter coeﬃcient:
						double c = filter[j+1][i+1];
						sum = sum + c * p;
						}
					}
				int q = (int) Math.round(sum);
				orig.putPixel(u, v, q);
				}
			}
		orig.convertToByteProcessor();		
		}

	
	
	float[] makeGaussKernel1d(double sigma) {
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
	
    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass = FilterAverageJ.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcimp = IJ.openImage();
		 srcimp.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcimp.setTitle("box filter");	
		 srcimp.createImagePlus();
		 srcimp.getOriginalFileInfo();
		 
	}

}

