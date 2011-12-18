package jobs;

import java.util.ArrayList;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job
{
	public void doJob() throws ESEException
	{

		//ESEDatabase.createUser("guest", "guest", "guest", "guest");
		//ESEDatabase.setCurrentUser("guest");
		//ESEDatabase.prepareGuestUser();

		/*create users*/
		ESEDatabase.createUser("User1", "pw1", "firstname1", "secondname1");
		ESEDatabase.createUser("User2", "pw2", "firstname2", "secondname2");
		ESEDatabase.createUser("User3", "pw3", "firstname3", "secondname3");

		/*Add Calendars*/
		ESEUser user1 = ESEDatabase.getUserByName("User1");
		user1.addCalendar("1cal1");
		user1.addCalendar("1cal2");
		user1.addCalendar("1cal3");
		user1.addCalendar("1cal4");
		ESEUser user2 = ESEDatabase.getUserByName("User2");
		user2.addCalendar("2cal1");
		user2.addCalendar("2cal2");
		user2.addCalendar("2cal3");
		user2.addCalendar("2cal4");
		ESEUser user3 = ESEDatabase.getUserByName("User3");
		user3.addCalendar("3cal1");
		user3.addCalendar("3cal2");
		user3.addCalendar("3cal3");
		user3.addCalendar("3cal4");

		/*Add events*/
		ArrayList<ESECalendar> cals1 = user1.getCalendarList();
		cals1.get(0).addEvent("E1ofUser1pub", "13.12.2011 14:00", "14.12.2011 13:00", true);
		cals1.get(1).addEvent("E2ofUser1pub", "14.12.2011 14:00", "15.12.2011 13:00", true);
		cals1.get(1).addEvent("E3ofUser1priv", "15.12.2011 14:00", "16.12.2011 13:00", false);
		ArrayList<ESECalendar> cals2 = user2.getCalendarList();
		cals2.get(0).addEvent("E1ofUser2pub", "13.12.2011 14:00", "14.12.2011 13:00", true);
		cals2.get(0).addEvent("ESE", "11.11.2011 11:12", "12.12.2011 11:12", true);
		cals2.get(1).addEvent("E2ofUser2pub", "14.12.2011 14:00", "15.12.2011 13:00", true);
		cals2.get(1).addEvent("E3ofUser2priv", "15.12.2011 14:00", "16.12.2011 13:00", false);
		ArrayList<ESECalendar> cals3 = user3.getCalendarList();
		cals3.get(0).addEvent("E1ofUser3pub", "23.12.2011 14:00", "24.12.2011 13:00", true);
		cals3.get(1).addEvent("E2ofUser3pub", "24.12.2011 14:00", "25.12.2011 13:00", true);
		cals3.get(1).addEvent("E3ofUser3priv", "25.12.2011 14:00", "26.12.2011 13:00", false);

		/*Add groups*/
		user1.addGroup("1Testgroup1");
		user1.addGroup("1Testgroup2");

		user2.addGroup("2Testgroup1");
		user2.addGroup("2Testgroup2");

		user3.addGroup("3Testgroup1");
		user3.addGroup("3Testgroup2");
	}
}
