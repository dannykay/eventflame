package org.timeflame.graphics;

public class RGB {
	private final float r;
	private final float g;
	private final float b;
	private final float alpha;
	public final static RGB BLACK = new RGB(0);
	
	public class ValueOutOfRangeRuntimeException extends RuntimeException
	{
		ValueOutOfRangeRuntimeException(float v) {
			super(String.format("%1.6f",v));
		}
	}
	private float check(float v) {
		if (!(v>=0&&v<=1F))
			throw new ValueOutOfRangeRuntimeException(v);
		return v;
	}
	

	public RGB(double r, double g, double b, double alpha) {
		super();
		this.r = check((float)r);
		this.g = check((float)g);
		this.b = check((float)b);
		this.alpha = check((float)alpha);
	}
	public RGB(float r, float g, float b) {
		this(r,g,b,1F);
	}
	public RGB(int rgb) {
		this(
			((float)((0x00FF0000&rgb)>>>16))/255F,
			((float)((0x0000FF00&rgb)>>>8))/255F,
			((float)((0x000000FF&rgb)>>>0))/255F,
			1.0f);
	}
	public RGB(int rgb, float alpha) {
		this(
				((float)((0x00FF0000&rgb)>>>16))/255F,
				((float)((0x0000FF00&rgb)>>>8))/255F,
				((float)((0x000000FF&rgb)>>>0))/255F,
			alpha);
	}
	public int rgbInt()
	{
		int v=0;
		v |= (int)(r*0xFF)<<16;
		v |= (int)(g*0xFF)<<8;
		v |= (int)(b*0xFF);
		return v;
	}
	public static RGB interp(RGB a,RGB b,double percent) {
		if (percent<0||percent>1) {
			throw new RuntimeException("percent must be between 0.0 and 1.0: "+percent);
		}
		return new RGB(
				a.r*(1.0F-percent)+b.r*percent,
				a.g*(1.0F-percent)+b.g*percent,
				a.b*(1.0F-percent)+b.b*percent,
				a.alpha*(1.0F-percent)+b.alpha*percent
			);
	}
	@Override
	public String toString() {
		String res=String.format("RGB [r=%1.2f g=%1.2f b=%1.2f 0x%02X%02X%02X ]",r,g,b,(int)(r*255),(int)(g*255),(int)(b*255));
		if (alpha!=1F) {
			res=String.format("RGB [r=%1.2f g=%1.2f b=%1.2f 0x%02X%02X%02X alpha=%1.2f]",r,g,b,(int)(r*255),(int)(g*255),(int)(b*255),alpha);
		}
		return res;
	}
	
}
