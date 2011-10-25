package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ESEEvent extends Model {

	@ManyToOne
	public ESECalendar correspondingCalendar;
	public String eventName;
	public Date startDate;
	public Date endDate;
	public boolean isPublic;

	public ESEEvent(@Required String name, @Required String strStart,
			@Required String strEnd, @Required String strIsPublic) {
		this.eventName = eventName;
		this.startDate = ESEConversionHelper.convertStringToDate(strStart);
		this.endDate = ESEConversionHelper.convertStringToDate(strEnd);
		this.correspondingCalendar = correspondingCalendar;
		this.isPublic = Boolean.parseBoolean(strIsPublic);
	}

	/**
	 * @deprecated Es soll stattdessen {@link #editEventName(String)} verwendet
	 *             werden
	 */
	public void renameEvent(@Required String newName) {
		editEventName(newName);
	}

	public String getEventName() {
		return this.eventName;
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

	// TODO: Setters = editEvents? How does this look like?
	public void editEventName(@Required String newName) {
		this.eventName = newName;
	}

	public void editStartDate(@Required String newStartDate) {
		this.startDate = ESEConversionHelper.convertStringToDate(newStartDate);
	}

	public void editEndDate(@Required String newEndDate) {
		this.endDate = ESEConversionHelper.convertStringToDate(newEndDate);
	}

	public void editVisibility(@Required String publicViewable) {
		this.isPublic = Boolean.parseBoolean(publicViewable);
	}

}
