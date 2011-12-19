/**
 * @author Alt-F4
 */
package models.visitor;

import java.util.ArrayList;
import java.util.List;
import models.ESECalendar;

public class SearchCalendarVisitor extends AbstractVisitor<ESECalendar>
{

	private List<ESECalendar> calendars = new ArrayList<ESECalendar>();
	private String calendarName;
	private Integer id;

	public SearchCalendarVisitor(String calendarName)
	{
		this.calendarName = calendarName;
	}

	public SearchCalendarVisitor(Integer id)
	{
		this.id = id;
	}

	@Override
	public void visit(ESECalendar eseCalendar)
	{
		if(this.condition(eseCalendar))
		{
			this.calendars.add(eseCalendar);
		}
	}

	@Override
	public List<ESECalendar> results()
	{
		return this.calendars;
	}

	private boolean condition(ESECalendar eseCalendar)
	{
		if(this.calendarName != null)
		{
			return eseCalendar.getCalendarName().contains(this.calendarName);
		}
		else if(this.id != null)
		{
			return this.id.equals(eseCalendar.getID());
		}
		else
		{
			return false;
		}
	}
}
