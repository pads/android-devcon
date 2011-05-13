package com.mytasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mytasks.db.TaskDbAdapter;

public class ClearOverdueTasks extends Activity {
	private TaskDbAdapter mTaskDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("MyTasks", "ClearOverdueTasks");
		mTaskDbHelper = new TaskDbAdapter(this);
		mTaskDbHelper.open();
		
		mTaskDbHelper.clearOverdueTasks();
		
		Intent mTasksIntent = new Intent(this, MyTasks.class);
		startActivity(mTasksIntent);
		
    }
    
}