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
		eventList = new ArrayList<ESEEvent>();
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
			String startDate, String endDate, boolean isPublic) {
		
		eventList.add(new ESEEvent(eventName, this,
			ESEConversionHelper.convertStringToDate(startDate), 
			ESEConversionHelper.convertStringToDate(endDate), isPublic));		
	}
	
	public void removeEvent(int eventID){
		//TODO
		/*
		 * Maybe with id?
		 */
		// TODO: Inform DB
	}
	
	public ArrayList<ESEEvent> getAllEvents() {
		return new ArrayList<ESEEvent>(this.eventList);
	}

	public ArrayList<ESEEvent> getAllPublicEvents() {
		ArrayList<ESEEvent> publicEventsList = new ArrayList<ESEEvent>();
		for (ESEEvent event : eventList){
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
}
