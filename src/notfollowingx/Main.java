package notfollowingx;

import java.util.ArrayList;

import Instagram.Instagram;
import Instagram.Person;

public class Main {

	public static void main(String[] args) {
		
		String username = "***REMOVED***";
		String password = "***REMOVED***!";

		//get session from mongodb
		Mongodb mdb = new Mongodb();		
		String ds_user_id = mdb.getDs_user_id();
		String sessionId = mdb.getSessionId();
		//check session
		Instagram instagram = new Instagram();
		instagram.connect("session", sessionId, ds_user_id);
		if(!instagram.getSessionIdValid()) {
			instagram.connect("login", username, password);
			//set new session
			mdb.saveSession(instagram.getSessionId());
		}
		//check session again
		if(!instagram.getSessionIdValid()) {
			System.out.println("Session still not valid!");
			return;
		}
		
		//get old follower
		
		
		//get new followers
		instagram.setFollowingAndFollowers("followers");
		ArrayList<Person> followers = instagram.getFollowers();
		//set followers in mongodb
		mdb.setFollower(followers);
		//compare old and new
		
	}

}
