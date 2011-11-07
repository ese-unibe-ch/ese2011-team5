package controllers;

import models.ESEDatabase;
import models.ESEUser;
import play.mvc.Controller;

public class Tagger extends Controller {

	public static void footer() {
		ESEUser currentUser = ESEDatabase.getCurrentUser();
		render(currentUser);
	}

}