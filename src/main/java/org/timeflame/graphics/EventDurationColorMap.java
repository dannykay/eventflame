package org.timeflame.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** Returns a color for event duration
 * 
 * @author dkay
 *
 */
public class EventDurationColorMap {
	
	private static class Threshold {
		
		public final double upperLimitSecs;
		public final RGB rgb;
		public final String label;
		private Threshold(double upperLimitSecs, int rgb, String label) {
			super();
			this.upperLimitSecs = upperLimitSecs;
			this.rgb = new RGB(rgb);
			this.label = label;
		}
		static Threshold parse(String s) {
			String[] tok = s.replaceFirst("^\\s+", "").split("\\s+", -3);
			String label = s.replaceFirst("^\\s+", "").replaceFirst("[^\\s]+\\s+", "").replaceFirst("[^\\s]+\\s*", "");
			double secMultiplier = 0F;
			switch (tok[0].replaceAll("[0-9]", "")) {
			case "s" : secMultiplier=1F; break;
			case "m" : secMultiplier=60F; break;
			case "h" : case "hr" : secMultiplier=3600F; break;
			case "d" : secMultiplier=3600F*24F; break;
			}
			double units=Float.parseFloat(tok[0].replaceAll("[a-zA-Z]", ""));
			int rgb=tok.length>1?Integer.decode("0x"+tok[1]):0;
			if (label.length()==0&&tok.length>0) {
				label=tok[0];
			}
			return new Threshold(units*secMultiplier,rgb,label);
		}
		public final double getUpperLimitSecs() {
			return upperLimitSecs;
		}
		public final RGB getRgb() {
			return rgb;
		}
		public final String getLabel() {
			return label;
		}
		
	}
	private final List<Threshold>thresholds;
	private RGB defRgb = RGB.BLACK;
	public final static String DEFAULT_COLORMAP 
		= ""
		+ "1s 	000055\n"
		+ "5s 	000088\n"
		+ "15s	0000AA\n"
		+ "30s	0055AA Half Hour\n"
		+ "1m 	00AA88\n"
		+ "5m 	00AA88\n"
		+ "15m 	00AA88\n"
		+ "30m 	00AA88\n"
		+ "1hr 	00AA88\n"
		+ "2hr 	00AA88 > 2 hours\n"
		;
	public EventDurationColorMap(String config) {
		thresholds= Arrays.asList(config.split("\n")).stream()
				.map(Threshold::parse)
				.filter(t->t.upperLimitSecs>0F)
				.sorted(Comparator.comparing(Threshold::getUpperLimitSecs))
				.collect(Collectors.toList());
		if (thresholds.size()>0) {
			defRgb=thresholds.get(thresholds.size()-1).getRgb();
		}
	}

	public RGB decode(double secs) {
		return decode(secs,false);
	}
	public RGB decode(double secs,boolean intep) {
		for (int i=0;i<thresholds.size();i++) {
			if (secs<thresholds.get(i).getUpperLimitSecs()||i==thresholds.size()-1) {
				Threshold lo=thresholds.get(Math.max(i-i, 0));
				Threshold hi=thresholds.get(Math.max(i, 0));
				double range=hi.getUpperLimitSecs()-lo.getUpperLimitSecs();
				double percent=(intep&&range>0)?(secs-lo.getUpperLimitSecs())/range : 0.0F;
				return RGB.interp(lo.getRgb(),hi.getRgb(),percent);
			}
		}
		return defRgb;
	}
}
