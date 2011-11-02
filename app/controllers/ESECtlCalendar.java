package controllers;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import play.mvc.*;
import play.data.validation.*;
import models.*;
import utils.*;

@With(Secure.class)
public class ESECtlCalendar extends Controller
{
	private static String auth_user = Secure.Security.connected();

	public static void lsEvents (
		String uri_user,
		String uri_cal,
		String uri_yy,
		String uri_mm,
		String uri_dd
	) {
		String user = uri_user==null ?auth_user :uri_user;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		ESEMessage msg = null;
		List<ESEEvent> le = null;

		if ((u = ESEUser.getUser(user)) == null) {
			user = auth_user;
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

	public static void lsEvents (
		String uri_user,
		String uri_cal
	) {
		String user = uri_user==null ?auth_user :uri_user;
		String cal = uri_cal;

		ESECtlCalendar.lsEvents(user, cal, null, null, null);
	}

	public static void addEvent (
		String uri_user,
		String uri_cal,
		String eid,
		String ename,
		String ebeg,
		String eend,
		String epub,
		Boolean err_date
	) {
		String user = uri_user==null ?auth_user :uri_user;
		String cal = uri_cal;

		render(user, cal, eid, ename, ebeg, eend, epub, err_date);
	}

	public static void addEventPost (
		String uri_user,
		String uri_cal,
		String eid,
		@Required String ename,
		@Required String ebeg,
		@Required String eend,
		String epub
	) {
		String user = uri_user==null ?auth_user :uri_user;
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
			user = auth_user;
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
			ESECtlCalendar.lsEvents(user, cal);
		}
		params.flash();
		validation.keep();
		ESECtlCalendar.addEvent(user, cal, eid, ename, ebeg,
			eend, epub, err_date);
	}

	public static void modEvent (
		String uri_user,
		String uri_cal,
		String eid
	) {
		String user = uri_user==null ?auth_user :uri_user;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		ESEEvent e = null;	/* XXX: needs handling */
		if ((u = ESEUser.getUser(user)) == null) {
			user = auth_user;
			u = ESEUser.getUser(user);
		}
		if ((c = u.getCalendar(cal)) != null && permitted(c)) {
			e = c.getEvent(Long.parseLong(eid));
		}
		/**
		 *	XXX: ugly below
		 */
		ESECtlCalendar.addEvent(user, cal, eid, e.getEventName(),
			ESEConversionHelper.convertDateToString(
				e.getStartDate()),
			ESEConversionHelper.convertDateToString(
				e.getEndDate()),
			((Boolean)e.isPublic()).toString(), null);
	}

	public static void delEvent (
		String uri_user,
		String uri_cal,
		String eid
	) {
		String user = uri_user==null ?auth_user :uri_user;
		String cal = uri_cal;

		ESEUser u;
		ESECalendar c;
		if ((u = ESEUser.getUser(user)) == null) {
			user = auth_user;
			u = ESEUser.getUser(user);
		}
		if ((c = u.getCalendar(cal)) != null && permitted(c)) {
			c.removeEvent(Long.parseLong(eid));
		}
		ESECtlCalendar.lsEvents(user, cal);
	}

	public static Boolean permitted (
		ESECalendar c
	) {
		String cal_owner = c.getOwner().getUsername();
		return auth_user.equals(cal_owner);
	}
}
