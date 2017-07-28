package org.timeflame.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.timeflame.graphics.FlameGraphChart;


public class ImageServerHandler extends AbstractHandler {
	Logger logger = LoggerFactory.getLogger(ImageServerHandler.class);
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		if (target.matches(".*png.*")) {
			FlameGraphChart fgc = new FlameGraphChart(null);
			byte[] png = fgc.writePngFile();
			response.setContentType("image/png");
			response.setContentLength(png.length);
			response.getOutputStream().write(png);
	        response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
	        PrintWriter out = response.getWriter();
	        out.println("<h1>Hello from jetty server</h1>");
		}
        baseRequest.setHandled(true);
    }

	public static void main(String[]s ) {
		int port=8080;
		if (s.length>0) {
			port=Integer.parseInt(s[0]);
		}
		Server server = new Server(port);
		try {
			server.setHandler(new ImageServerHandler());
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
