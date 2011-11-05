package models;

import java.util.ArrayList;
import java.util.Date;

public class ESECalendar {

	private static int idCounter = 0;

	private int calendarID;
	private String calendarName;
	private ESEUser owner;
	private ArrayList<ESEEvent> eventList;

	public ESECalendar(String calendarName, ESEUser owner) {
		assert calendarName != "";
		assert(owner != null);
		
		this.calendarID = idCounter++;
		this.eventList = new ArrayList<ESEEvent>();
		this.calendarName = calendarName;
		this.owner = owner;
	}

	public int getID() {
		return this.calendarID;
	}
	
	public String getCalendarName(){
		return this.calendarName;
	}
	
	public ESEUser getOwner(){
		return this.owner;
	}

	public void addEvent(String eventName, ESECalendar correspondingCalendar, 
			String startDate, String endDate, boolean isPublic)
	{
		ESEEvent newEvent = new ESEEvent(eventName, this,
			ESEConversionHelper.convertStringToDate(startDate), 
			ESEConversionHelper.convertStringToDate(endDate), isPublic);		
		for (ESEEvent existingEvent : this.eventList)
		{
			if (checkEventOverlaps(existingEvent, newEvent))
			{
				throw new IllegalArgumentException("New event overlaps with existing event");
			}
		}
		this.eventList.add(newEvent);		
	}
	
	public void removeEvent(int eventID){
		//TODO
		/*
		 * Maybe with id?
		 */
		// TODO: Inform DB
	}

	public void removeEvent(ESEEvent event){
		//TODO
		/*
		 * Maybe with id?
		 */
		//TODO: Inform DB
	}

	public ArrayList<ESEEvent> getAllEvents() {
		return new ArrayList<ESEEvent>(this.eventList);
	}

	public ArrayList<ESEEvent> getAllPublicEvents() {
		ArrayList<ESEEvent> publicEventsList = new ArrayList<ESEEvent>();
		for (ESEEvent event : this.eventList){
			if (event.isPublic() == true)
				publicEventsList.add(event);
		}
		return publicEventsList;
	}
	
	public ArrayList<ESEEvent> getAllAllowedEvents(){
		if (ESEDatabase.getCurrentUser().equals(this.owner))
			return this.getAllEvents();
		else
			return this.getAllPublicEvents();
	}	

	private boolean checkEventOverlaps(ESEEvent existingEvent, ESEEvent newEvent)
	{
		boolean startDateOverlaps = startDateLiesInBetweenExistingEvent(existingEvent, newEvent);
		boolean endDateOverlaps = endDateLiesInBetweenExistingEvent(existingEvent, newEvent);
		boolean embracesEvent = eventIsSubsetOfExistingEvent(existingEvent, newEvent);
		boolean isInEvent = eventContainsExistingEvent(existingEvent, newEvent);

		return startDateOverlaps || endDateOverlaps || embracesEvent || isInEvent;
	}

	private boolean startDateLiesInBetweenExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long existingStartTime = existingEvent.getStartDate().getTime();
		long newStartTime = newEvent.getStartDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();

		return existingStartTime <= newStartTime && newStartTime <= existingEndTime;
	}

	private boolean endDateLiesInBetweenExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long existingStartTime = existingEvent.getStartDate().getTime();
		long newEndTime = newEvent.getEndDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();

		return existingStartTime <= newEndTime && newEndTime <= existingEndTime;
	}

	private boolean eventIsSubsetOfExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long existingStartTime = existingEvent.getStartDate().getTime();
		long newStartTime = newEvent.getStartDate().getTime();
		long newEndTime = newEvent.getEndDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();

		return existingStartTime <= newStartTime && newEndTime <= existingEndTime;
	}

	private boolean eventContainsExistingEvent(ESEEvent existingEvent, ESEEvent newEvent)
	{
		long newStartTime = newEvent.getStartDate().getTime();
		long existingStartTime = existingEvent.getStartDate().getTime();
		long existingEndTime = existingEvent.getEndDate().getTime();
		long newEndTime = newEvent.getEndDate().getTime();

		return newStartTime <= existingStartTime && existingEndTime <= newEndTime;
	}
}
