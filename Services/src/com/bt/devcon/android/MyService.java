package com.bt.devcon.android;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyService extends IntentService {
	
	private static final int NOTIFICATION_ID = 1;
	private NotificationManager notificationManager;
	private Notification notification;
	
	public MyService() {
		super("MyService");
	}

	@Override
	/* Called after another component calls startService() 
	 * Intents are queued for us - useful for single-threaded work. */
	protected void onHandleIntent(Intent intent) {
		sendNotification();		
		while(true) {
			/* This should run in a separate thread as a service
			 * runs within the application's thread. */
			doSomeLongTermWork();
		}
		// Service would be automatically stopped after this method executes.
	}
	
	@Override
	/* Called after another component calls stopService() */
	public void onDestroy() {		
		notificationManager.cancel(NOTIFICATION_ID);
		sendToast();
	}
	
	private void doSomeLongTermWork() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}		
	}
	
	private void sendNotification() {
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.icon, "Service Started", System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification);
		notificationView.setChronometer(R.id.chronometer, SystemClock.elapsedRealtime(), null, true);
		notification.contentView = notificationView;
		notification.contentIntent = PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0);
		notificationManager.notify(NOTIFICATION_ID, notification);	
	}
	
	private void sendToast() {
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
	}
	
}
