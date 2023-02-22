import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;





public class ImageDualShowJ  implements PlugInFilter {
	ImagePlus imp;
	private final int flags = DOES_ALL;
    private int iw;
	private int ih;
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
        return flags;
    }

    
    @Override
	public void run(ImageProcessor ip) {
    	ImageShowRun(ip);
    }


	private void ImageShowRun(ImageProcessor ip) {
		ImageProcessor ipd = ip.convertToFloatProcessor();  
		BufferedImage src = ipd.getBufferedImage();
		iw = ip.getWidth();
		ih = ip.getHeight();
		
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
	


	
    //-----------main rotuine............
	public static void main(String[] args) {
		 Class<?> colass=ImageDualShowJ.class;
		 new ImageJ();
		 ImageJ.main(args);
		 ImagePlus srcij = IJ.openImage();
		 srcij.show();
		 IJ.runPlugIn(colass.getName(), null);	
		 srcij.setTitle("Enhanced Image");
		 srcij.updateAndDraw();	
		 
	}

}