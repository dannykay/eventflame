package org.timeflame.graphics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.timeflame.graphics.RGB.ValueOutOfRangeRuntimeException;

public class RGBTest {

	@Test
	public void white() {
		Exception ex=null;
		RGB rgb=null;
		try {
			rgb=new RGB(1,1,1);
		} catch (Exception e) {
			ex=e;
		}
		assertNull("No exception",ex);
		assertEquals("Integer value",0xFFFFFF,rgb.rgbInt());
		assertEquals("toString","RGB [r=1.00 g=1.00 b=1.00 0xFFFFFF ]",rgb.toString());
	}
	@Test
	public void whiteAlpha() {
		Exception ex=null;
		RGB rgb=null;
		try {
			rgb=new RGB(1,1,1,0.5F);
		} catch (Exception e) {
			ex=e;
		}
		assertNull("No exception",ex);
		assertEquals("Integer value",0xFFFFFF,rgb.rgbInt());
		assertEquals("toString","RGB [r=1.00 g=1.00 b=1.00 0xFFFFFF alpha=0.50]",rgb.toString());
	}
	@Test
	public void hexInOut() {
		Exception ex=null;
		RGB rgb=null;
		try {
			rgb=new RGB(0x020304);
		} catch (Exception e) {
			e.printStackTrace();
			ex=e;
		}
		assertNull("No exception",ex);
		assertEquals("toString","RGB [r=0.01 g=0.01 b=0.02 0x020304 ]",rgb.toString());
		assertEquals("Integer value",0x020304,rgb.rgbInt());
	}
	@Test
	public void hexInOutAlpha() {
		Exception ex=null;
		RGB rgb=null;
		try {
			rgb=new RGB(0x020304,0.7f);
		} catch (Exception e) {
			e.printStackTrace();
			ex=e;
		}
		assertNull("No exception",ex);
		assertEquals("toString","RGB [r=0.01 g=0.01 b=0.02 0x020304 alpha=0.70]",rgb.toString());
		assertEquals("Integer value",0x020304,rgb.rgbInt());
	}

	@Test
	public void interp() {
		Exception ex=null;
		RGB rgb=null;
		try {
			RGB rgb1=new RGB(0x020202,0.2f);
			RGB rgb2=new RGB(0x060606,0.6f);
			rgb = RGB.interp(rgb1, rgb2, 0.75f);
		} catch (Exception e) {
			e.printStackTrace();
			ex=e;
		}
		assertNull("No exception",ex);
		assertEquals("toString","RGB [r=0.02 g=0.02 b=0.02 0x050505 alpha=0.50]",rgb.toString());
		assertEquals("Integer value",0x050505,rgb.rgbInt());
	}
	@Test
	public void interpException() {
		Exception ex=null;
		String err=null;
		RGB rgb=null;
		try {
			RGB rgb1=new RGB(0x020202,0.2f);
			RGB rgb2=new RGB(0x060606,0.6f);
			rgb = RGB.interp(rgb1, rgb2, 75f);
		} catch (Exception e) {
			ex=e;
			err=e.getMessage();
		}
		assertNotNull("No exception",ex);
		assertEquals("Exception message","percent must be between 0.0 and 1.0",err);
	}
	@Test
	public void interpException2() {
		Exception ex=null;
		String err=null;
		RGB rgb=null;
		try {
			RGB rgb1=new RGB(0x020202,0.2f);
			RGB rgb2=new RGB(0x060606,0.6f);
			rgb = RGB.interp(rgb1, rgb2, -1f);
		} catch (Exception e) {
			ex=e;
			err=e.getMessage();
		}
		assertNotNull("No exception",ex);
		assertEquals("Exception message","percent must be between 0.0 and 1.0",err);
	}
	@Test
	public void ctorException() {
		Exception ex=null;
		String err=null;
		try {
			new RGB(2f,2f,2f,2f);
		} catch (Exception e) {
			ex=e;
			err=e.getMessage();
		}
		assertNotNull("No exception",ex);
		assertTrue(ex instanceof ValueOutOfRangeRuntimeException);
	}
	
}
