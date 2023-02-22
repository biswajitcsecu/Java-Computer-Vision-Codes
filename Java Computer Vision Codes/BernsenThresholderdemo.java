import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class BernsenThresholderdemo implements PlugInFilter{
	ImagePlus imp;
	private final int flags = DOES_ALL;


	
	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
        return flags;
    }
    @Override
	public void run(ImageProcessor ip) {
    	
    	BernsenThresholder bt = new BernsenThresholder();    	
    	ByteProcessor bp = bt.getThreshold(ip.convertToByteProcessor());    	
		ip.insert(bp.convertToByteProcessor(false), 0, 0);
		new ImagePlus("Src Image", bp).show();
		

    }
	//-----------main rotuine............
	public static void main(String[] args) {
		Class<?> colass=BernsenThresholderdemo.class;
		new ImageJ();
		ImageJ.main(args);
		String var=ImageJ.VERSION;
		System.out.print("ImageJ version" + var);
		ImagePlus cimg = IJ.openImage();
		cimg.show();
		cimg.setTitle("Bernsen Threshold");
		cimg.updateAndDraw();
		IJ.runPlugIn(colass.getName(), null);
		
	}

}
