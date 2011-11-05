package models;

import java.util.ArrayList;

public class ESECalendar {

	private static int idCounter = 0;

	private int calendarID;
	private String calendarName;
	private ESEUser owner;
	private ArrayList<ESEEvent> eventList;
	
	public ESECalendar(String calendarName,
			ESEUser owner) {
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

	public void addEvent(ESEEvent eventToAdd) {
		assert(eventToAdd != null);
		//TODO: Inform DB
		eventList.add(eventToAdd);		
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
