package com.example.lpchomecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.example.lpchomecontrol.BackGroundService.ServiceReceiver;
import com.example.lpchomecontrol.R.id;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	static String serveraddress ;
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_SERVER_MSG = "omsg_server";
    public static final String PARAM_OUT_MSG = "omsg";
    private ActivityReceiver receiver;
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    private Button button_sendtolcd ;
    private Button button_sendtoLED ;
    private EditText edittext_lcdtxt ;
    private CheckBox checkbox_ledon ;
    private Intent msgIntent;

	
	public class ActivityReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =    
           "com.example.lpchomecontrol.intent.action.MESSAGE_FROM_SERVICE";
         
        
         public void onReceive(Context context, Intent intent) {
                Log.d("com.example.lpchomecontrol","onReceive");
                String server = "";
                server = intent.getStringExtra(PARAM_OUT_SERVER_MSG);
                
                
                if (server != null)
                {
                	serveraddress = server ;
                	editor.putString(getString(R.string.sharedprefkey_server), serveraddress); //update settings
                	editor.commit();
                }
                
                
                
         }
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        IntentFilter filter = new IntentFilter(ActivityReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ActivityReceiver();
        registerReceiver(receiver, filter);
        
        button_sendtolcd = (Button)findViewById(R.id.buttonTxtToLCD);
        button_sendtoLED = (Button)findViewById(R.id.buttonSendtoLED);
        edittext_lcdtxt = (EditText)findViewById(R.id.editTextToLCD);
        checkbox_ledon = (CheckBox)findViewById(R.id.checkBoxLEDON);
     
        
        //check if it is empty , if so fill it up with those values
        settings = this.getPreferences(MODE_PRIVATE);
        editor = settings.edit();
        //get the address value for the server
        serveraddress = settings.getString(getString(R.string.sharedprefkey_server), null) ;
        
        if(serveraddress == null)
        {
        	//put default value in the address and update the non-volatile storage
        	serveraddress = getString(R.string.default_server);
        	editor.putString(getString(R.string.sharedprefkey_server), serveraddress);
        	editor.commit();
        	
        }
        
        //run service
        String strInputMsg = "";
        //startService(new Intent(BackGroundService.this, BackGroundService.class));
        msgIntent = new Intent(this, BackGroundService.class);
        msgIntent.putExtra(BackGroundService.PARAM_IN_MSG, strInputMsg);
        startService(msgIntent);

        button_sendtolcd.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				//get the text from the Edit text for LCD and send it over to the server
				String command = edittext_lcdtxt.getText().toString();
				command = "LCD"+command ;
				//send the command with the content to the LPC server
		        
		        
		        Intent broadcastIntent = new Intent();
		        broadcastIntent.setAction(ServiceReceiver.ACTION_RESP);
		        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		        broadcastIntent.putExtra(PARAM_OUT_MSG, command);
		        sendBroadcast(broadcastIntent);

		
				
			}});
        
        
        button_sendtoLED.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				//get the text from the Edit text for LCD and send it over to the server
				boolean led_status = checkbox_ledon.isChecked();
				String  command ;
				if (led_status == true){
					command = "LED"+"1" ;
					}
				else
				{
					command = "LED"+"0" ;
				
				}
				
				//send the command with the content to the LPC server
		        
		        
		        Intent broadcastIntent = new Intent();
		        broadcastIntent.setAction(ServiceReceiver.ACTION_RESP);
		        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		        broadcastIntent.putExtra(PARAM_OUT_MSG, command);
		        sendBroadcast(broadcastIntent);

		
				
			}});
        
   
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_settings:
           Intent myIntent = new Intent(this, SetServer.class);
           startActivityForResult(myIntent, 0);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }   
}
