import ij.plugin.filter.RankFilters;
import ij.process.ByteProcessor;


public class BernsenThresholder implements AdaptiveThresholder {
	private int radius = 5;
	private int cmin = 5;
	private BackgroundMode bgMode = BackgroundMode.DARK;
	
	// default constructor:
	public BernsenThresholder() {
		
	}
	// speciﬁc constructor:
	public BernsenThresholder(int radius, int cmin,BackgroundMode bgMode) {
		this.radius = radius;
		this.cmin = cmin;
		this.bgMode = bgMode;
		}
	
	public ByteProcessor getThreshold(ByteProcessor I) {
		int width = I.getWidth();
		int height = I.getHeight();
		ByteProcessor Imin = (ByteProcessor) I.duplicate();
		ByteProcessor Imax = (ByteProcessor) I.duplicate();
		RankFilters rf = new RankFilters();
		
		rf.rank(Imin, radius, RankFilters.MIN); 
		rf.rank(Imax, radius, RankFilters.MAX); 
		int q = (bgMode == BackgroundMode.DARK) ? 256 : 0;
		ByteProcessor Q = new ByteProcessor(width, height);
		
		for (int v = 0; v < height; v++) {
			for (int u = 0; u < width; u++) {
				int gMin = Imin.get(u, v);
				int gMax = Imax.get(u, v);
				int c = gMax - gMin;
				if (c >= cmin)
					Q.set(u, v, (gMin + gMax) / 2);
				else
					Q.set(u, v, q);
				}
			}
		return Q; // Q(u, v)
		}

	}



