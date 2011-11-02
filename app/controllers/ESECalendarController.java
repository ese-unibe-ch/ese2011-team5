package controllers;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import play.mvc.*;
import play.data.validation.*;
import models.*;
import utils.*;

@With(Secure.class)
public class ESECalendarController extends Controller
{
	private static String loggedInUser = Secure.Security.connected();

	public static void listEvents (String uriUser, String uri_cal, 
			String uri_yy, String uri_mm, String uri_dd) {
		String user = uriUser==null ?loggedInUser :uriUser;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		ESEMessage msg = null;
		List<ESEEvent> le = null;

		if ((u = ESEUser.getUser(user)) == null) {
			user = loggedInUser;
			u = ESEUser.getUser(user);
		}
		if ((c = u.getCalendar(cal)) != null) {
			msg = new ESEMessage();
			msg.lsEvents(uri_yy, uri_mm, uri_dd, c);
			le = permitted(c)
				?c.getListOfEventsRunningAtDay(
					msg.date_human, false)
				:c.getListOfEventsRunningAtDay(
					msg.date_human, true);
		}
		render(user, cal, le, msg);
	}

	public static void listEvents (String uriUser, String uri_cal) {
		String user = uriUser==null ?loggedInUser :uriUser;
		String cal = uri_cal;

		ESECalendarController.listEvents(user, cal, null, null, null);
	}

	public static void addEvent (String uriUser, String uri_cal, String eid, 
			String ename, String ebeg, String eend, String epub, Boolean err_date) {
		String user = uriUser==null ?loggedInUser :uriUser;
		String cal = uri_cal;

		render(user, cal, eid, ename, ebeg, eend, epub, err_date);
	}

	public static void addEventPost (String uriUser, String uri_cal, 
			String eid, @Required String ename, @Required String ebeg, @Required String eend, String epub) {
		String user = uriUser==null ?loggedInUser :uriUser;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		Date dbeg, dend;
		Boolean err_date = false;
		SimpleDateFormat sdf = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm");

		try {
			dbeg = sdf.parse(ebeg);
			dend = sdf.parse(eend);
			if (dend.compareTo(dbeg) < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			err_date = true;
		}
		if ((u = ESEUser.getUser(user)) == null) {
			user = loggedInUser;
			u = ESEUser.getUser(user);
		}
		if (!validation.hasErrors() && !err_date &&
		   (c = u.getCalendar(cal)) != null) {
			if (permitted(c)) {
				epub = epub==null ?"false" :"true";
				if (eid != null) {
					c.removeEvent(Long.parseLong(eid));
				}
				ESEFactory.createEvent(
					ename, ebeg, eend, epub, c);
			}
			ESECalendarController.listEvents(user, cal);
		}
		params.flash();
		validation.keep();
		ESECalendarController.addEvent(user, cal, eid, ename, ebeg,
			eend, epub, err_date);
	}

	public static void modifyEvent(String uriUser, String uri_cal, String eid) {
		String user = uriUser==null ?loggedInUser :uriUser;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		ESEEvent e = null;	/* XXX: needs handling */
		if ((u = ESEUser.getUser(user)) == null) {
			user = loggedInUser;
			u = ESEUser.getUser(user);
		}
		if ((c = u.getCalendar(cal)) != null && permitted(c)) {
			e = c.getEvent(Long.parseLong(eid));
		}
		/**
		 *	XXX: ugly below
		 */
		ESECalendarController.addEvent(user, cal, eid, e.getEventName(),
			ESEConversionHelper.convertDateToString(
				e.getStartDate()),
			ESEConversionHelper.convertDateToString(
				e.getEndDate()),
			((Boolean)e.isPublic()).toString(), null);
	}

	public static void delEvent (String uri_user, String uri_cal, String eid) {
		String user = uri_user==null ?loggedInUser :uri_user;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		if ((u = ESEUser.getUser(user)) == null) {
			user = loggedInUser;
			u = ESEUser.getUser(user);
		}
		if ((c = u.getCalendar(cal)) != null && permitted(c)) {
			c.removeEvent(Long.parseLong(eid));
		}
		ESECalendarController.listEvents(user, cal);
	}

	public static Boolean permitted (
		ESECalendar c
	) {
		String cal_owner = c.getOwner().getUsername();
		return loggedInUser.equals(cal_owner);
	}
}
