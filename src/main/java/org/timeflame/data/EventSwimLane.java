package org.timeflame.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EventSwimLane {
	private final LinkedList<Event> list = new LinkedList<>();

	/**
	 * Returns true if the provided event was added to the swimlane. false if
	 * would not fit.
	 * 
	 * @param event
	 * @return
	 */
	public boolean add(Event event) {

		if (list.isEmpty() || list.getLast().getEndEpochMs() <= event.getBeginEpochMs()) {
			list.add(event);
			return true;
		} else if (event.getEndEpochMs()<=list.getFirst().getBeginEpochMs()) {
			list.add(0, event);
			return true;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getEndEpochMs() <= event.getBeginEpochMs() && list.get(i + 1).getBeginEpochMs() >= event.getEndEpochMs()) {
					list.add(i+1, event);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a new ArrayList containing the elements
	 * 
	 * @return
	 */
	public final List<Event> get() {
		List<Event> r = new ArrayList<>();
		r.addAll(list);
		return r;
	}
	
	public final Event getFirst()
	{
		return list.getFirst();
	}
	public final Event getLast()
	{
		return list.getLast();
	}

	@Override
	public String toString() {
		return "EventSwimLane [" + list + "]";
	}
	
}
