package com.mytasks;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.mytasks.db.TaskDbAdapter;

public class MyTasks extends ListActivity implements OnSharedPreferenceChangeListener {
	public enum TaskSortKeys {
		sort_due_date_asc,
		sort_due_date_desc,
		sort_created_date_asc,
		sort_created_date_desc,
		sort_title_asc
	}
	private static final int ACTIVITY_DETAILS = 1;
	public static final String CLEAR_OVERDUE_TASKS = "com.mytasks.CLEAR_OVERDUE_TASKS";
	private int mTaskNumber = 1;
	private TaskDbAdapter mDbHelper;
	private SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        mDbHelper = new TaskDbAdapter(this);
        mDbHelper.open();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        fillData();
        registerForContextMenu(getListView());

        Intent pollIntent = new Intent(this, PollTasks.class);
        startService(pollIntent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.task_options_menu, menu);
        return true;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.task_context_menu, menu);
    }    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add_task:
            createTask();
            return true;
        case R.id.menu_prefs:
        	Intent i = new Intent(this, TaskPreferences.class);
        	startActivity(i);
        	return true;
        }
       
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	  case R.id.delete_task:
    	    deleteTask(info.id);
    	    return true;
    	  default:
    	    return super.onContextItemSelected(item);
    	}
    }    
    
	private void createTask() {
        String taskName = "Task " + mTaskNumber++;
        mDbHelper.createTask(taskName);
        fillData();		
	}

	private void deleteTask(long roundId) {
		mDbHelper.deleteTask(roundId);
		fillData();
		Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show();	    
	}
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Intent i = new Intent(this, TaskDetails.class);
    	i.putExtra(TaskDbAdapter.KEY_TASKID, id);
    	startActivityForResult(i, ACTIVITY_DETAILS);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			Toast.makeText(this, "Updated " + intent.getExtras().getString(TaskDbAdapter.KEY_TASKNAME), Toast.LENGTH_SHORT).show(); 
		}
	}
	
	private void fillData() {
		
		String sortValue = prefs.getString(getString(R.string.pref_sort_key), "sort_title_asc");
		String sortKey = null;
		String sortDirection = null;
		switch (TaskSortKeys.valueOf(sortValue)) {
		case sort_created_date_asc: 
			sortKey = TaskDbAdapter.KEY_TASKCREATED;
			sortDirection = "ASC";
			break;
		case sort_created_date_desc:
			sortKey = TaskDbAdapter.KEY_TASKCREATED;
			sortDirection = "DESC";
			break;
		case sort_due_date_asc:
			sortKey = TaskDbAdapter.KEY_TASKDUE;
			sortDirection = "ASC";
			break;
		case sort_due_date_desc:
			sortKey = TaskDbAdapter.KEY_TASKDUE;
			sortDirection = "DESC";
			break;
		case sort_title_asc:
			sortKey = TaskDbAdapter.KEY_TASKNAME;
			sortDirection = "ASC";
			break;
		}
		
		// Get all of the tasks from the database and create the item list
		Cursor tasksCursor = mDbHelper.fetchAllTasks(sortKey, sortDirection);		
        startManagingCursor(tasksCursor);

        String[] from = new String[] { TaskDbAdapter.KEY_TASKNAME, TaskDbAdapter.KEY_TASKDUE};
        int[] to = new int[] { R.id.task_title, R.id.task_created };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter tasks =
            new SimpleCursorAdapter(this, R.layout.task_row, tasksCursor, from, to);
        setListAdapter(tasks);
	}
	
	@Override
	public void  onSharedPreferenceChanged(SharedPreferences  sharedPreferences, String  key){
	    // prefs have been updated, so need to redraw the screen to reflect new prefs
		fillData();
	}

}