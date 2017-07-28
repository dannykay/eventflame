package org.timeflame.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.timeflame.data.TimeFlameGraph;

public class FlameGraphChart {
	private BufferedImage bi ;
	private Graphics2D g ;
	private final Optional<TimeFlameGraph> timeFlameGraph;
	
	/** pixel height of the 2D graphic */
	double px_canvas_height=1000;
	/** pixel width of the 2D graphic */
	double px_canvas_width=2000;
	/** Width of the area to the left of the left y axis line where axis values and labels are written */
	double px_y_axis_label_width=50;
	/** pixel margin around whole 2D graphic where nothing is drawn */
	double px_canvas_margin=2;
	/** pixel width of graph image area */
	double px_x_data_width = px_canvas_width - 2*px_y_axis_label_width - 2 * px_canvas_margin;
	/** top margin for drawing title */
	double px_header_title_margin = 50;
	/** bottom margin for drawing title */
	double px_footer_title_margin = 50;
	/** pixel height of graph image area */
	double px_y_data_height = px_canvas_height - px_header_title_margin - px_footer_title_margin - 2 * px_canvas_margin;
	
	/** max y tic label */
	double y_max_v = 15;
	/** min y tic label */
	double y_min_v = -70;
	/** Total number of swim lanes that need to be drawn */
	double y_major_tick= Math.ceil((y_max_v-y_min_v)/25f);
	double n_pos_tick=Math.ceil(y_max_v/y_major_tick);
	double n_neg_tick=Math.ceil(Math.abs(y_min_v)/y_major_tick);
	double y_range = (n_pos_tick + n_neg_tick)*y_major_tick;
	double y_min_tick= (0f - n_neg_tick)*y_major_tick;
	double y_max_tick= n_pos_tick*y_major_tick;
	
	/** Range of epoch seconds that need to be drawn on the graph. Default is 1 day */
	double x_range_secs = 24*60*60;
	
	double px_per_sec = x_range_secs/px_x_data_width;
	double px_per_y = px_y_data_height/ y_range ;

	double px_y_zero =  px_header_title_margin + (y_max_tick*px_per_y);

	
	
	/** fattness of bar expressed as a proportion of data height */

	public FlameGraphChart(TimeFlameGraph timeFlameGraph) {
		super();
		this.timeFlameGraph=Optional.ofNullable(timeFlameGraph);
	}
	
	private void rightAlignedText(double x,double y,String text) {
		FontMetrics fm = g.getFontMetrics( g.getFont() );
		int width = fm.stringWidth(text);
		int px_x = (int)(x-width);
		int px_y =(int)y+g.getFontMetrics().getAscent()/2;
		g.drawString(text,px_x,px_y);
	}
	private void centeredText(double x,double y,String text) {
		FontMetrics fm = g.getFontMetrics( g.getFont() );
		int width = fm.stringWidth(text);
		int px_x = (int)(x-width/2);
		int px_y =(int)y+g.getFontMetrics().getAscent()/2;
		g.drawString(text,px_x,px_y);
	}
	
	public byte[] writePngFile() throws IOException {

		ByteArrayOutputStream baos=null;
		try {
			bi = new BufferedImage((int)px_canvas_width, (int)px_canvas_height, BufferedImage.TYPE_INT_ARGB);
			g = bi.createGraphics();
			g.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
			//g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
			
			g.setColor(Color.WHITE);
			g.fillRect(0,0,(int)px_canvas_width,(int)px_canvas_height);
			
			g.setFont(new Font("Arial", Font.PLAIN, 14));
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
	
			int max_x = (int)(px_y_axis_label_width+px_x_data_width);
			g.drawLine((int) px_y_axis_label_width, (int) px_header_title_margin , (int)(px_y_axis_label_width) , (int) (px_header_title_margin + px_y_data_height));
			
			float dash[] = {1f,3f};
			for (double tick=y_min_tick;tick<=y_max_tick;tick+=y_major_tick) {
				int y = (int) (px_y_zero - px_per_y*tick);
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(1f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
				g.drawLine((int)px_y_axis_label_width-4, y, (int) px_y_axis_label_width, y);
				rightAlignedText((int) (px_y_axis_label_width -6), y ,String.format("%d", (int)Math.abs(tick)));
				if (tick!=y_min_tick) {
					g.setColor(Color.GRAY);
					g.setStroke(new BasicStroke(0.5f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,1f,dash,1f));
				}
				g.drawLine((int)px_y_axis_label_width-4, y, max_x, y);
				
			}
			int y = (int) (px_y_zero - px_per_y*y_min_tick);
			for (double hr=0;hr<24;hr++) {
				for (double min=0;min<=45;min+=15) {
					int x = (int)(px_y_axis_label_width+(hr*3600f+min*60f)/px_per_sec);
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
					g.drawLine(x,y,x,(int)(y+7-(min%30)/4));
					if (min==0 && hr!=0) {
						centeredText(x,y+20,String.format("%02d:%02d", (int)hr,(int)min));
						g.setColor(Color.GRAY);
						g.setStroke(new BasicStroke(0.5f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,1f,dash,1f));
						g.drawLine(x,y,x,(int) (px_y_zero - px_per_y*y_max_tick));
					} 
				}
			}
			
			baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", baos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
}
