import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;





public class KMeansCluster implements PlugInFilter {
	boolean loopAgain, valueChanged;
	int[] pixels; 
	int[] newPixels;
	int[] clusters;
	int[] meanValues;
	int iters, pixelCount;
	
	public int setup(String arg, ImagePlus ip)
	{
		return DOES_RGB;
	}
	
	public void run(ImageProcessor orig)
	{
		int height = orig.getHeight();
		int width = orig.getWidth();
		pixelCount = orig.getPixelCount();
		pixels = (int[])orig.getPixels();
		System.out.println("Pixel array created. Length = " + pixels.length);
		
		int numClusters = (int)IJ.getNumber("Number of clusters: ",3);

		meanValues = new int[numClusters];
		System.out.println("Mean array created. Length = " + meanValues.length);
		
		for(int i = 0; i < numClusters; i++)
		{
			meanValues[i] = orig.get((int)(Math.random() * pixels.length));
		}
		
		clusters = new int[pixelCount];
		
		loopAgain = true;
		while(loopAgain)
		{
			iterate();
			updateMeans();
			iters++;
			if(valueChanged)
				loopAgain = true;
			else
			{
				loopAgain = false;
				System.out.println("no change");
			}
		}
		
		newPixels = new int[pixelCount];
		for(int i = 0; i < pixelCount; i++)
		{
			newPixels[i] = meanValues[clusters[i]];
		}
		
		ColorProcessor ip = new ColorProcessor(width, height, newPixels);
		ImagePlus im = new ImagePlus("Segmented Image", ip);
		im.show();
		System.out.println(iters);
		
		
	}
	
	private void iterate()
	{
		valueChanged = false;
		int currentDistance, distance;
		for(int i = 0; i < pixelCount; i++)
		{
			currentDistance = getDistance(pixels[i], meanValues[clusters[i]]);
			for(int j = 0; j < meanValues.length; j++)
			{
				distance = getDistance(pixels[i], meanValues[j]);
				if((distance < currentDistance))
				{
					currentDistance = distance;
					clusters[i] = j;
					valueChanged = true;
				}
			}
		}
	}
			
	private int getDistance(int pixelVal, int meanVal)
	{
		int pRed = (pixelVal & 0xff0000) >> 16;
		int pGreen = (pixelVal & 0x00ff00) >> 8;
		int pBlue = (pixelVal & 0x0000ff);
		
		int mRed = (meanVal & 0xff0000) >> 16;
		int mGreen = (meanVal & 0x00ff00) >> 8;
		int mBlue = (meanVal & 0x0000ff);
		
		int dist = (int)(Math.pow(pRed - mRed, 2) + Math.pow(pGreen - mGreen, 2) + Math.pow(pBlue - mBlue, 2));
		return dist;
	}
	
	private void updateMeans()
	{
		int count, r, g, b;
		
		for(int i = 0; i < meanValues.length; i++)
		{
			count = 0;
			r = 0;
			g = 0;
			b = 0;
			for(int j = 0; j < clusters.length; j++)
			{
				if(clusters[j] == i)
				{
					count++;
					
					r += (pixels[j] & 0xff0000) >> 16;
					g += (pixels[j] & 0x00ff00) >> 8;
					b += (pixels[j] & 0x0000ff);
				}
			}
			if(count != 0)
			{
				r /= count;
				g /= count;
				b /= count;
				meanValues[i] = ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
			System.out.println(count + " pixels belong to cluster " + i);
			System.out.println("mean value: " + meanValues[i]);
		}
	}
	
	    public static void main(String args[]) {
		Class<?>clavar = KMeansCluster.class;
		new ImageJ();
		ImagePlus imgj = IJ.openImage();
		imgj.show();
		IJ.runPlugIn(clavar.getName(), " ");
		imgj.updateAndDraw();
		
	}
	
}