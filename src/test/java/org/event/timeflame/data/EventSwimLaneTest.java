package org.event.timeflame.data;

import static org.junit.Assert.*;

import org.junit.Test;
import org.timeflame.data.Event;
import org.timeflame.data.EventSwimLane;

public class EventSwimLaneTest {

	@Test
	public void testAdd() {
		Event [] events = {
			new Event(100L,200L)
		,	new Event(200L,300L)
		,	new Event(200L,300L)
		,	new Event(400L,500L)
		,	new Event(1L,2L)
		,	new Event(350L,380L)
		,	new Event(380L,400L)
		};
		boolean r;
		EventSwimLane esl = new EventSwimLane();
		r=esl.add(events[0]);
		assertEquals("Add first",1,esl.get().size());
		assertEquals("return check",true,r);
		r=esl.add(events[1]);
		assertEquals("Add second",2,esl.get().size());
		assertEquals("return check",true,r);
		r=esl.add(events[2]);
		assertEquals("List has 2 ele",2,esl.get().size());
		assertEquals("return check",false,r);
		r=esl.add(events[3]);
		assertEquals("List has 3 ele",3,esl.get().size());
		assertEquals("return check",true,r);
		r=esl.add(events[4]);
		assertEquals("Add to begining",4,esl.get().size());
		assertEquals("return check",true,r);
		r=esl.add(events[5]);
		assertEquals("List has 2 ele",5,esl.get().size());
		assertEquals("return check",true,r);
		r=esl.add(events[6]);
		assertEquals("return check",true,r);
		assertEquals("List has 2 ele",6,esl.get().size());
		assertEquals("return check",true,r);
		assertEquals("Ordering is correct","EventSwimLane [[Event [1,2], Event [100,200], Event [200,300], Event [350,380], Event [380,400], Event [400,500]]]",esl.toString());
		assertEquals("Check first",events[4],esl.getFirst());
		assertEquals("Check last",events[3],esl.getLast());
		
	}
	
	@Test
	public void beforeFirstTest() {
		EventSwimLane esl=new EventSwimLane();
		esl.add(new Event(2L,3L));
		esl.add(new Event(1L,2L));
		assertEquals("first",1L,esl.get().get(0).getBeginEpochMs());
	}
}
