package org.event.timeflame.data;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.timeflame.data.Event;
import org.timeflame.data.Event.EventBeginEndSequenceException;

public class EventTest {

	@Test
	public void testEventLongLong() {
		Event e = new Event(1L,2L);
		assertEquals("begin",1L,e.getBeginEpochMs());
		assertEquals("end",2L,e.getEndEpochMs());
		Exception ex=null;
		try {
			Event e2 = new Event(3L,2L);
		} catch (EventBeginEndSequenceException e1) {
			ex=e1;
		}
		assertTrue("begin before end",ex instanceof EventBeginEndSequenceException );
	}

	@Test
	public void testEventLongFloat() {
		Event e = new Event(1L,0.5F);
		assertEquals("begin",1L,e.getBeginEpochMs());
		assertEquals("end",501L,e.getEndEpochMs());
	}

	@Test
	public void testToString() {
		Event e = new Event(1L,2L);
		assertEquals("string","Event [1,2]",e.toString());
	}


	@Test
	public void testBeginsAfter() {
		Event e1 = new Event(1L,2L);
		Event e2 = new Event(2L,3L);
		Event e3 = new Event(3L,4L);
		assertTrue("is after",e2.beginsAfter(e1));
		assertFalse("null param check",e2.beginsAfter(null));
		assertFalse("false check same",e2.beginsAfter(e2));
		assertFalse("false check not same",e2.beginsAfter(e3));
	}

	@Test
	public void testBeginsBefore() {
		Event e1 = new Event(1L,2L);
		Event e2 = new Event(2L,3L);
		Event e3 = new Event(3L,4L);
		assertTrue("is before",e1.beginsBefore(e2));
		assertFalse("null param check",e2.beginsBefore(null));
		assertFalse("false check same",e2.beginsBefore(e2));
		assertFalse("false check not same",e3.beginsBefore(e2));
	}

	@Test
	public void testEndsAfter() {
		Event e1 = new Event(1L,2L);
		Event e2 = new Event(2L,3L);
		Event e3 = new Event(3L,4L);
		assertTrue("true",e2.endsAfter(e1));
		assertFalse("null param check",e2.endsAfter(null));
		assertFalse("false check same",e2.endsAfter(e2));
		assertFalse("false check not same",e1.endsAfter(e2));
	}

	@Test
	public void testEndsBefore() {
		Event e1 = new Event(1L,2L);
		Event e2 = new Event(2L,3L);
		Event e3 = new Event(3L,4L);
		assertTrue("true",e1.endsBefore(e2));
		assertFalse("null param check",e2.endsBefore(null));
		assertFalse("false check same",e2.endsBefore(e2));
		assertFalse("false check not same",e3.endsBefore(e2));
	}

	@Test
	public void testCompareOnBegins() {
		Event arr[] = {new Event(4L,10L),new Event(2L,30L),new Event(3L,20L),new Event(1L,40L)};
		List<Event> list=Arrays.asList(arr);
		list.sort((Event a,Event b)->a.compareOnBegins(b));
		assertEquals("lowest first",1L,list.get(0).getBeginEpochMs());
		assertEquals("highest last",4L,list.get(3).getBeginEpochMs());
	}

	@Test
	public void testCompareOnEnds() {
		Event arr[] = {new Event(4L,10L),new Event(2L,30L),new Event(3L,20L),new Event(1L,40L)};
		List<Event> list=Arrays.asList(arr);
		list.sort((Event a,Event b)->a.compareOnEnds(b));
		assertEquals("lowest first",10L,list.get(0).getEndEpochMs());
		assertEquals("highest last",40L,list.get(3).getEndEpochMs());
	}
	
	@Test
	public void testHashCode() {
		Event e1 = new Event(0xAAAAAAAAL,0xAAAAAAABL);
		Event e2 = new Event(0xAAAAAAAAL,0xAAAAAAACL);
		
		assertNotEquals(e1.hashCode(),e2.hashCode());
	}
	
	@Test
	public void testEquals() {
		Event e1 = new Event(1L,2L);
		assertTrue("same object",e1.equals(e1));
		assertFalse("null",e1.equals(null));
		assertFalse("different class",e1.equals(""));
		assertFalse("different end",e1.equals(new Event(1L,3L)));
		assertFalse("different begin",e1.equals(new Event(2L,2L)));
		assertTrue("different begin",e1.equals(new Event(1L,2L)));
	}
}
