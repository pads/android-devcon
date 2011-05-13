package com.bt.devcon.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView activityText;
	private String[] states = {"created", 
							   "started", 
							   "resumed", 
							   "paused", 
							   "stopped", 
							   "destroyed"};
	
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
    
    private void logState(String state) {
    	Log.d("Activities", state);
        activityText.append(state + "\n");
    }

}