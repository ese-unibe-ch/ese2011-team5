/**
 * @author Alt-F4
 */
package controllers;

import java.util.Collection;

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

public class Events extends Application {

	public static void searchEvent() {
		// ?!!!?
		ESEUser user = ESEDatabase.getCurrentUser();

		render(user);
	}

	public static void handleEventSearch(@Required Long uid,
			@Required String name, String lowerlimit, String upperlimit) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.excludeFieldsWithoutExposeAnnotation()
				.setDateFormat("yyyy/MM/dd, HH:mm").create();

		try {
			ESEUser user = ESEDatabase.getUserByID(uid.intValue());
			SearchEventVisitor query;
			if (lowerlimit == null || upperlimit == null) {
				query = new SearchEventVisitor(name);
				user.accept(query);
			} else {
				DateTime upper = formatter.parseDateTime(upperlimit);
				DateTime lower = formatter.parseDateTime(lowerlimit);
				query = new SearchEventVisitor(name, lower, upper);
				user.accept(query);
			}

			Collection<ESEEvent> events = query.results();

			renderText(gson.toJson(events));
		} catch (Exception e) {
			renderText("fuck: " + e);
		}

	}
}
