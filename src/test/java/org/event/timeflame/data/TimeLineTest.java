package org.event.timeflame.data;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;
import org.timeflame.data.TimeLine;

public class TimeLineTest {

	@Test
	public void inSameDayTest() {
		Instant i0 = Instant.parse("1990-05-17T00:10:00Z");
		Instant i1 = Instant.parse("1990-05-17T22:04:00Z");
		TimeLine tl=new TimeLine(i0.getEpochSecond()*1000L,i1.getEpochSecond()*1000L,ZoneId.of("Z"));
		assertEquals("wholeHourCount",23,tl.wholeHourCount());
	}
	@Test
	public void hoursTest() {
		Instant i0 = Instant.parse("1990-05-17T05:10:00Z");
		Instant i1 = Instant.parse("1990-05-17T22:04:00Z");
		TimeLine tl=new TimeLine(i0.getEpochSecond()*1000L,i1.getEpochSecond()*1000L,ZoneId.of("Z"));
		System.out.println(tl.quarterHourLabels());
		assertEquals("wholeHourCount",23,tl.wholeHourCount());
	}

}
