package org.timeflame.data;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeLine {
	private final Instant start;
	private final Instant end;
	private ZoneId zone;

	public TimeLine(long startEpochMs, long endEpochMs) {
		this(startEpochMs, endEpochMs, ZoneId.systemDefault());
	}

	public TimeLine(long startEpochMs, long endEpochMs, ZoneId zone) {
		super();
		this.start = Instant.ofEpochMilli(startEpochMs).truncatedTo(ChronoUnit.HOURS);
		this.end = Instant.ofEpochMilli(endEpochMs+59900L).truncatedTo(ChronoUnit.HOURS);
		this.zone = zone;
	}
	
	public int wholeHourCount()
	{
		return (int)ChronoUnit.HOURS.between(start, end)+1;
	}
	
	public List<Instant> hours() {
		ArrayList<Instant> arr=new ArrayList<>();
		for (int i=0;i<wholeHourCount();i++) {
			arr.add(Instant.from(start.plus(Duration.ofHours(i))));
		}
		return arr;
	}
	public List<Instant> quarterHours() {
		ArrayList<Instant> arr=new ArrayList<>();
		for (int i=0;i<wholeHourCount();i++) {
			arr.add(Instant.from(start.plus(Duration.ofMinutes(i*15))));
		}
		return arr;
	}
	public List<List<String>> quarterHourLabels() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("h:mma").withZone(zone);
		DateTimeFormatter d = DateTimeFormatter.ofPattern("MMM dd YYYY").withZone(zone);
		List<List<String>> r=new ArrayList<>();
		r.add(	quarterHours()
				.stream()
				.map((Instant i)->f.format(i).replaceFirst(".*:[^0].*", ""))
				.collect(Collectors.toList()));
		r.add(	quarterHours()
				.stream()
				.map((Instant i)->d.format(i))	
				.collect(Collectors.toList()));
		for (int i=r.get(1).size()-1;i>0;i--) {
			if (r.get(1).get(i-1).matches(r.get(1).get(i))) {
				r.get(1).set(i, "");
			}
		}
		return r;
		
		
	}
}
