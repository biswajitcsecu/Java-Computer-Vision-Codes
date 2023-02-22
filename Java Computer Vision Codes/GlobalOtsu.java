
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import imagingbook.common.threshold.global.OtsuThresholder;



public class GlobalOtsu implements PlugInFilter {
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;
		}	
	
	@Override
	public void run(ImageProcessor ip) {
		ByteProcessor bp = (ByteProcessor) ip;
		
		OtsuThresholder thr = new OtsuThresholder();
		int q = thr.getThreshold(bp);
		if (q >= 0) {
			IJ.log("threshold = " + q);
			bp.threshold(q);
			}
		else {
			IJ.showMessage("no threshold found");
			}
		}
	
	//-----------main rotuine............
	public static void main(String[] args) {
		Class<?> colass=GlobalOtsu.class;
		new ImageJ();
		ImageJ.main(args);
		ImagePlus srcij = IJ.openImage();
		srcij.show();
		IJ.runPlugIn(colass.getName(), null);	
		srcij.setTitle("Source Image");
		srcij.updateAndDraw();	
		}
	}

