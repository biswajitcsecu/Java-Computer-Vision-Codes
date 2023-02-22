import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.color.ColorSpace;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import imagingbook.common.color.colorspace.LabColorSpace;
import imagingbook.common.color.colorspace.LinearRgb65ColorSpace;
import imagingbook.common.color.colorspace.LuvColorSpace;
import imagingbook.common.filter.generic.GenericFilter;
import imagingbook.common.filter.linear.GaussianFilterSeparable;
import imagingbook.common.ij.DialogUtils;
import imagingbook.common.image.ColorPack;
import static imagingbook.common.ij.IjUtils.noCurrentImage;


public class BrightenRGB implements PlugInFilter {
	
	private ImagePlus imp = null;
	private enum ColorStackType {
		Lab, Luv, LinearRGB, sRGB;
	}

	private static double Sigma = 3.0;
	private static int Iterations = 1;
	private static ColorStackType CsType = ColorStackType.Lab;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		// this plugin works on RGB images
		return DOES_RGB; 
		}
	
	public void run(ImageProcessor ip) {
		int[] pixels = (int[]) ip.getPixels();
		for (int i = 0; i < pixels.length; i++) {
			int c = pixels[i];
			
			// split color pixel into rgb-components:
			int r = (c & 0xff0000) >> 16;
			int g = (c & 0x00ff00) >> 8;
			int b = (c & 0x0000ff);
			
			// modify colors:
			r = r + 20; if (r > 255) r = 255;
			g = g + 60; if (g > 255) g = 255;
			b = b + 50; if (b > 255) b = 255;
			// reassemble color pixel and insert into pixel array:
			pixels[i]= ((r & 0xff)<<16) | ((g & 0xff)<<8) | b & 0xff;
			}
		//ImageShowRun(ip);
		
		ColorProcessor cp = ip.convertToColorProcessor();
		String title = imp.getShortTitle() + "-filtered-" + CsType.name();
		ImagePlus result = new ImagePlus(title, cp);
		result.show();

		}
	
	
	public void ImageShowRun(ImageProcessor ip) {
		if (!runDialog())
			return;

		ColorPack colStack = new ColorPack((ColorProcessor) ip);
		ColorSpace cs = null;
		switch (CsType) {
			case Lab : 		cs = LabColorSpace.getInstance(); break;
			case Luv: 		cs = LuvColorSpace.getInstance(); break;
			case LinearRGB: cs = LinearRgb65ColorSpace.getInstance(); break;
			case sRGB: 		cs = null; break;
		}

		if (cs != null) {
			colStack.convertFromSrgbTo(cs);
		}

		FloatProcessor[] processors = colStack.getProcessors();
		GenericFilter filter = new GaussianFilterSeparable(Sigma); // non-separable: GaussianFilter(sigma)

		for (int k = 0; k < Iterations; k++) {
			for (FloatProcessor fp : processors) {
				filter.applyTo(fp);
			}
		}
		if (!colStack.getColorspace().isCS_sRGB()) {
			colStack.convertToSrgb();    // convert back to sRGB
		}
		ColorProcessor cp = colStack.toColorProcessor();
		String title = imp.getShortTitle() + "-filtered-" + CsType.name();
		ImagePlus result = new ImagePlus(title, cp);
		result.show();

	}
		
	
	
	boolean runDialog() {
		GenericDialog gd = new GenericDialog(this.getClass().getSimpleName());
		gd.addEnumChoice("Color space", CsType);
		gd.addNumericField("sigma", Sigma, 1);
		gd.addNumericField("iterations", Iterations, 0);

		gd.showDialog();
		if(gd.wasCanceled())
			return false;

		CsType = gd.getNextEnumChoice(ColorStackType.class);
		Sigma = gd.getNextNumber();
		Iterations = (int)gd.getNextNumber();
		if (Iterations < 1) Iterations = 1;
		return true;
	}




	//-----------main rotuine............
	public static void main(String[] args) {
		Class<?> colass=BrightenRGB.class;
		new ImageJ();
		ImageJ.main(args);
		ImagePlus srcij = IJ.openImage();
		srcij.show();
		IJ.runPlugIn(colass.getName(), null);	
		srcij.setTitle("Source Image");
		srcij.updateAndDraw();	
		 
	}
}
