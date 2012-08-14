package com.example.lpchomecontrol;


import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.lpchomecontrol.MainActivity.ActivityReceiver;

public class SetServer extends Activity {
	
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
	public String serveraddress ;
	private Button save_server_button ;
	private EditText editText_server_address ;
	private Dialog dialogbox ;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_server);
        
        dialogbox = new Dialog(this);
        
        
        editText_server_address = (EditText)findViewById(R.id.editTextServerAddress);
        serveraddress = editText_server_address.getText().toString(); //init data from what we have in the server
                
        save_server_button = (Button)findViewById(R.id.buttonSaveServer);
        save_server_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				//now we save ;
				String new_server =  serveraddress = editText_server_address.getText().toString(); //init data from what we have in the server
				//make sure it is not empty
				if (new_server.length() == 0 )
				{
					//we have a problem , use proper server name
					//TextView txt= (TextView)dialogbox.findViewByID(R.id.textbox);
					//txt.setText("Empty server name is anot allowed");
					dialogbox.setTitle("Empty server name !");
					dialogbox.show();
					//TODO make proper dialogbox 
					return;
					
					
				}
				//update the string for the server and get back to the main activity
				serveraddress = editText_server_address.getText().toString(); //update server 
				//send intent to the main activity
				
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ActivityReceiver.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_OUT_MSG, serveraddress);
                sendBroadcast(broadcastIntent);

                finish();
				
				
			}
		});
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_set_server, menu);
        return true;
    }
}
