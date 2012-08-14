package com.example.lpchomecontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	public String serveraddress ;
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
    private ActivityReceiver receiver;
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

	
	public class ActivityReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =    
           "com.example.lpchomecontrol.intent.action.MESSAGE_FROM_SERVICE";
         
        
         public void onReceive(Context context, Intent intent) {
                Log.d("com.example.lpchomecontrol","onReceive");
                String output = "";
                output = intent.getStringExtra(PARAM_OUT_MSG);
                if (output != null)
                {
                	serveraddress = output ;
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
