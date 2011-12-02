/**
 * @author Alt-F4
 */
package controllers;

import java.util.Collections;
import java.util.List;

import models.ESEDatabase;
import models.ESEEvent;
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
		// ?!!!?
		ESEUser user = ESEDatabase.getCurrentUser();

		render(user);
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
			} else
			{
				DateTime upper = formatter.parseDateTime(upperlimit);
				DateTime lower = formatter.parseDateTime(lowerlimit);
				query = new SearchEventVisitor(name, lower, upper);
			}
			user.accept(query);
			List<ESEEvent> events = query.results();
			Collections.sort(events);

			renderText(gson.toJson(events));
		} catch (Exception e)
		{
			renderText(e);
		}

	}
}
