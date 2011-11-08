package models;

import java.util.Date;

public class ESEEvent implements Comparable<ESEEvent>{

	private static int idCounter = 0;

	private int eventID;
	private String eventName;
	private ESECalendar correspondingCalendar;
	private Date startDate;
	private Date endDate;
	private boolean isPublic;

	public ESEEvent(String eventName, ESECalendar correspondingCalendar,
			Date startDate, Date endDate, boolean isPublic) throws AssertionError, IllegalArgumentException {

		// This would prevent creation of events with zero duration
		//assert(startDate.before(endDate));
		assert(!startDate.after(endDate));
		checkDateValidity(startDate, endDate);
		assert (correspondingCalendar != null);
		assert (eventName != "");

		this.eventID = idCounter++;
		this.eventName = eventName;
		this.correspondingCalendar = correspondingCalendar;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPublic = isPublic;

	}

	private void checkDateValidity(Date startDate, Date endDate)
	{
		if (startDate.after(endDate))
		{
			throw new IllegalArgumentException("Invalid start and end date");
		}
	}

	/*
	 * Methods with read only access
	 */

	public int getEventID() {
		return this.eventID;
	}

	public String getEventName() {
		return this.eventName;
	}

	public ESECalendar getCorrespondingCalendar() {
		return this.correspondingCalendar;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}
	
	public String getStringEndDate()
	{
		String str= ESEConversionHelper.convertDateToString(this.endDate);
		System.out.println("END DATE: " + str);
		return str;
	}
	
	public String toString()
	{
		return "ID: "+ this.idCounter +" Name: " + this.eventName + " StartDate: "+ getStringStartDate() + " EndDate: " + this.getStringEndDate();
	}
	
	public String getStringStartDate()
	{
		return ESEConversionHelper.convertDateToString(this.startDate);
	}

	public boolean isPublic() {
		return this.isPublic;
	}

	/*
	 * Methods with read-write access
	 */

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public void setStartDate(Date startDate)
	{
		checkDateValidity(startDate, this.endDate);
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate)
	{
		checkDateValidity(this.startDate, endDate);
		this.endDate = endDate;
	}

	public void setVisibility(boolean publiclyViewable)
	{
		this.isPublic = publiclyViewable;
	}
	
	
	
	@Override
	public int compareTo(ESEEvent compareEvent) {
		if (this.getStartDate().getTime() > compareEvent.getStartDate().getTime())
			return 1;
		if (this.getStartDate().getTime() == compareEvent.getStartDate().getTime())
			return 0;
		else
			return -1;
	}
}
