
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.opencv.core.*;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;


class ImageFusion{
	static ListDisplayPanel mainPanel = new ListDisplayPanel();
	
	public void rundemo() throws IOException {
       
 
        // Load the image
        String filea ="/home/picox/Projects/Eclipse/IFIFusion/src/LF1.png";
        String fileb ="/home/picox/Projects/Eclipse/IFIFusion/src/RF1.png";
        
        File inputx = new File(filea);
        BufferedImage imageX = ImageIO.read(inputx);
        
        File inputy = new File(fileb);
        BufferedImage imageY = ImageIO.read(inputy);
        
        
        BufferedImage imageA =fuzzificationbuf (imageX); 
        BufferedImage imageB =fuzzificationbuf (imageY); 
          
        displayimg(imageA, imageB);		
        //---------save ---------data------
        //File ouptut = new File("fused.png");
        //ImageIO.write(imageR, "png", ouptut);
	}
	

	
	//------------fuzzification-------------------
	public static BufferedImage fuzzificationbuf (BufferedImage X) {
    	int w = X.getWidth();
    	int h = X.getHeight();
    	BufferedImage Y = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    	for(int i=0; i<h; i++) {
    		for(int j=0;j<w; j++) {
    			double a = X.getRGB(i, j);
    			a = (255.0-a)/255.0;
    			Y.setRGB(i, j, (int) a);     			
    		}    		
    	}  	  
   	 return Y; 
     }

	
	//-----------Fuzzy entropy--------- 
	public float fuzzyentropyfunc(BufferedImage img){
    	return (float) 0.0;
    	}
	
	//-------------------Intuitionistic fmd---------------
	public BufferedImage intuitionisticfmdfunc(BufferedImage img){
    	 return img;
    	 
     }
	
	//-----------------Intuitionistic type-2fmd------------------
	public BufferedImage intuitionistict2fmdfunc(BufferedImage img){

	 return img; 
	 }     
	
	 //-----------type-2fmd distance measure------
	public float t2fmdmeasurefunc(BufferedImage imgA, BufferedImage imgB){
	   return 0;
	   
   }
	
	
	//--------------Pythagorean fuzzy sets------------
	public BufferedImage pythagoreanfuzzyfunc(BufferedImage img){
    	return img;
    	}
	
	 //-----------------Fusion rules------------
	public BufferedImage fusionrulesfunc(BufferedImage imgA, BufferedImage imgB){
	   return imgA;
	   
	   } 
	
	
	
	//-------------Defuzzification------------- 
    public static BufferedImage defuzzificationbuf (BufferedImage X) {
    	int w = X.getWidth();
    	int h = X.getHeight();
    	
    	BufferedImage Y =  new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    	for(int i=0; i<h; i++) {
    		for(int j=0;j<w; j++) {
    			int a = X.getRGB(i, j);
    			a= (255-a*255);
    			Y.setRGB(i, j, a);     			
    		}    		
    	}  	  
   	 return Y; 
     }
	
   //---------------display--------------
   public void displayimg(BufferedImage imageA, BufferedImage imageB) {
			ListDisplayPanel panel = new ListDisplayPanel();
			panel.addImage(imageA, "Left_Focus");
			panel.addImage(imageB, "Right_Focus");
			panel.setPreferredSize(new Dimension(imageA.getWidth(), imageA.getHeight()));
			mainPanel.addItem(panel, "Fusion");
			ShowImages.showWindow(mainPanel, "Fusion", true);
	}
		
	
}



public class IFIFusion {

	public static void main(String[] args) throws Exception{
		try{
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			new ImageFusion().rundemo();
			}catch (Exception e) {
				System.out.println("error: " + e.getMessage());
			}
	}

}
