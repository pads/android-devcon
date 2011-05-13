package com.devcon.menus;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MainActivity extends Activity {
	TextView mText;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mText = (TextView)findViewById(R.id.text_hello);
        registerForContextMenu(mText);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.context_menu, menu);
    } 
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	  case R.id.context_delete:
    		  Toast.makeText(this, "You clicked 'Delete'", Toast.LENGTH_SHORT).show();
    		  return true;
    	  case R.id.context_edit:
    		  Toast.makeText(this, "You clicked 'Edit'", Toast.LENGTH_SHORT).show();
    		  return true;
    	  default:
    	    return super.onContextItemSelected(item);
    	}
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
        case R.id.menu_close:
        	Toast.makeText(this, "You clicked 'Close'", Toast.LENGTH_SHORT).show();
        	return true;
        case R.id.menu_refresh:
        	Toast.makeText(this, "You clicked 'Refresh'", Toast.LENGTH_SHORT).show();
        	return true;
        case R.id.menu_search:
        	Toast.makeText(this, "You clicked 'Search'", Toast.LENGTH_SHORT).show();
        	return true;
        case R.id.menu_settings:
        	Toast.makeText(this, "You clicked 'Settings'", Toast.LENGTH_SHORT).show();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
}