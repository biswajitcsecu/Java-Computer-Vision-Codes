import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import imagingbook.common.threshold.Thresholder;



public interface AdaptiveThresholder extends Thresholder {
	public ByteProcessor getThreshold(ByteProcessor bp);
	public default void threshold(ByteProcessor I, ByteProcessor Q) {
		final int w = I.getWidth();
		final int h = I.getHeight();
		final int minVal = 0;
		final int maxVal = 255;
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				int p = I.get(u, v);
				int q = Q.get(u, v);
				I.set(u, v, (p <= q) ? minVal : maxVal);
			}
		}
	}

	public default void threshold(ByteProcessor I, FloatProcessor Q) {
		final int w = I.getWidth();
		final int h = I.getHeight();
		final int minVal = 0;
		final int maxVal = 255;
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				int p = I.get(u, v);
				float q = Q.getf(u, v);
				I.set(u, v, (p <= q) ? minVal : maxVal);
			}
		}
	}
	
	@Override
	public default boolean threshold(ByteProcessor ip) {
		ByteProcessor Q = this.getThreshold(ip);
		if (Q != null) {
			this.threshold(ip, Q);
			return true;
		}
		else {
			return false;
		}
	}

}