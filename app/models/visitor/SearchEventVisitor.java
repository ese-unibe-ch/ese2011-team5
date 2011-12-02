/**
 * @author Alexander Rüedlinger
 */
package models.visitor;

import java.util.ArrayList;
import java.util.List;

import models.ESEEvent;

import org.joda.time.DateTime;

public class SearchEventVisitor extends AbstractVisitor<ESEEvent> {

	private String name;
	private List<ESEEvent> events = new ArrayList<ESEEvent>();
	private Integer id;
	private DateTime upperLimit;
	private DateTime lowerLimit;

	public SearchEventVisitor(String name) {
		this.name = name;
	}

	public SearchEventVisitor(Integer id) {
		this.id = id;
	}

	public SearchEventVisitor(String name, DateTime lowerLimit,
			DateTime upperLimit) {
		this(name);
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
	}

	@Override
	public void visit(ESEEvent event) {
		// TODO: implement boundaries
		if (this.condition(event)) {
			events.add(event);
		}
	}

	private boolean condition(ESEEvent event) {
		DateTime start = new DateTime(event.getStartDate());
		DateTime end = new DateTime(event.getEndDate());

		if (this.upperLimit == null && this.lowerLimit == null) {
			return this.isAlike(event);
		}

		if ((this.lowerLimit.isBefore(start) || this.lowerLimit.equals(start))
				&& (this.upperLimit.isAfter(end) || this.upperLimit.equals(end))) {
			return this.isAlike(event);
		} else {
			return false;
		}
	}

	private boolean isAlike(ESEEvent event) {

		if (this.name != null) {
			return event.getEventName().contains(this.name);
		} else if (this.id != null)
			return this.id.equals(event.getEventID());
		else {
			return false;
		}
	}

	public List<ESEEvent> results() {
		return this.events;
	}

}
