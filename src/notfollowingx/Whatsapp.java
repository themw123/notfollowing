package notfollowingx;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Whatsapp {

	
	public void sendMessage(String message) {
		OkHttpClient client = new OkHttpClient().newBuilder()
				  .build();
				Request request = new Request.Builder()
				  .url("https://api.callmebot.com/whatsapp.php?phone=***REMOVED***&text="+message+"&apikey=***REMOVED***")
				  .method("GET", null)
				  .build();
				try {
					Response response = client.newCall(request).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	

	
}
