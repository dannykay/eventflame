package org.timeflame.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.timeflame.graphics.EventDurationColorMap.Threshold;

public class EventDurationColorMapTest {

	String config 
	= ""
	+ "1s 	000055\n"
	+ "5s 	000088\n"
	+ "15s	0000AA\n"
	+ "30s	0055AA\n"
	+ "1m 	00AA88\n"
	+ "5m 	00AA88\n"
	+ "15m 	00AA88\n"
	+ "30m 	00AA88 Half Hour\n"
	+ "1hr 	00AA88\n"
	+ "2h 	00AA88 > 2 hours\n"
	+ "1d 	00AA88 > 1 day\n"
	;
	@Test
	public void testEventDurationColorMap() {
		Exception ex=null;
		String err=null;
		EventDurationColorMap ecm=null;
		try {
			ecm = new EventDurationColorMap(config);
			RGB colorInt = ecm.decode(10f, true);
			RGB colorNon = ecm.decode(10f, false);
			
		} catch (Exception e) {
			e.printStackTrace();
			ex=e;
			err=e.getMessage();
		}
		assertNull("No exception",ex);
		
		RGB def = ecm.decode(1000000f, false);
		assertEquals("RGB [r=0.00 g=0.67 b=0.53 0x00AA88 ]",def.toString());
		def = ecm.decode(1000000f, true);
		System.out.println(def);
		assertEquals("RGB [r=0.00 g=0.67 b=0.53 0x00AA88 ]",def.toString());
		def = ecm.decode(1000000f);
		System.out.println(def);
		assertEquals("RGB [r=0.00 g=0.67 b=0.53 0x00AA88 ]",def.toString());
	}

	@Test
	public void nomapTest() {
		Exception ex=null;
		String err=null;
		EventDurationColorMap ecm=null;
		RGB rgb=null;
		try {
			ecm = new EventDurationColorMap("");
			rgb = ecm.decode(10f, true);
			
		} catch (Exception e) {
			e.printStackTrace();
			ex=e;
			err=e.getMessage();
		}
		assertNull("No exception",ex);
		assertEquals("Defult to black with no map","RGB [r=0.00 g=0.00 b=0.00 0x000000 ]",rgb.toString());
	}

	@Test
	public void getThresholdsList() {
		EventDurationColorMap ecm = new EventDurationColorMap(config);
		List<Threshold> thod=ecm.getThresholds();
		assertEquals("All parsed entries exist",11,thod.size());
		assertEquals("> 1 day",thod.get(thod.size()-1).getLabel());
	}
}
