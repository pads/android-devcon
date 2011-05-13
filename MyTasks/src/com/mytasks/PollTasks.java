package com.mytasks;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.mytasks.db.TaskDbAdapter;

public class PollTasks extends IntentService {
	private TaskDbAdapter mTaskDbHelper;
	private static final String TAG = "PollTasks"; 
	NotificationManager mNotificationManager;
	private static final int notificationId = 1;
	
	public PollTasks() {
		super("PollTasks");
	}
	public PollTasks(String name) {
		super(name);
	}
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.i(TAG, "Polling for overdue tasks");
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mTaskDbHelper = new TaskDbAdapter(this);
		mTaskDbHelper.open();
		
      while (true) {
          synchronized (this) {
              try {
            	Cursor mCursor = mTaskDbHelper.fetchOverdueTasks();
          		mCursor.moveToFirst();
          		if (mCursor != null && mCursor.getCount() > 0) {
          			Notification mNotification = new Notification(R.drawable.icon, "New overdue tasks", System.currentTimeMillis());
          			Intent mNotificationIntent = new Intent(this, ClearOverdueTasks.class);
          			mNotificationIntent.putExtra(MyTasks.CLEAR_OVERDUE_TASKS, true);
          			PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, mNotificationIntent, 0);
          			StringBuilder message = new StringBuilder();
          			if (mCursor.getCount() == 1) {
          				message.append(mCursor.getString(mCursor.getColumnIndexOrThrow(TaskDbAdapter.KEY_TASKNAME)));
          			} else {
          				// more than one overdue task
          				message.append(mCursor.getCount() + " overdue tasks");
          			}
          			mNotification.setLatestEventInfo(this, "MyTasks", message, mPendingIntent);
          			mNotification.flags = Notification.FLAG_AUTO_CANCEL;
          			mNotificationManager.notify(notificationId, mNotification);
          		} else {
          		}
              } catch (Exception e) {
              } finally {
            	  try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
          }
      }
		
		
	}

	
}
