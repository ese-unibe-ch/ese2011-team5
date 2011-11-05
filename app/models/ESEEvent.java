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
			Date startDate, Date endDate, boolean isPublic) {

		assert (startDate.before(endDate));
		assert (correspondingCalendar != null);
		assert (eventName != "");

		this.eventID = idCounter++;
		this.eventName = eventName;
		this.correspondingCalendar = correspondingCalendar;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPublic = isPublic;

	}

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

	public boolean isPublic() {
		return this.isPublic;
	}

}
