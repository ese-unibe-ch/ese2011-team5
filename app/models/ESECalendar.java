package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
	
	public ESEEvent getEventById(int id)
	{
		for(ESEEvent e: this.eventList)
		{
			if(e.getEventID()==id)
			{
				return e;
			}
		}
		
		return null;
	}
	

	public void addEvent(String eventName, String startDate, String endDate, boolean isPublic) throws AssertionError, IllegalArgumentException
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
	/*
	 * Mir geht es einfach die Bootstrapt daten so einzugeben. Falls die methode
	 * unerwünscht ist, bitte wieder löschen und Bootstrapt Events anpassen:
	 * statt Date String übergeben.
	 * 
	 * by Judith
	 */
	public void addEvent(String eventName, Date startDate, Date endDate, boolean isPublic) throws AssertionError, IllegalArgumentException
	{
		this.addEvent(eventName, ESEConversionHelper.convertDateToString(startDate), 
				ESEConversionHelper.convertDateToString(endDate), isPublic);		
	}	
	
	public void removeEvent(int eventID){
		ESEEvent eventToRemove=this.getEventById(eventID);
		this.eventList.remove(eventToRemove);
	}

	public ArrayList<ESEEvent> getAllAllowedEvents(){
		if (ESEDatabase.getCurrentUser().equals(this.owner))
			return this.eventList;
		else
			return this.getAllPublicEvents();
	}
	
	public ArrayList<ESEEvent> getAllPublicEvents() {
		ArrayList<ESEEvent> publicEventsList = new ArrayList<ESEEvent>();
		for (ESEEvent event : this.eventList){
			if (event.isPublic())
				publicEventsList.add(event);
		}
		return publicEventsList;
	}
	
	public ArrayList<ESEEvent> getAllEvents(){
		return new ArrayList<ESEEvent>(this.eventList);
	}
	
	public ArrayList<ESEEvent> getAllAllowedEventsOfMonth(int month){
		if (ESEDatabase.getCurrentUser().equals(this.owner))
			return this.getAllEventsOfMonth(month);
		else
			return this.getAllPublicEventsOfMonth(month);
	} 

	public ArrayList<ESEEvent> getAllEventsOfMonth(int month) {
		Calendar monthAsCal = new GregorianCalendar();
		monthAsCal.set(Calendar.MONTH,month);
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		ArrayList<ESEEvent> eventsOfMonth = new ArrayList<ESEEvent>();
		for (ESEEvent event : eventList){
			startCal.setTime(event.getStartDate());
			endCal.setTime(event.getEndDate());
			if (monthAsCal.get(monthAsCal.MONTH) == startCal.get(startCal.MONTH) && 
					monthAsCal.get(monthAsCal.YEAR) == startCal.get(startCal.YEAR))
				eventsOfMonth.add(event);
			else if (monthAsCal.get(monthAsCal.MONTH) == endCal.get(endCal.MONTH) && 
					monthAsCal.get(monthAsCal.YEAR) == endCal.get(endCal.YEAR))
				eventsOfMonth.add(event);
			else if ((monthAsCal.get(monthAsCal.MONTH) > startCal.get(startCal.MONTH) && 
					monthAsCal.get(monthAsCal.YEAR) == startCal.get(startCal.YEAR)) &&
					(monthAsCal.get(monthAsCal.MONTH) < endCal.get(endCal.MONTH) && 
							monthAsCal.get(monthAsCal.YEAR) == endCal.get(endCal.YEAR)))
				eventsOfMonth.add(event);
		}
		return eventsOfMonth;
	}
	
	public ArrayList<ESEEvent> getAllPublicEventsOfMonth(int month) {
		ArrayList<ESEEvent> listOfPublicEvents = new ArrayList<ESEEvent>();
		
		for (ESEEvent event : this.getAllEventsOfMonth(month)){
			if (event.isPublic())
				listOfPublicEvents.add(event);
		}
		return listOfPublicEvents;
	}	
	
	public List<Integer> getDaysFromThisMonth(int month, int year) {
		Calendar cal = new GregorianCalendar();
		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<Integer> daysFromThisMonth = new ArrayList<Integer>();
		for (int i=0; i < max; i++) {
			daysFromThisMonth.add(i+1);
		}
		return daysFromThisMonth;
	}
	
	public List<Integer> getDaysFromLastMonth(int month, int year) {
		List<Integer> daysFromLastMonth = new ArrayList<Integer>();
		Calendar thisMonth = new GregorianCalendar();
		Calendar lastMonth = new GregorianCalendar();
		thisMonth.set(Calendar.MONTH, month);
		thisMonth.set(Calendar.YEAR, year);
		lastMonth.set(Calendar.MONTH, month-1);
		lastMonth.set(Calendar.YEAR, year);
		
		thisMonth.setFirstDayOfWeek(Calendar.MONDAY);
		thisMonth.set(Calendar.DAY_OF_MONTH,1);
		int max = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		int start = thisMonth.get(Calendar.DAY_OF_WEEK);
		
		for (int i=max-start+2; i < max; i++) {
			daysFromLastMonth.add(i+1);
		}
		return daysFromLastMonth;
	}
	
	public List<Integer> getDaysFromNextMonth(int month, int year) {
		List<Integer> daysFromNextMonth = new ArrayList<Integer>();
		int daysToAdd = 7 - (this.getDaysFromThisMonth(month, year).size() 
				+ this.getDaysFromLastMonth(month, year).size())%7 ;
		for(int i=1; i <= daysToAdd; i++)
			daysFromNextMonth.add(i);
		return daysFromNextMonth;
	}
	
	public ArrayList<Integer> getEventDaysOfMonth(int month){
		
		ArrayList<Integer> eventDaysList = new ArrayList<Integer>();
		
		for (ESEEvent event : this.getAllAllowedEvents()){
			
			Calendar startCal = new GregorianCalendar();
			Calendar endCal = new GregorianCalendar();
			startCal.setTime(event.getStartDate());
			endCal.setTime(event.getEndDate());
			
			if (startCal.get(startCal.MONTH) == month){
				int startDay = startCal.get(startCal.DAY_OF_MONTH);
				int endDay = endCal.get(endCal.DAY_OF_MONTH);
				
				int addDay = startDay;
				while (addDay <= endDay){
					eventDaysList.add(addDay);
					addDay+=1;
				}
			}
			else if (endCal.get(endCal.MONTH) == month){
				int startDay = startCal.get(startCal.DAY_OF_MONTH);
				int endDay = endCal.get(endCal.DAY_OF_MONTH);
				
				int addDay = endDay;
				while (addDay >= startDay){
					eventDaysList.add(addDay);
					addDay-=1;
				}
			}
			else if (startCal.get(startCal.MONTH) < month && endCal.get(endCal.MONTH) > month){
				int addDay = 0;//= endDay;
				while (addDay <= 31){ // if a month has only 28/30 days, it does not matter for the output, when 29,30,31 are included
					eventDaysList.add(addDay);
					addDay+=1;
				}
			}
		}
		
		return eventDaysList;
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
