/**
 * @author Alt-F4
 */
package models.visitor;

import java.util.List;

import models.ESECalendar;
import models.ESEEvent;
import models.ESEUser;

public abstract class AbstractVisitor<T> implements Visitor<T>
{

	@Override
	public void visit(ESEEvent eseEvent)
	{
	}

	@Override
	public void visit(ESECalendar eseCalendar)
	{
	}

	@Override
	public List<T> results()
	{
		return null;
	}

	@Override
	public void visit(ESEUser eseUser)
	{
	}

}
