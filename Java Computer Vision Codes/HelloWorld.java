
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;

class HelloWorld implements PlugInFilter{
    public static void main(String[] args) {
        int width = 400;  
        int height = 400;  
        ImageProcessor ip = new ByteProcessor(width, height);  
        String title = "My new image";  
        ImagePlus imp = new ImagePlus(title, ip);  
        imp.show();  
              
        System.out.println("Hello, World!"); 
    }

	@Override
	public void run(ImageProcessor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
}