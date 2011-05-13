package com.mytasks;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mytasks.db.TaskDbAdapter;

public class TaskDetails extends Activity {
	private TaskDbAdapter mTaskDbHelper;
	EditText mTaskNameText;
	EditText mTaskDescriptionText;
	TextView mTaskDueDateText;
	Button mPickDueDate;
	Long mRowId;
	static final int DATE_DIALOG_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_details);
		mTaskDbHelper = new TaskDbAdapter(this);
		mTaskDbHelper.open();
		
		mTaskDescriptionText = (EditText) findViewById(R.id.task_details_description);
		mTaskDueDateText = (TextView) findViewById(R.id.task_detail_due_date);
		mTaskNameText = (EditText) findViewById(R.id.task_details_name);
		Button updateButton = (Button) findViewById(R.id.task_details_update);
		mPickDueDate = (Button) findViewById(R.id.task_detail_pick_date_button);
		mPickDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

		mRowId = (savedInstanceState==null)?null : (Long)savedInstanceState.getSerializable(TaskDbAdapter.KEY_TASKID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras==null ? null : extras.getLong(TaskDbAdapter.KEY_TASKID);
		}
		populateFields();
		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				saveState();
				Intent data = new Intent();
				data.putExtra(TaskDbAdapter.KEY_TASKNAME, mTaskNameText.getText().toString());
				setResult(RESULT_OK, data);
				finish();
		    }
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//saveState();
		outState.putSerializable(TaskDbAdapter.KEY_TASKID, mRowId);
	}
	
	private void saveState() {
		String taskName = mTaskNameText.getText().toString();
		String taskDescription = mTaskDescriptionText.getText().toString();
		String taskDueDate = mTaskDueDateText.getText().toString();
		if (mRowId == null) {
			long id = mTaskDbHelper.createTask(taskName);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mTaskDbHelper.updateTask(mRowId, taskName, taskDescription, taskDueDate);
		}
	}
	
	private void populateFields() {
		if (mRowId != null) {
			Cursor task = mTaskDbHelper.fetchTask(mRowId);
			startManagingCursor(task);
			mTaskNameText.setText(task.getString(task.getColumnIndexOrThrow(TaskDbAdapter.KEY_TASKNAME)));
			mTaskDueDateText.setText(task.getString(task.getColumnIndexOrThrow(TaskDbAdapter.KEY_TASKDUE)));
			mTaskDescriptionText.setText(task.getString(task.getColumnIndexOrThrow(TaskDbAdapter.KEY_TASKDESCRIPTION)));
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	    	Calendar today = Calendar.getInstance();
	        return new DatePickerDialog(this,
	                    mDateSetListener,
	                    today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
	    }
	    return null;
	}
	
	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mTaskDueDateText.setText(new StringBuilder()
				.append(year).append("-")
				// Month is 0 based so add 1 (and zero-pad)
	            .append(String.format("%02d", monthOfYear + 1)).append("-")
	            .append(String.format("%02d",dayOfMonth))
	        );
		}
	};
}
