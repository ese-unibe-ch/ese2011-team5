/**
 * @author Alt-F4
 */
package models.visitor;

import java.util.ArrayList;
import java.util.List;

import models.ESECalendar;

public class SearchCalendarVisitor extends AbstractVisitor<ESECalendar> {

	private List<ESECalendar> calendars = new ArrayList<ESECalendar>();
	private String calnendarName;
	private Integer id;

	public SearchCalendarVisitor(String calendarName) {
		this.calnendarName = calendarName;
	}

	public SearchCalendarVisitor(Integer id) {
		this.id = id;
	}

	@Override
	public void visit(ESECalendar eseCalendar) {
		if (this.condition(eseCalendar)) {
			this.calendars.add(eseCalendar);
		}
	}

	@Override
	public List<ESECalendar> results() {
		return this.calendars;
	}

	private boolean condition(ESECalendar eseCalendar) {
		if (this.calnendarName != null) {
			return eseCalendar.getCalendarName().contains(this.calnendarName);
		} else if (id != null) {
			return this.id.equals(eseCalendar.getID());
		} else {
			return false;
		}
	}
}
