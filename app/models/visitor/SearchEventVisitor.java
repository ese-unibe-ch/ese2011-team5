package models.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.ESECalendar;
import models.ESEEvent;

public class SearchEventVisitor implements Visitor {

	private String name;
	private List<ESEEvent> list;

	public SearchEventVisitor(String name) {
		this.name = name;
		this.list = new ArrayList<ESEEvent>();
	}

	@Override
	public void visitEvent(ESEEvent event) {
		// TODO: implement boundaries
		if (event.getEventName().contains(this.name)) {
			list.add(event);
		}
	}

	public Iterator<ESEEvent> results() {
		return this.list.iterator();
	}

	@Override
	public void visitCalendar(ESECalendar Calendar) {
		// TODO Auto-generated method stub

	}

}
