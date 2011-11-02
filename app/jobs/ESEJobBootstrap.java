package jobs;

import play.jobs.*;
import play.test.*;
import models.*;

@OnApplicationStart
public class ESEJobBootstrap extends Job
{
	public void doJob (
	) {
		/**
		 *	disable fixtures for now..
		 *
		 *
		Fixtures.deleteAll();
		Fixtures.load("../test/data.yml");
		 *
		 */

		ESEUser steve = ESEFactory.createUser(
			"steve", "doocy", "Steve", "Doocy");
		ESEUser gretchen = ESEFactory.createUser(
			"gretchen", "carlson", "Gretchen", "Carlson");
		ESEUser brian = ESEFactory.createUser(
			"brian", "kilmeade", "Brian", "Kilmeade");

		ESECalendar steve_work = ESEFactory.createCalendar(
			"work", steve);
		ESECalendar steve_private = ESEFactory.createCalendar(
			"private", steve);
		ESECalendar gretchen_work = ESEFactory.createCalendar(
			"work", gretchen);
		ESECalendar gretchen_private = ESEFactory.createCalendar(
			"private", gretchen);
		ESECalendar brian_work = ESEFactory.createCalendar(
			"work", brian);
		ESECalendar brian_private = ESEFactory.createCalendar(
			"private", brian);

		ESEFactory.createEvent("event for steve #1",
			"04.11.2011 14:00", "04.11.2011 18:00",
			"true", steve_work);
		ESEFactory.createEvent("event for steve #2 (private)",
			"08.11.2011 10:00", "09.11.2011 20:00",
			"false", steve_work);
		ESEFactory.createEvent("event for steve #1",
			"04.11.2011 14:00", "04.11.2011 18:00",
			"true", steve_private);
		ESEFactory.createEvent("event for steve #2 (private)",
			"08.11.2011 10:00", "09.11.2011 20:00",
			"false", steve_private);

		ESEFactory.createEvent("event for gretchen #1",
			"04.11.2011 14:00", "04.11.2011 18:00",
			"true", gretchen_work);
		ESEFactory.createEvent("event for gretchen #2 (private)",
			"08.11.2011 10:00", "09.11.2011 20:00",
			"false", gretchen_work);
		ESEFactory.createEvent("event for gretchen #1",
			"04.11.2011 14:00", "04.11.2011 18:00",
			"true", gretchen_private);
		ESEFactory.createEvent("event for gretchen #2 (private)",
			"08.11.2011 10:00", "09.11.2011 20:00",
			"false", gretchen_private);

		ESEFactory.createEvent("event for brian #1",
			"04.11.2011 14:00", "04.11.2011 18:00",
			"true", brian_work);
		ESEFactory.createEvent("event for brian #2 (private)",
			"08.11.2011 10:00", "09.11.2011 20:00",
			"false", brian_work);
		ESEFactory.createEvent("event for brian #1",
			"04.11.2011 14:00", "04.11.2011 18:00",
			"true", brian_private);
		ESEFactory.createEvent("event for brian #2 (private)",
			"08.11.2011 10:00", "09.11.2011 20:00",
			"false", brian_private);
	}
}
