import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.*;




public class IteratingPixels implements PlugInFilter{	
	public int setup(String s, ImagePlus imp) {
		return PlugInFilter.DOES_8G | PlugInFilter.DOES_STACKS;
		}
	
	public void run(ImageProcessor ip) {
		(new ImagePlus("Enhanced Image", ip.convertToByteProcessor(false))).show();
		
		int[] hist = ip.getHistogram();
		int threshold = entropySplit(hist);
		ip.threshold(threshold);
		}
	 
	 
	 private int entropySplit(int[] hist) {
		 double sum = 0;
		 for (int i = 0; i < hist.length; ++i) {
			 sum += hist[i];
			 }
		 
		 if (sum == 0) {
			 throw new IllegalArgumentException("Empty histogram");
			 }
		 
		 double[] normalizedHist = new double[hist.length];
		 for (int i = 0; i < hist.length; i++) {
			 normalizedHist[i] = hist[i] / sum;
			 }
		 
		 double[] pT = new double[hist.length];
		 pT[0] = normalizedHist[0];
		 for (int i = 1; i < hist.length; i++) {
			 pT[i] = pT[i - 1] + normalizedHist[i];
			 }
		 
		 // Entropy for black and white parts of the histogram
		 final double epsilon = Double.MIN_VALUE;
		 double[] hB = new double[hist.length];
		 double[] hW = new double[hist.length];
		 for (int t = 0; t < hist.length; t++) {
			 // Black entropy
			 if (pT[t] > epsilon) {
				 double hhB = 0;
				 for (int i = 0; i <= t; i++) {
					 if (normalizedHist[i] > epsilon) {
						 hhB -= normalizedHist[i]/pT[t]*Math.log(normalizedHist[i]/pT[t])*
								 Math.cos((Math.PI*pT[t])/9);
						 }
					 }
				 hB[t] = hhB;
				 } 
			 else {
				 hB[t] = 0;
				 }
			 
			 // White  entropy
			 double pTW = 1 - pT[t];
			 if (pTW > epsilon) {
				 double hhW = 0;
				 for (int i = t + 1; i < hist.length; ++i) {
					 if (normalizedHist[i] > epsilon) {
						 hhW -= normalizedHist[i]/pTW * Math.log(normalizedHist[i]/pTW)*
								 Math.cos((Math.PI*pT[t])/9);
						 }
					 }
				 hW[t] = hhW;
				 } 
			 else {
				 hW[t] = 0;
				 }
			 }
		 
		 // Find histogram index with maximum entropy
		 double jMax = hB[0] + hW[0];
		 int tMax = 0;
		 for (int t = 1; t < hist.length; ++t) {
			 double j = (hB[t] + hW[t]);
			 if (j > jMax) {
				 jMax = j;
				 tMax = t;
				 }
			 }
		 return tMax;
		 }//end entropySplit
	 
	 
	 //-----------main rotuine............
	 public static void main(String[] args) {
		 Class<?> colass=IteratingPixels.class;	
		 //IJ.runPlugIn("IteratingPixels", "");
		 new ImageJ();
		 ImageJ.main(args);		 
		 ImagePlus srcij = IJ.openImage();
		 WindowManager.addWindow(srcij.getWindow());
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcij.setTitle("Thresholding");
		 
		 //-----------
	}

}
