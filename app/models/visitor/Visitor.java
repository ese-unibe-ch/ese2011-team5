/**
 * @author Alt-F4
 */
package models.visitor;

import java.util.Collection;

import models.ESECalendar;
import models.ESEEvent;
import models.ESEUser;

public interface Visitor<T> {
	void visit(ESEEvent eseEvent);

	void visit(ESECalendar eseCalendar);

	Collection<T> results();

	void visit(ESEUser eseUser);
}
