package com.sample.pushtest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class MainActivity extends ActionBarActivity {

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPush = (Button) findViewById(R.id.button_push);
        btnPush.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi") @Override
			public void onClick(View arg0) {
//			    // Prepare intent which is triggered if the
//			    // notification is selected
//			    Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
//			    PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			    
//			    Notification noti = new Notification.Builder(MainActivity.this)
//			        .setContentTitle("New mail from " + "test@gmail.com")
//			        .setContentText("Subject")
//			        .setSmallIcon(R.drawable.ic_launcher)
//			        .setContentIntent(pIntent).build();
//			    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//			    noti.flags |= Notification.FLAG_AUTO_CANCEL;
//			    notificationManager.notify(0, noti);	
				
				registerInBackground();
			}
		});
    }

    @SuppressWarnings("unchecked")
	private void registerInBackground() {
    	new AsyncTask() {
    		
            @Override
            protected Object doInBackground(Object... params) {
            	GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
            	String SENDER_ID = ""; //registration id here from google developer console
            			
            	String regId = null;
            	try {
        			regId = gcm.register(SENDER_ID);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	Log.d("", "== REGISTRATION ID IS: " + regId);
            	
            	// sending a message to the server
            	AtomicInteger msgId = new AtomicInteger();
            	String msg = "";
                try {
                    Bundle data = new Bundle();
                        data.putString("my_message", "Title of notification");
                        data.putString("my_action",
                                "com.google.android.gcm.demo.app.ECHO_NOW");
                        String id = Integer.toString(msgId.incrementAndGet());
                        gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                        msg = "Sent message";
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            	
            	// return null;
            }
            
            
            
            protected void onPostExecute(String msg) {
                //Log.d(msg + "\n");
            }
    	
    	}.execute(null, null, null);    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
