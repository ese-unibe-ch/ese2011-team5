package jobs;

import java.util.ArrayList;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import models.ESEDatabase;
import models.ESEUser;

@OnApplicationStart
public class Bootstrap extends Job
{
     public void doJob() throws IllegalAccessException
     {
         try
         {
             ArrayList<ESEUser> userList = ESEDatabase.getAllUsers();
             assert userList != null;
             System.out.println("Database is already initialized!");
             return;
         }
         catch(NullPointerException e)
         {
             ESEDatabase.setUserList(new ArrayList<ESEUser>());
         }
     }
}
