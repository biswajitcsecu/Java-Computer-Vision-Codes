import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;



public class ParticleanalyzerImagev implements PlugInFilter, Measurements{
	
	int myMinSize = 1;
	int myMaxSize = 999999;				
	double myMinCirc = 0.80;			
	double myMaxCirc = 1.00;
	
	int myOptions = ParticleAnalyzer.SHOW_RESULTS + 
			ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES;	
	
	int myMeasurements = ParticleAnalyzer.LABELS + 
			ParticleAnalyzer.AREA + ParticleAnalyzer.PERIMETER + 
			ParticleAnalyzer.CIRCULARITY + ParticleAnalyzer.RECT + 
			ParticleAnalyzer.SLICE;	
	
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;
		}
	
	public void run(ImageProcessor ip) {
		ImagePlus myImPlus = WindowManager.getCurrentImage();
		ip.autoThreshold();	
		ResultsTable myRT = new ResultsTable();	
		ParticleAnalyzer myPA = new ParticleAnalyzer(myOptions, myMeasurements, myRT, myMinSize, myMaxSize, myMinCirc, myMaxCirc);			
		myPA.analyze(myImPlus, ip);				
	}

	//-----------main rotuine............
	public static void main(String[] args) {
		Class<?> colass=ParticleanalyzerImagev.class;
		new ImageJ();
		ImageJ.main(args);
		ImagePlus cimg = IJ.openImage();
		cimg.show();
		cimg.setTitle("Particle analyzer");
		cimg.updateAndDraw();
		IJ.runPlugIn(colass.getName(), null);
	}

}