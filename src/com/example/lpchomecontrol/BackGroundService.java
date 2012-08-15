package com.example.lpchomecontrol;

import java.io.IOException;
import android.util.Log;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;



public class BackGroundService extends IntentService {



	public static final String PARAM_IN_MSG = "imsg";
	public static final String PARAM_OUT_MSG = "omsg";
	private ServiceReceiver receiver;
	

	class NetowrkProcess extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... aurl) {
        	
    		HttpClient httpClient = new DefaultHttpClient() ;
    		HttpContext localContext = new BasicHttpContext();

    		HttpGet httpGet = new HttpGet(aurl[0].toString());

    		try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



            return null;

        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String unused) {

        }
    }
	
	public class ServiceReceiver extends BroadcastReceiver {

		public static final String ACTION_RESP = "com.example.lpchomecontrol.intent.action.MESSAGE_FROM_ACTIVITY";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("com.example.lpchomecontrol", "onReceive from Service");
			//message recived with new command
			String command;
			command = intent.getStringExtra(PARAM_OUT_MSG);
			String command_type ;
			String command_content ;
			command_type = command.substring(0, 3);
			command_content = command.substring(3);
			
			//first of all check the command and based on it decide the right string to send, 

			List<NameValuePair> params = new LinkedList<NameValuePair>();
			
			if(command_type.contains("LCD"))
			{
				params.add(new BasicNameValuePair("LCD_TEXT",command_content));
			
			}
			else if(command_type.contains("LED") )
			{
				if(command_content.contains("1"))
					params.add(new BasicNameValuePair("LED0", "1"));
				else
					params.add(new BasicNameValuePair("LED0", "0"));
			}


			
			
			String paramString = URLEncodedUtils.format(params, "utf-8");

			
			
			new NetowrkProcess().execute("http://" + com.example.lpchomecontrol.MainActivity.serveraddress +"?"+paramString);
			//execute_http("http://" + com.example.lpchomecontrol.MainActivity.serveraddress +"?"+paramString);




		}
	}
	
	private void execute_http(String cmd) throws ClientProtocolException, IOException
	{
		
		HttpClient httpClient = new DefaultHttpClient() ;
		HttpContext localContext = new BasicHttpContext();

		HttpGet httpGet = new HttpGet(cmd);

		HttpResponse response = httpClient.execute(httpGet, localContext);

	}



	public BackGroundService() {
		super("BackGroundService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter filter = new IntentFilter(ServiceReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ServiceReceiver();
		registerReceiver(receiver, filter);

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		//clean up the system





	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Replace with service binding implementation.
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d("com.example.lpchomecontrol", "Received start id " + startId + ": "
				+ intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// processing done here�.
		

		
		//run forever
		while(true)
		{
			SystemClock.sleep(50);

		}
		




	}








}
