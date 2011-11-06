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
public class Bootstrap extends Job{
	
	public ESEDatabase database;
	
	public void doJob() throws IllegalAccessException{
		
		database = ESEDatabase.getInstance();
		if(database.isEmpty()){
			
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			
			database.createUser("jack", "pw1", "Jack", "Sparrow");
			database.createUser("luke", "pw2", "Luke", "Skywalker");
			database.createUser("frodo", "pw3", "Frodo", "Baggins");
			
			ESEUser jack = database.getUserByName("jack");
			ESEUser luke = database.getUserByName("luke");
			ESEUser frodo = database.getUserByName("frodo");
			
			/*
			 * Jack owns to Calendars: work (3 Events) and private (2 Events)
			 * Jack has one additional Group "team" that contains luke
			 */
			ESECalendar calJackWork = new ESECalendar("work", jack);
			ESECalendar calJackPrivate = new ESECalendar("private", jack);
			jack.addCalendar(calJackWork);
			jack.addCalendar(calJackPrivate);
			
			start.set(2011, 11, 6, 18, 0);
			end.set(2011, 11, 6, 20, 0);
			calJackWork.addEvent("Event1", calJackWork, start.getTime(), end.getTime(), true);
			
			start.set(2011, 10, 22, 12, 0);
			end.set(2011, 10, 22, 20, 0);
			calJackWork.addEvent("Event2", calJackWork, start.getTime(), end.getTime(), false);
			
			start.set(2012, 1, 10, 12, 0);
			end.set(2012, 1, 12, 20, 0);
			calJackWork.addEvent("Event3", calJackWork, start.getTime(), end.getTime(), true);
			
			ESEGroup jackTeam = new ESEGroup("team", jack);
			jack.addGroup(jackTeam);
			jackTeam.addUserToGroup(luke);
			
		}
		
	}
}
