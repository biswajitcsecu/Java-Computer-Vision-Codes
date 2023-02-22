import java.awt.image.BufferedImage;
import org.ddogleg.struct.DogArray_I32;
import boofcv.abst.segmentation.ImageSuperpixels;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.segmentation.ComputeRegionMeanColor;
import boofcv.alg.segmentation.ImageSegmentationOps;
import boofcv.factory.segmentation.ConfigFh04;
import boofcv.factory.segmentation.FactoryImageSegmentation;
import boofcv.factory.segmentation.FactorySegmentationAlg;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.feature.VisualizeRegions;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.ColorQueue_F32;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayS32;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.Planar;





public class SegmentSuperpixels {
	
	public static <T extends ImageBase<T>>
	void performSegmentation( ImageSuperpixels<T> alg, T color ) {
		// Segmentation -----------------------
		GBlurImageOps.gaussian(color, color, 0.5, -1, null);

		// Storage for segmented image-----------
		var pixelToSegment = new GrayS32(color.width, color.height);

		// Segmentation-----------------
		alg.segment(color, pixelToSegment);

		// Displays the results--------------
		visualize(pixelToSegment, color, alg.getTotalSuperpixels());
	}



	public static <T extends ImageBase<T>>
	
	void visualize( GrayS32 pixelToRegion, T color, int numSegments ) {
		// Computes the mean color inside each region
		ImageType<T> type = color.getImageType();
		ComputeRegionMeanColor<T> colorize = FactorySegmentationAlg.regionMeanColor(type);

		var segmentColor = new ColorQueue_F32(type.getNumBands());
		segmentColor.resize(numSegments);

		var regionMemberCount = new DogArray_I32();
		regionMemberCount.resize(numSegments);

		ImageSegmentationOps.countRegionPixels(pixelToRegion, numSegments, regionMemberCount.data);
		colorize.process(color, pixelToRegion, regionMemberCount, segmentColor);

		// Draw each region
		BufferedImage outColor = VisualizeRegions.regionsColor(pixelToRegion, segmentColor, null);
		// Draw each region by a random color
		BufferedImage outSegments = VisualizeRegions.regions(pixelToRegion, numSegments, null);

		// Make region edges
		var outBorder = new BufferedImage(color.width, color.height, BufferedImage.TYPE_INT_RGB);
		ConvertBufferedImage.convertTo(color, outBorder, true);
		VisualizeRegions.regionBorders(pixelToRegion, 0xFF0000, outBorder);

		// Show the results
		var gui = new ListDisplayPanel();
		gui.addImage(outColor, "Color of Segments");
		gui.addImage(outBorder, "Region Borders");
		gui.addImage(outSegments, "Regions");
		ShowImages.showWindow(gui, "Superpixels", true);
	}

	
	
	@SuppressWarnings("unchecked")
	public static void main( String[] args ) {
		String file ="/home/picox/Projects/Eclipse/SegmentSuperpixels/src/src6.jpg";
		BufferedImage image = UtilImageIO.loadImageNotNull(UtilIO.path(file));
		image = ConvertBufferedImage.stripAlphaChannel(image);

		// Select input image type
		ImageType<Planar<GrayF32>> imageType = ImageType.pl(3, GrayF32.class);

		@SuppressWarnings("rawtypes")
		ImageSuperpixels alg = FactoryImageSegmentation.fh04(new ConfigFh04(100, 30), imageType);

		// Convert image into BoofCV format
		@SuppressWarnings("rawtypes")
		ImageBase color = imageType.createImage(image.getWidth(), image.getHeight());
		ConvertBufferedImage.convertFrom(image, color, true);

		// Segment and display results
		performSegmentation(alg, color);
	}
	

}
