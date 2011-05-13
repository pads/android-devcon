package com.devcon.prefs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {
	private SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        refreshDisplay();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_prefs:
        	Intent i = new Intent(this, ShowPreferences.class);
        	startActivity(i);
        	return true;
        }
       
        return super.onOptionsItemSelected(item);
    }
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		refreshDisplay();
	}
	
	private void refreshDisplay() {
		((TextView)findViewById(R.id.username)).setText(prefs.getString("pref_username_key", "username"));
		((TextView)findViewById(R.id.password)).setText(prefs.getString("pref_password_key", "password"));
		((TextView)findViewById(R.id.checkbox)).setText(String.valueOf(prefs.getBoolean("pref_autosignin_key", true)));
		((TextView)findViewById(R.id.nested)).setText(prefs.getString("pref_nested_key", "nested"));
	}
}