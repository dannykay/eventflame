package org.timeflame.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class FlameGraphChart {
	/** pixel height of the 2D graphic */
	double px_canvas_height=1000;
	/** pixel width of the 2D graphic */
	double px_canvas_width=2000;
	/** Width of the area to the left of the left y axis line where axis values and labels are written */
	double px_y_axis_label_width=20;
	/** pixel margin around whole 2D graphic where nothing is drawn */
	double px_canvas_margin=2;
	/** pixel width of graph image area */
	double px_x_data_width = px_canvas_width - px_y_axis_label_width - 2 * px_canvas_margin;
	/** top margin for drawing title */
	double px_header_title_margin = 5;
	/** bottom margin for drawing title */
	double px_footer_title_margin = 5;
	/** pixel height of graph image area */
	double px_y_data_height = px_canvas_height - px_header_title_margin - px_footer_title_margin - 2 * px_canvas_margin;
	
	/** max y tic label */
	double y_max = 15;
	/** min y tic label */
	double y_min = -60;
	/** Total number of swim lanes that need to be drawn */
	double y_range = 0 - y_min + y_max;
	
	/** Range of epoch seconds that need to be drawn on the graph. Default is 1 day */
	double x_range_secs = 24*60*60;
	
	double px_per_sec = x_range_secs/px_x_data_width;
	double px_per_y = y_range / px_y_data_height;
	
	/** fattness of bar expressed as a proportion of data height */
	double bar_pct_height=0.9;

	public FlameGraphChart() {
		super();
	}
	
	public byte[] writePngFile() throws IOException {

		BufferedImage bi = new BufferedImage((int)px_canvas_width, (int)px_canvas_height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setColor(Color.WHITE);
		g.drawRect(0,0,(int)px_canvas_width,(int)px_canvas_height);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g.drawLine(this.px_y_axis_label_width, this., x2, y2);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		return baos.toByteArray();
	}
	
}
