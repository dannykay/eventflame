package org.event.timeflame.data;

import static org.junit.Assert.*;

import org.junit.Test;
import org.timeflame.data.Event;
import org.timeflame.data.TimeFlameGraph;
import org.timeflame.data.TimeFlameGraph.AddOutOfSequenceException;

public class TimeFlameGraphTest {

	Event [] events = {
			new Event(100L,200L)
		,	new Event(200L,300L)
		,	new Event(200L,300L)
		,	new Event(200L,500L)
		,	new Event(350L,380L)
		,	new Event(380L,400L)
		};
	@Test
	public void test() {
		TimeFlameGraph tfg = new TimeFlameGraph();
		
		for (Event e:events) {
			tfg.add(e);
		}
		assertEquals("size check",events.length,tfg.size());
		assertEquals("height check",3,tfg.height());
		assertSame("Get first",events[0],tfg.getEventOfFirstBegin());
		assertSame("Get last",events[3],tfg.getEventOfLastEnd());
		
	}
	
	@Test
	public void outOfSequenceTest() {
		TimeFlameGraph tfg = new TimeFlameGraph();
		Exception e=null;
		
		tfg.add(new Event(200L,300L));
		try {
			tfg.add(new Event(199L,300L));
		} catch (AddOutOfSequenceException e1) {
			e=e1;
		}
		assertTrue("Seqeunce exception check",e instanceof AddOutOfSequenceException);
	}
	@Test
	public void outOfSequenceIgnoreTest() {
		TimeFlameGraph tfg = new TimeFlameGraph(false);
		Exception e=null;
		
		tfg.add(new Event(200L,300L));
		try {
			tfg.add(new Event(199L,300L));
		} catch (AddOutOfSequenceException e1) {
			e=e1;
		}
		assertNull("No Seqeunce exception check",e);
	}

}
