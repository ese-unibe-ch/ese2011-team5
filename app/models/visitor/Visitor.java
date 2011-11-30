package models.visitor;

import models.ESECalendar;
import models.ESEEvent;

public interface Visitor {
	void visitEvent(ESEEvent eseEvent);

	void visitCalendar(ESECalendar eseCalendar);
}
