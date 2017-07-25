package org.timeflame.data;

public class Event {
	
	public class EventBeginEndSequenceException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		EventBeginEndSequenceException(long beginEpochMs, long endEpochMs) {
			super(String.format("beginEpochMs %d is after endEpochMs %d",beginEpochMs,endEpochMs));
		}
	}
	
	private final long beginEpochMs;
	private final long endEpochMs;
	public Event(long beginEpochMs, long endEpochMs) throws EventBeginEndSequenceException{
		super();
		if (beginEpochMs>endEpochMs) {
			throw new EventBeginEndSequenceException(beginEpochMs,endEpochMs);
		}
		this.beginEpochMs = beginEpochMs;
		this.endEpochMs = endEpochMs;
	}
	public Event(long startEpochMs,float secsDuration) {
		this(startEpochMs, startEpochMs+(long)(secsDuration*1000F));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endEpochMs ^ (endEpochMs >>> 32));
		result = prime * result + (int) (beginEpochMs ^ (beginEpochMs >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (endEpochMs != other.endEpochMs)
			return false;
		if (beginEpochMs != other.beginEpochMs)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Event [" +beginEpochMs + "," + endEpochMs + "]";
	}
	public final long getBeginEpochMs() {
		return beginEpochMs;
	}
	public final long getEndEpochMs() {
		return endEpochMs;
	}
	public boolean beginsAfter(Event other) {
		return (other!=null&&other.beginEpochMs<this.beginEpochMs);
	}
	public boolean beginsBefore(Event other) {
		return (other!=null&&other.beginEpochMs>this.beginEpochMs);
	}
	public boolean endsAfter(Event other) {
		return (other!=null&&other.endEpochMs<this.endEpochMs);
	}
	public boolean endsBefore(Event other) {
		return (other!=null&&other.endEpochMs>this.endEpochMs);
	}
	public int compareOnBegins(Event other) {
		int r=0;
		if (this.beginsBefore(other)) {
			r=-1;
		} else if (this.beginsAfter(other)) {
			r=1;
		}
		return r;
	}
	public int compareOnEnds(Event other) {
		int r=0;
		if (this.endsBefore(other)) {
			r=-1;
		} else if (this.endsAfter(other)) {
			r=1;
		}
		return r;
	}
}
