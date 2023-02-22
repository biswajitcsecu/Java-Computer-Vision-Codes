import boofcv.abst.filter.blur.BlurFilter;
import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.filter.derivative.DerivativeType;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.alg.filter.derivative.GradientSobel;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.core.image.border.FactoryImageBorderAlgs;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.factory.filter.derivative.FactoryDerivative;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.border.BorderType;
import boofcv.struct.image.*;

import java.awt.image.BufferedImage;



@SuppressWarnings({"unchecked"})
public class ImageFilterBoof {

	private static final int blurRadius = 10;

	private static final ListDisplayPanel panel = new ListDisplayPanel();

	public static void procedural( GrayU8 input ) {
		var blurred = new GrayU8(input.width, input.height);
		var derivX = new GrayS16(input.width, input.height);
		var derivY = new GrayS16(input.width, input.height);

		// Gaussian blur: Convolve a Gaussian kernel
		BlurImageOps.gaussian(input, blurred, -1, blurRadius, null);

		// Calculate image's derivative
		GradientSobel.process(blurred, derivX, derivY, FactoryImageBorderAlgs.extend(input));

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeGradient(derivX, derivY, -1, null);
		panel.addImage(outputImage, "Procedural Fixed Type");
	}

	
	
	
	
	public static <T extends ImageGray<T>, D extends ImageGray<D>>
	void generalized( T input ) {
		Class<T> inputType = (Class<T>)input.getClass();
		Class<D> derivType = GImageDerivativeOps.getDerivativeType(inputType);

		T blurred = input.createSameShape();
		D derivX = GeneralizedImageOps.createSingleBand(derivType, input.width, input.height);
		D derivY = GeneralizedImageOps.createSingleBand(derivType, input.width, input.height);

		// Gaussian blur: Convolve a Gaussian kernel
		GBlurImageOps.gaussian(input, blurred, -1, blurRadius, null);

		// Calculate image's derivative
		GImageDerivativeOps.gradient(DerivativeType.SOBEL, blurred, derivX, derivY, BorderType.EXTENDED);

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeGradient(derivX, derivY, -1, null);
		panel.addImage(outputImage, "Generalized " + inputType.getSimpleName());
	}
	
	
	
	
	

	public static <T extends ImageGray<T>, D extends ImageGray<D>>
	void filter( T input ) {
		Class<T> inputType = (Class<T>)input.getClass();
		Class<D> derivType = GImageDerivativeOps.getDerivativeType(inputType);

		T blurred = input.createSameShape();
		D derivX = GeneralizedImageOps.createSingleBand(derivType, input.width, input.height);
		D derivY = GeneralizedImageOps.createSingleBand(derivType, input.width, input.height);

		// declare image filters
		BlurFilter<T> filterBlur = FactoryBlurFilter.gaussian(ImageType.single(inputType), -1, blurRadius);
		ImageGradient<T, D> gradient = FactoryDerivative.sobel(inputType, derivType);

		// process the image
		filterBlur.process(input, blurred);
		gradient.process(blurred, derivX, derivY);

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeGradient(derivX, derivY, -1, null);
		panel.addImage(outputImage, "Filter " + inputType.getSimpleName());
	}

	
	
	
	
	
	public static void nogenerics( ImageGray input ) {
		Class inputType = input.getClass();
		Class derivType = GImageDerivativeOps.getDerivativeType(inputType);

		ImageGray blurred = (ImageGray)input.createSameShape();
		ImageGray derivX = GeneralizedImageOps.createSingleBand(derivType, input.width, input.height);
		ImageGray derivY = GeneralizedImageOps.createSingleBand(derivType, input.width, input.height);

		// Gaussian blur: Convolve a Gaussian kernel
		GBlurImageOps.gaussian(input, blurred, -1, blurRadius, null);

		// Calculate image's derivative
		GImageDerivativeOps.gradient(DerivativeType.SOBEL, blurred, derivX, derivY, BorderType.EXTENDED);

		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeGradient(derivX, derivY, -1, null);
		panel.addImage(outputImage, "Generalized " + inputType.getSimpleName());
	}

	
	
	
	
	
	
	public static void main( String[] args ) {
		String file= "/home/picox/Projects/Eclipse/ImageFilterBfcv/src/corn.jpg";
		BufferedImage image = UtilImageIO.loadImageNotNull(UtilIO.path(file));

		// produces the same results
		procedural(ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class));
		generalized(ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class));
		filter(ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class));
		nogenerics(ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class));

		// try another image data type
		generalized(ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class));

		ShowImages.showWindow(panel, "Image Filter Examples", true);
	}
}








