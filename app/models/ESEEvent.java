package models;

import java.util.Date;

public class ESEEvent {
	
	private ESECalendar correspondingCalendar;

	public ESEEvent(int eventID, String eventName, ESECalendar correspondingCalendar,
			Date startDate, Date endDate, boolean isPublic) {
		// TODO Auto-generated constructor stub
	}

	public Object getOwner() {
		return this.correspondingCalendar;
	}

}

