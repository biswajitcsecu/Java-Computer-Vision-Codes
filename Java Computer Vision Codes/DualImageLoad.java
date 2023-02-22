import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
class ImageLoad extends JFrame{
	public void run()  throws IOException{
	// TODO Auto-generated method stub
	 		File file = new File("/home/picox/Projects/Eclipse/JavaDualImageLoad/src/src1.jpg");
	 		BufferedImage src =  ImageIO.read(file);
	 		int iw = src.getWidth();
	 		int ih = src.getHeight();
	 		
	 		//RGB Filter
	 		BufferedImage dst = new  BufferedImage(iw,ih,BufferedImage.TYPE_3BYTE_BGR);
	 		final float[] SHARPEN3x3 =  {0.f, -1.f, 0.f, -1.f, 5.0f, -1.f,0.f, -1.f, 0.f}; 		
	 	
	 		Kernel kernel = new Kernel(3,3,SHARPEN3x3);	 		 
	 		ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,null);
	 		cop.filter(src,dst);	
	 		
			// Create and set up the window.
			JFrame frame = new JFrame("Color Filtering");        
	        
	        // Set up the content pane.
			JPanel pan =new JPanel();
			JLabel l1 = new JLabel(new ImageIcon(src));
			JLabel l2 = new JLabel(new ImageIcon(dst));
			pan.setLayout(new FlowLayout());  

			pan.add(l1);
			pan.add(l2);

	        // Display the window.
			frame.getContentPane().add(pan);
	        frame.pack();
	        frame.setLocationRelativeTo(null); 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
	        

	}
}


public class DualImageLoad {
	public static void main(String[] args) throws IOException  {
		ImageLoad ld= new ImageLoad();
		ld.run();		
	}

}
