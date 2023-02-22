import ij.*;
import ij.process.*;
import ij.plugin.filter.*;




public class LinearHistoEqual implements PlugInFilter{
	ImagePlus imp;
	int flags = DOES_8G;

	
	@Override
	public int setup(String arg0, ImagePlus imp) {
		// TODO Auto-generated method stub
		this.imp = imp;
		return flags;
	}
	
	
	@Override
	public void run(ImageProcessor ip) {
		// TODO Auto-generated method stub
		ImageProcessor dip=ip.duplicate();
		new ImagePlus("Original Image", dip).show();
		int M = ip.getWidth();
		int N = ip.getHeight();
		int K = 256; 
		
		// compute the cumulative histogram:
		int[] H = ip.getHistogram();
		for (int j = 1; j < H.length; j++) {
			H[j] = H[j - 1] + H[j];
		}
		// equalize the image:
		for (int v = 0; v < N; v++) {
			for (int u = 0; u < M; u++) {
				int a = ip.get(u, v);
				int b = H[a] * (K - 1) / (M * N); 
				ip.set(u, v, b);
				}
		}
		ip.convertToByteProcessor();
		ip.createImage();
		imp.updateAndDraw();
		imp.flush();
	}

    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass = LinearHistoEqual.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcimp = IJ.openImage();
		 srcimp.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcimp.setTitle("Linear histogram equalization");	
		 srcimp.createImagePlus();
		 srcimp.getOriginalFileInfo();
		 
	}

}
