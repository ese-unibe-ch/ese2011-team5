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
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ESECalendar eseCalendar)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<T> results()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void visit(ESEUser eseUser)
	{
		// TODO Auto-generated method stub

	}

}
