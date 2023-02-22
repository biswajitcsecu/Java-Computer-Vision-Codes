import ij.plugin.filter.PlugInFilter;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.Blitter;
import ij.process.ImageProcessor;
import imagingbook.lib.ij.IjUtils;




public class LinearBlending  implements PlugInFilter{
	
	static double alpha = 0.5; 
	
	// transparency of foreground image
	ImagePlus fgIm;
	
	// foreground image (to be selected)
	
	
	public int setup(String arg, ImagePlus im) {
		return DOES_ALL;
		}
	
		
	public void run(ImageProcessor ipBG) { 
		// ipBG = I BG
		if(runDialog()) {
			ImageProcessor ipFG = fgIm.getProcessor().convertToByteProcessor(true);
			ipFG.sharpen();
			
			// ipFG = I FG
			ipFG = ipFG.duplicate();
			ipFG.multiply(1 - alpha); 
			
			// I FG ← I FG · (1 − α)
			ipBG.multiply(alpha);
			
			// I BG ← I BG · α
			ipBG.copyBits(ipFG,0,0,Blitter.ADD); // I BG ← I BG +I FG
			}
		}
	
	boolean runDialog() {
		// get list of open images and their titles:
		ImagePlus[] openImages = IjUtils.getOpenImages(true);
		String[] imageTitles = new String[openImages.length];
		
		for (int i = 0; i < openImages.length; i++) {
			imageTitles[i] = openImages[i].getShortTitle();
			}
		
		// create the dialog and show:
		GenericDialog gd =new GenericDialog("Linear Blending");
		gd.addChoice("Foreground image:",	imageTitles, imageTitles[0]);
		gd.addNumericField("Alpha value [0..1]:", alpha, 2);
		gd.showDialog();
		
		if (gd.wasCanceled())
			return false;
		else {
			fgIm = openImages[gd.getNextChoiceIndex()];
			alpha = gd.getNextNumber();
			return true;
			}
		}
	
	public static void main(String args[]) {
		Class<?>clavar = LinearBlending.class;
		new ImageJ();
		ImagePlus imgj = IJ.openImage();
		imgj.show();
		IJ.runPlugIn(clavar.getName(), " ");
		imgj.updateAndDraw();
		
	}

}


