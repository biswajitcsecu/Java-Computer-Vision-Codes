import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


class BilateralFilter{
	
	public void run(String[] args) {
		//---Load
		String file ="/home/picox/Projects/Eclipse/BilateralCVFilter/src/mam2.png";
		Mat src = Imgcodecs.imread(file);
       
		if( src.empty() ) {
            System.out.println("Error opening image!");
            System.exit(-1);
        }

		Mat dst = new Mat();
		Imgproc.bilateralFilter(src, dst, 15, 80, 80, Core.BORDER_DEFAULT);
        
		//-- Show 
		HighGui.namedWindow("Mamography");
		HighGui.imshow("Mamography", src);
		HighGui.namedWindow("BilateralFilter");
        HighGui.imshow("BilateralFilter", dst);
        //--Write
		Imgcodecs.imwrite("bilfilter.png", dst);
		System.out.println("Bilateral Filter Processed");	
		HighGui.waitKey(0);
		HighGui.destroyAllWindows();
		
	}
}


public class BilateralCVFilter {
	static {System.loadLibrary( Core.NATIVE_LIBRARY_NAME );}
	public static void main(String args[]) {
		new BilateralFilter().run(args);
		System.exit(0);
	   }
	}

