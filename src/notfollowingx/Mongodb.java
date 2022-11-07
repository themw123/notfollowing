package notfollowingx;


import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import Instagram.Person;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
public class Mongodb {
	

	private final String endpoint = "mongodb+srv://marv:***REMOVED***@cluster0.4ejve.mongodb.net/?retryWrites=true&w=majority";
	private ConnectionString connectionString;
	private MongoClientSettings settings;
	private MongoClient mongoClient;
	private MongoDatabase databaseSession;
	private MongoDatabase databaseNotFollowing;
	private MongoCollection<Document> sessions;
	private MongoCollection<Document> follower;

	
	private String sessionId;
	private String ds_user_id;
	
	public Mongodb() {
		connectionString = new ConnectionString(endpoint);
		settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		mongoClient = MongoClients.create(settings);
		databaseSession = mongoClient.getDatabase("session");
		databaseNotFollowing = mongoClient.getDatabase("notFollowing");
		sessions = databaseSession.getCollection("sessions");
		follower = databaseNotFollowing.getCollection("follower");
		Logger mongoLogger = Logger.getLogger("org.mongodb");
		mongoLogger.setLevel(Level.WARNING);
		getSessionDB();
	}
	
	private void getSessionDB() {
        Document doc = sessions.find(Filters.eq("name", "instagram")).first();
        sessionId = doc.getString("sessionId");
	    ds_user_id = doc.getString("ds_user_id");
	}
	
	public void setFollowerDB(ArrayList<Person> followers) {
       
		//del all
		follower.deleteMany(new Document());
		
		//add
		List<Document> followersDocuments = new ArrayList<Document>();
        for(Person follower : followers) {
        	Document document = new Document().append("name", follower.getName()).append("id", follower.getId());
        	followersDocuments.add(document);
        }
        follower.insertMany(followersDocuments);        
	}
	
	public ArrayList<Person> getFollowerDB() {
		
		ArrayList<Person> followers = new ArrayList<Person>();
		
	    FindIterable<Document> iterDoc = follower.find();
	    MongoCursor<Document> it = iterDoc.iterator();
	    while (it.hasNext()) {    	
	    	Document document = it.next();
	    	Person person = new Person(document.getLong("id"), document.getString("name"));
	    	followers.add(person);
	    }
	    return followers;

        
	}
	
	public void setSessionDB(String sessionString) {
	      sessions.updateOne(Filters.eq("name","instagram"), Updates.set("sessionId", sessionString));
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getDs_user_id() {
		return ds_user_id;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setDs_user_id(String ds_user_id) {
		this.ds_user_id = ds_user_id;
	}
	
	
}
