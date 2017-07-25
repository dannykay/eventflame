package org.timeflame.graphics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.timeflame.graphics.RGB.ValueOutOfRangeRuntimeException;

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
	+ "2hr 	00AA88 > 2 hours\n"
	+ "1d 	00AA88 > 1 day\n"
	;
	@Test
	public void testEventDurationColorMap() {
		Exception ex=null;
		String err=null;
		EventDurationColorMap ecm;
		try {
			ecm = new EventDurationColorMap(config);
			RGB colorInt = ecm.decode(10f, true);
			RGB colorNon = ecm.decode(10f, false);
			System.out.println(colorInt);
			System.out.println(colorNon);
			
		} catch (Exception e) {
			e.printStackTrace();
			ex=e;
			err=e.getMessage();
		}
		assertNull("No exception",ex);

		
	}

	@Test
	public void testDecodeDouble() {
	}

	@Test
	public void testDecodeDoubleBoolean() {
	}

}
