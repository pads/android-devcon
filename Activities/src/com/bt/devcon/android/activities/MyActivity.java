package com.bt.devcon.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MyActivity extends Activity {
	
	private TextView activityText;
	private String[] states = {"created", 
							   "started", 
							   "resumed", 
							   "paused", 
							   "stopped", 
							   "destroyed",
							   "back pressed"};
	
	/* The activity is being created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        activityText = (TextView)findViewById(R.id.activityText);
        logState(states[0]);
    }
	
	/* The activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        logState(states[1]);
    }
    
    /* The activity has become visible/has resumed. */
    @Override
    protected void onResume() {
        super.onResume();
        logState(states[2]);
    }
    
    /* Another activity is taking focus (this activity is about to be paused). */
    @Override
    protected void onPause() {
        super.onPause();
        logState(states[3]);
    }
    
    /* The activity is no longer visible (it is now stopped). */
    @Override
    protected void onStop() {
        super.onStop();
        logState(states[4]);
    }
    
    /* The activity is about to be destroyed. */
    @Override
    protected void onDestroy() {
        super.onDestroy();        
        logState(states[5]);
    }
    // Without this the back button kills an application.
    @Override
    public void onBackPressed() {
       logState(states[6]);
       // Tells Android to launch the home screen
       // If we do not fire an intent, nothing happens.
       Intent setIntent = new Intent(Intent.ACTION_MAIN);
       setIntent.addCategory(Intent.CATEGORY_HOME);
       setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(setIntent);
    }

    
    private void logState(String state) {
    	Log.d("Activities", state);
        activityText.append(state + "\n");
    }

}