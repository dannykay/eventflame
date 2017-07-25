package org.timeflame.data;

import java.util.ArrayList;
import java.util.Optional;

import com.google.common.collect.TreeMultiset;

/**
 * A time flame graph is constructed by creating layers of timelines for a set
 * of concurrent events. If no events on the graph run concurrently the graph
 * will be a single timeline. The layers of the time flame graph can be thought
 * of as "swimlanes" in which only one event can be active at any time. If a new
 * event starts while other events are still in progress it is placed in the
 * lowest numbered swim lane that does not contain an event.
 * <p>
 * In the case where multiple events start at the same moment the order in which
 * the new events are considered for placement into swimlanes should be
 * deterministic in order produce the same graph with the same input data. The
 * default ordering is based on event elapsed time. Longer events are placed
 * first in order to minimize simlane gaps and help produce the "flame" effect.
 * 
 * @author dkay
 *
 */
public class TimeFlameGraph {
	private final ArrayList<EventSwimLane> swimlanes = new ArrayList<>();
	private TreeMultiset<Event> begins;
	private TreeMultiset<Event> ends;
	private final Object addSync = new Object();
	private boolean enforceAdditionBeginOrder=false;
	
	public TimeFlameGraph() {
		this(true);
	}
	
	public TimeFlameGraph(boolean enforceAdditionBeginOrder) {
		begins = TreeMultiset.create((Event a, Event b) -> a.compareOnBegins(b));
		ends = TreeMultiset.create((Event a, Event b) -> a.compareOnEnds(b));
		this.enforceAdditionBeginOrder=enforceAdditionBeginOrder;
	}

	public class AddOutOfSequenceException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		AddOutOfSequenceException(long v) {
			super(""+v);
		}
	}
	
	public void add(Event event) throws AddOutOfSequenceException {
		synchronized (addSync) {
			if (enforceAdditionBeginOrder && begins.size()>0 && event.beginsBefore(begins.firstEntry().getElement())) {
				throw new AddOutOfSequenceException(event.getBeginEpochMs());
			}
			begins.add(event);
			ends.add(event);
			Optional<EventSwimLane> lane=Optional.empty();
			for (EventSwimLane esl : swimlanes) {
				if (!lane.isPresent() && esl.add(event)) {
					lane=Optional.of(esl);
				}
			}
			if (!lane.isPresent()) {
				EventSwimLane l=new EventSwimLane();
				l.add(event);
				swimlanes.add(l);
			}
		}
	}

	public final Event getEventOfFirstBegin() {
		return begins.firstEntry().getElement();
	}

	public final Event getEventOfLastEnd() {
		return ends.lastEntry().getElement();
	}

	public final int height() {
		return swimlanes.size();
	}

	public int size() {
		int size = 0;
		for (EventSwimLane esl : swimlanes) {
			size += esl.get().size();
		}
		return size;
	}
}
