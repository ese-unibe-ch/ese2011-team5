package controllers;

import java.util.ArrayList;

import models.ESECalendar;
import models.ESEEvent;

import play.*;
import play.mvc.*;


public class CalendarController extends Controller{
	
	public static void showEvents(ESECalendar calendar){
		ArrayList<ESEEvent> eventList = new ArrayList<ESEEvent>(); 
		eventList = calendar.getAllAllowedEvents();
		render(eventList);
	}

}