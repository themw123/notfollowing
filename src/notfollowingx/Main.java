package notfollowingx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Instagram.Instagram;
import Instagram.Person;

public class Main {

	public static void main(String[] args) {

		String username = "marvin";
		String passwordMongo = getPassword(0);
		String passwordInsta = getPassword(1);
		// get session from mongodb
		Mongodb mdb = new Mongodb(passwordMongo);
		mdb.getSessionDB();
		String ds_user_id = mdb.getDs_user_id();
		String sessionId = mdb.getSessionId();
		// check session
		Instagram instagram = new Instagram();
		instagram.connect("session", sessionId, ds_user_id);
		if (!instagram.getSessionIdValid()) {
			instagram.connect("login", username, passwordInsta);
			// set new session
			String test = instagram.getSessionId();
			mdb.setSessionDB(instagram.getSessionId());
		}
		// check session again
		if (!instagram.getSessionIdValid()) {
			System.out.println("Session still not valid!");
			return;
		}

		// get old follower
		ArrayList<Person> followersOld = mdb.getFollowerDB();

		// get new followers
		instagram.setFollowingAndFollowers("followers");
		ArrayList<Person> followersNew = instagram.getFollowers();
		// update followers in mongodb
		mdb.setFollowerDB(followersNew);

		// compare old with new
		ArrayList<Person> notFollowAnymore = new ArrayList<Person>();
		for (Person old : followersOld) {
			boolean still = false;
			for (Person neww : followersNew) {
				if (old.getId() == neww.getId()) {
					still = true;
					break;
				}
			}
			if (!still) {
				notFollowAnymore.add(old);
			}
		}

		if (notFollowAnymore.isEmpty()) {
			System.out.println("No one has left you.");
			return;
		}
		// send whatsapp
		Whatsapp w = new Whatsapp();
		String textWhatsapp = null;
		String textConsole = null;

		if (notFollowAnymore.size() == 1) {
			textWhatsapp = "Folgende%20Person%20folgt%20dir%20seit%20der%20letzten%20%C3%9Cberpr%C3%BCfung%20nicht%20mehr%3A%0A";
			textConsole = "Folgende Person folgt dir seit der letzten Ueberpruefung nicht mehr:";
		} else {
			textWhatsapp = "Folgende%20Personen%20folgen%20dir%20seit%20der%20letzten%20%C3%9Cberpr%C3%BCfung%20nicht%20mehr%3A%0A";
			textConsole = "Folgende Personen folgen dir seit der letzten Ueberpruefung nicht mehr: \n ";
		}

		for (Person person : notFollowAnymore) {
			textWhatsapp = textWhatsapp + person.getName() + "%0A";
			textConsole = textConsole + person.getName() + "\n";

		}

		w.sendMessage(textWhatsapp);
		System.out.println(textConsole);

	}

	public static String getPassword(int index) {
		String key = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
			String line;
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				if (counter == index) {
					key = line;
					break;
				}
				counter++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

}
