
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class HoughLinesRun {

    public void run(String[] args) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;
        String filename = "/home/picox/Projects/Eclipse/HoughLines/src/Merry4.jpg";

        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);

        // Check image is loaded
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.exit(-1);
        }
  
        // Edge detection
        Imgproc.Canny(src, dst, 50, 200, 3, false);
       

        // Copy edges to the images
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();

        // Standard Hough Line Transform
        Mat lines = new Mat(); 
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); 
        
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];

            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

        // Probabilistic Line Transform
        Mat linesP = new Mat(); 
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); 
        
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]),
            		new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

        // Show results
        HighGui.namedWindow("Source", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("Source", src); 
        HighGui.namedWindow("Detected Lines", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("Detected Lines", cdstP);

        // Wait and Exit
        HighGui.waitKey();
        HighGui.destroyAllWindows();
        System.exit(0);

    }
}

public class HoughLines {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughLinesRun().run(args);
    }
}



