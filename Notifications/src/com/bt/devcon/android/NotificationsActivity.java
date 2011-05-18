package com.bt.devcon.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NotificationsActivity extends Activity implements OnClickListener {
    
	private static final int NOTIFICATION_ID = 1;
	private static final int DIALOG_ID = 2;
	
	private NotificationManager notificationManager;
	private Notification notification;
	private Button toastButton, statusBarButton, dialogButton, clearNotificationButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        toastButton = (Button)findViewById(R.id.toastButton);
        toastButton.setOnClickListener(this);
        
        statusBarButton = (Button)findViewById(R.id.statusBarButton);
        statusBarButton.setOnClickListener(this);
        
        dialogButton = (Button)findViewById(R.id.dialogButton);
        dialogButton.setOnClickListener(this);
        
        clearNotificationButton = (Button)findViewById(R.id.clearNotificationButton);
        clearNotificationButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.toastButton:
			showToast();
			break;
		case R.id.statusBarButton:
			if(notification == null) {
				showStatusBarNotification();
			} else {
				updateStatusBarNotification();
			}
			break;
		case R.id.dialogButton:
			// Built in method to show dialogs.
			showDialog(DIALOG_ID);
			break;
		case R.id.clearNotificationButton:
			clearStatusBarNotification();
			break;
		}
	}
	
	private void showToast() {
		Toast toast = Toast.makeText(getApplicationContext(), "I am a piece of toast.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();	
	}
	
	private void showStatusBarNotification() {
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		notification = new Notification(R.drawable.icon, "I am a status bar notification",
													 System.currentTimeMillis());
		/* You pass a pending intent as part of the notification to tell the notification what to do when it is
		 * selected in the expanded view.  The intent in this example does nothing. */
		PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationsActivity.class), 0);
		notification.setLatestEventInfo(getApplicationContext(), "Notification Title", "Notification Expanded Text", 
										notificationIntent);
		
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	private void updateStatusBarNotification() {
		PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationsActivity.class), 0);
		notification.setLatestEventInfo(getApplicationContext(), "Title Updated", "Content updated", notificationIntent);
		// Append notification options - this is the default system sound.  You can also add vibrate and light options.
		notification.defaults |= Notification.DEFAULT_SOUND;			
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	private void clearStatusBarNotification() {
		if(notificationManager != null) {
			notificationManager.cancel(NOTIFICATION_ID);
		}
	}
	
	/* This is called after showDialog() */
	@Override
    protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Example Progress Dialog");
        dialog.setMessage("Performing some complicated task...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        return dialog;	
	}
}