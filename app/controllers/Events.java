/**
 * @author Alt-F4
 */
package controllers;

import java.util.Collections;
import java.util.List;

import models.ESEDatabase;
import models.ESEEvent;
import models.ESEException;
import models.ESEExceptionGuestUser;
import models.ESEUser;
import models.visitor.SearchEventVisitor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Events extends Application
{

	private static final String DATETIME_PATTERN = "dd.MM.yyy, HH:mm";
	private static final String DATE_PATTERN = "dd.MM.yyyy";

	public static void searchEvent()
	{
		ESEUser user;
		try 
		{
			user = ESEDatabase.getCurrentUser();
			String lowerlimit = new DateTime().minusYears(1).toString(DATE_PATTERN);
			String upperlimit = new DateTime().plusYears(1).toString(DATE_PATTERN);
			renderArgs.put("upperlimit", upperlimit);
			renderArgs.put("lowerlimit", lowerlimit);
			renderArgs.put("userId", user.getUserID());
		} 
		catch (ESEExceptionGuestUser e) 
		{
			//Don't do anything, because other users than the current user are not
			//allowed to use this method
		}
			render();
	}

	public static void handleEventSearch(@Required Long uid,
			@Required String name, String lowerlimit, String upperlimit)
	{
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_PATTERN);
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.excludeFieldsWithoutExposeAnnotation()
				.setDateFormat(DATETIME_PATTERN).create();

		try
		{
			ESEUser user = ESEDatabase.getUserByID(uid.intValue());
			SearchEventVisitor query;
			if (lowerlimit == null || upperlimit == null)
			{
				query = new SearchEventVisitor(name);
			}
			else
			{
				DateTime upper = formatter.parseDateTime(upperlimit);
				DateTime lower = formatter.parseDateTime(lowerlimit);
				query = new SearchEventVisitor(name, lower, upper);
			}
			user.accept(query);
			List<ESEEvent> events = query.results();
			Collections.sort(events);

			renderText(gson.toJson(events));
		}
		catch (ESEException e)
		{
			renderText(e);
		}
	}
}
