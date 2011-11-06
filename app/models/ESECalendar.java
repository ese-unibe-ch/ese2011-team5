package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
				//throw new IllegalArgumentException("New event overlaps with existing event");
			}
		}
		this.eventList.add(newEvent);		
	}
	/*
	 * Mir geht es einfach die Bootstrapt daten so einzugeben. Falls die methode
	 * unerwünscht ist, bitte wieder löschen und Bootstrapt Events anpassen:
	 * statt Date String übergeben.
	 * 
	 * by Judith
	 */
	public void addEvent(String eventName, ESECalendar correspondingCalendar, 
			Date startDate, Date endDate, boolean isPublic)
	{
		ESEEvent newEvent = new ESEEvent(eventName, this, startDate, endDate, isPublic);		
		for (ESEEvent existingEvent : this.eventList)
		{
			if (checkEventOverlaps(existingEvent, newEvent))
			{
				//throw new IllegalArgumentException("New event overlaps with existing event");
			}
		}
		this.eventList.add(newEvent);		
	}	
	
	public void removeEvent(int eventID){
		//TODO
	}

	public ArrayList<ESEEvent> getAllAllowedEvents(){
		if (ESEDatabase.getInstance().getCurrentUser().equals(this.owner))
			return this.eventList;
		else
			return this.getAllPublicEvents();
	}
	
	public ArrayList<ESEEvent> getAllPublicEvents() {
		ArrayList<ESEEvent> publicEventsList = new ArrayList<ESEEvent>();
		for (ESEEvent event : this.eventList){
			if (event.isPublic() == true)
				publicEventsList.add(event);
		}
		return publicEventsList;
	}
	
	public ArrayList<ESEEvent> getAllEvents(){
		return this.eventList;
	}
	
	public ArrayList<ESEEvent> getAllAllowedEventsOfDay(String date){
		if (ESEDatabase.getInstance().getCurrentUser().equals(this.owner))
			return this.getAllEventsOfDay(date);
		else
			return this.getAllPublicEventsOfDay(date);
	} 

	public ArrayList<ESEEvent> getAllEventsOfDay(String date) {
		Date convertedDate = ESEConversionHelper.convertStringToDate(date);
		Calendar dayAsCal = new GregorianCalendar();
		
		dayAsCal.setTime(convertedDate);
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		ArrayList<ESEEvent> eventsOfDay = new ArrayList<ESEEvent>();
		for (ESEEvent event : eventList){
			startCal.setTime(event.getStartDate());
			endCal.setTime(event.getEndDate());
			if (dayAsCal.get(dayAsCal.DAY_OF_YEAR) == startCal.get(startCal.DAY_OF_YEAR) && 
					dayAsCal.get(dayAsCal.YEAR) == startCal.get(startCal.YEAR))
				eventsOfDay.add(event);
			else if (dayAsCal.get(dayAsCal.DAY_OF_YEAR) == endCal.get(startCal.DAY_OF_YEAR) && 
					dayAsCal.get(dayAsCal.YEAR) == endCal.get(startCal.YEAR))
				eventsOfDay.add(event);
			else if ((dayAsCal.get(dayAsCal.DAY_OF_YEAR) > startCal.get(startCal.DAY_OF_YEAR) && 
					dayAsCal.get(dayAsCal.YEAR) == startCal.get(startCal.YEAR)) &&
					(dayAsCal.get(dayAsCal.DAY_OF_YEAR) < endCal.get(startCal.DAY_OF_YEAR) && 
							dayAsCal.get(dayAsCal.YEAR) == endCal.get(startCal.YEAR)))
				eventsOfDay.add(event);
		}
		return eventsOfDay;
	}
	
	public ArrayList<ESEEvent> getAllPublicEventsOfDay(String date) {
		ArrayList<ESEEvent> listOfPublicEvents = new ArrayList<ESEEvent>();
		
		for (ESEEvent event : this.getAllEventsOfDay(date)){
			if (event.isPublic())
				listOfPublicEvents.add(event);
		}
		return listOfPublicEvents;
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
