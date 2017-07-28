package org.timeflame.data;

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
	public List<String> quarterHourLabels() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("h:mma");
		return quarterHours()
				.stream()
				.map((Instant i)->f.format(i).replaceFirst(".*:[^0].*", ""))
				.collect(Collectors.toList());
	}
}
