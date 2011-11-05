package models;

import java.util.Date;

public class ESEEvent {
	
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
		assert(!endDate.after(startDate));
		checkDateValidity(startDate, endDate);
		assert(correspondingCalendar != null);
		assert(eventName != "");
		
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

	public int getEventID(){
		return this.eventID;
	}
	
	public String getEventName(){
		return this.eventName;
	}
	
	public ESECalendar getCorrespondingCalendar() {
		return this.correspondingCalendar;
	}
	
	public Date getStartDate(){
		return this.startDate;
	}
	
	public Date getEndDate(){
		return this.endDate;
	}
	
	public boolean isPublic(){
		return this.isPublic;
	}

	/*
	 * Methods with read-write access
	 * All following methods must inform the database about changes carried out here
	 */

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
		//TODO: Inform DB
	}

	public void setStartDate(Date startDate)
	{
		checkDateValidity(startDate, endDate);
		this.startDate = startDate;
		//TODO: Inform DB
	}

	public void setEndDate(Date endDate)
	{
		checkDateValidity(startDate, endDate);
		this.endDate = endDate;
		//TODO: Inform DB
	}

	public void setVisibility(boolean publiclyViewable)
	{
		this.isPublic = publiclyViewable;
		//TODO: Inform DB
	}
}

