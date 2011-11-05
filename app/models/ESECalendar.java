package models;

import java.util.ArrayList;

public class ESECalendar {

	private int calendarID;
	private String calendarName;
	private ESEUser owner;
	private ArrayList<ESEEvent> eventList;
	
	public ESECalendar(int calendarID, String calendarName,
			ESEUser owner) {
		assert(owner != null);
		
		eventList = new ArrayList<ESEEvent>();
		this.calendarID = calendarID;
		this.calendarName = calendarName;
		this.owner = owner;
	}

	

	public void addEvent(ESEEvent eventToAdd) {
		assert(eventToAdd != null);
		eventList.add(eventToAdd);		
	}
	
	public void removeEvent(ESEEvent event){
		//TODO
		/*
		 * Maybe with id?
		 */
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

	public ArrayList<ESEEvent> getAllEvents() {
		return this.eventList;
	}

	public ArrayList<ESEEvent> getAllPublicEvents() {
		ArrayList<ESEEvent> publicEventsList = new ArrayList<ESEEvent>();
		for (ESEEvent event : eventList){
			if (event.isPublic() == true)
				publicEventsList.add(event);
		}
		return publicEventsList;
	}

}
