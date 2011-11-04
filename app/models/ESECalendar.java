package models;

public class ESECalendar {

	private int calendarID;
	private String calendarName;
	private ESEUser owner;
	
	public ESECalendar(int calendarID, String calendarName,
			ESEUser owner) {
		this.calendarID = calendarID;
		this.calendarName = calendarName;
		this.owner = owner;
	}

	public int getID() {
		return calendarID;
	}

	public void add(ESEEvent eventToAdd) {
		// TODO Auto-generated method stub
		
	}

}
