package jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import models.ESECalendar;
import models.ESEDatabase;
import models.ESEGroup;
import models.ESEUser;

@OnApplicationStart
public class Bootstrap extends Job
{
	public void doJob()
	{
		
		ESEDatabase.createUser("guest", "guest", "guest", "guest");
		ESEDatabase.setCurrentUser("guest");
		
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
		cals1.get(0).addEvent("E1ofUser1", "13.04.2011 14:00", "14.04.2011 13:00", true);
		cals1.get(1).addEvent("E2ofUser1", "14.04.2011 14:00", "15.04.2011 13:00", true);
		cals1.get(1).addEvent("E3ofUser1", "15.04.2011 14:00", "16.04.2011 13:00", true);
		ArrayList<ESECalendar> cals2 = user2.getCalendarList();
		cals2.get(0).addEvent("E1ofUser2", "13.04.2011 14:00", "14.04.2011 13:00", true);
		cals2.get(1).addEvent("E2ofUser2", "14.04.2011 14:00", "15.04.2011 13:00", true);
		cals2.get(1).addEvent("E3ofUser2", "15.04.2011 14:00", "16.04.2011 13:00", true);
		ArrayList<ESECalendar> cals3 = user3.getCalendarList();
		cals3.get(0).addEvent("E1ofUser3", "23.04.2011 14:00", "24.04.2011 13:00", true);
		cals3.get(1).addEvent("E2ofUser3", "24.04.2011 14:00", "25.04.2011 13:00", true);
		cals3.get(1).addEvent("E3ofUser3", "25.04.2011 14:00", "26.04.2011 13:00", true);
		
		/*Add groups*/
		user1.addGroup("1Testgroup1");
		user1.addGroup("1Testgroup2");
		
		user2.addGroup("2Testgroup1");
		user2.addGroup("2Testgroup2");
		
		user3.addGroup("3Testgroup1");
		user3.addGroup("3Testgroup2");
	
	}
	

}