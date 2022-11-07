package notfollowingx;


import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import java.util.logging.Logger;
import java.util.logging.Level;
public class Mongodb {
	
	//private final String apikey = "***REMOVED***";
	//private final String baseurl = "https://data.mongodb-api.com/app/data-pjlrs/endpoint/data/beta/action/";
	private final String endpoint = "mongodb+srv://marv:***REMOVED***@cluster0.4ejve.mongodb.net/?retryWrites=true&w=majority";
	private ConnectionString connectionString;
	private MongoClientSettings settings;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> session;
	
	private String sessionId;
	private String ds_user_id;
	
	public Mongodb() {
		connectionString = new ConnectionString(endpoint);
		settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		mongoClient = MongoClients.create(settings);
		database = mongoClient.getDatabase("notFollowing");
		session = database.getCollection("session");
		Logger mongoLogger = Logger.getLogger("org.mongodb");
		mongoLogger.setLevel(Level.WARNING);
		setSession();
	}
	
	private void setSession() {
	      Document doc = session.find().first();
	      sessionId = doc.getString("sessionId");
	      ds_user_id = doc.getString("ds_user_id");
	}
	
	public void saveSession(String sessionString) {
	      session.updateOne(session.find().first(), Updates.set("sessionId", sessionString));
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
