

import org.opencv.core.*;
import org.visp.core.VpCameraParameters;
import org.visp.core.VpColVector;
import org.visp.core.VpImageRGBa;
import org.visp.core.VpImageUChar;
import org.visp.core.VpMatrix;
import org.visp.core.VpRGBa;



public class TestJavaOpencv {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	static{System.loadLibrary("visp_java350");}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//--------------java opencv......................
		System.out.println("Hello java opencv");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    System.out.println("Welcome to OpenCV " + Core.VERSION);
	    Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
	    System.out.println("OpenCV Mat: " + m);
	    Mat mr1 = m.row(1);
	    mr1.setTo(new Scalar(1));
	    Mat mc5 = m.col(5);
	    mc5.setTo(new Scalar(5));
	    System.out.println("OpenCV Mat data:\n" + m.dump());
	    
	  //--------------java visp......................
	    System.out.println("Hello java visp");

	    // VpMatrix
	    VpMatrix vp = new VpMatrix(2,3,1.5);
	    System.out.println(vp.getCol(0));
	    System.out.println(vp.transpose());
	                
	    // VpColVector
	    VpColVector vpColVector = new VpColVector(10,1.5);
	    System.out.println(vpColVector.infinityNorm());
	                
	    // VpImageUChar
	    VpImageUChar imageUChar = new VpImageUChar(2, 4, (byte)220);
	    System.out.println(imageUChar);
	                
	    // VpImageRGBa
	    VpImageRGBa colorImage = new VpImageRGBa(3, 5, new VpRGBa((char)255,(char)0,(char)0,(char)255));
	    System.out.println(colorImage);
	                
	    // VpCameraParameters
	    VpCameraParameters vpCameraParameters = new VpCameraParameters(1.0, 1.0, 1.0, 1.0);
	    System.out.println(vpCameraParameters.get_K());

	}

}
