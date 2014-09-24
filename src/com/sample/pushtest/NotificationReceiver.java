package com.sample.pushtest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class NotificationReceiver extends WakefulBroadcastReceiver {
	
	public NotificationReceiver() {
		Log.d("", "Came into instantiating Notification Receiver");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("", "Came into onReceive in Notification Receiver");
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(context, "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(context,"Deleted messages on server: " +
                        extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                // Post notification of received message.
                sendNotification(context,"Received: " + extras.toString());
                Log.d("", "Received: " + extras.toString());
            } else {
            	sendNotification(context,"Received random message type: " + extras.toString());
                Log.d("", "Received: " + extras.toString());
            }
        }
		
	}
	
	// Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    @SuppressLint("NewApi") private void sendNotification(Context context, String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
        		context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(pIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }

}