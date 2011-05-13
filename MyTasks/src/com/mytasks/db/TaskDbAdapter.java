package com.mytasks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDbAdapter {
	private static final String DATABASE_NAME = "tasks.db";
	private static final String TASK_TABLE = "task";
	public static final String KEY_TASKNAME = "title";
	public static final String KEY_TASKDESCRIPTION = "description";
	public static final String KEY_TASKCREATED = "created_date";
	public static final String KEY_TASKDUE = "due_date";
    public static final String KEY_TASKID = "_id";
    public static final String KEY_HASALARM = "has_alarm";
    
	private static final int DATABASE_VERSION = 2;
	private static final String TAG = "TaskDbAdapter";
	private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
	private final Context mCtx; 
	
	/**
     * Database creation sql statements
     */
    private static final String DATABASE_CREATE_TASK =
        "CREATE TABLE task (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title text not null, " 
                + "description text, "
                + "created_date TIMESTAMP NOT NULL DEFAULT current_timestamp, " 
                + "due_date TIMESTAMP NOT NULL DEFAULT current_timestamp, " 
                + "has_alarm INTEGER NOT NULL DEFAULT 1);";
	
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public TaskDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    /**
     * Open the task database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public TaskDbAdapter open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Create a new task using the name provided. If the task is
     * successfully created return the new rowId for that task, otherwise return
     * a -1 to indicate failure.
     * 
     * @param name the name of the task
     * @return rowId or -1 if failed
     */
    public long createTask(String title) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASKNAME, title);

        return mDb.insert(TASK_TABLE, null, initialValues);
    }
    
    /**
     * Delete the task with the given rowId
     * 
     * @param rowId id of task to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTask(long rowId) {
        return mDb.delete(TASK_TABLE, KEY_TASKID + "=" + rowId, null) > 0;
    }
    
    public boolean updateTask(long rowId, String name, String description, String dueDate) {
        ContentValues args = new ContentValues();
        args.put(KEY_TASKNAME, name);
        args.put(KEY_TASKDESCRIPTION, description);
        args.put(KEY_TASKDUE, dueDate);

        return mDb.update(TASK_TABLE, args, KEY_TASKID + "=" + rowId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor fetchAllTasks() {
        return fetchAllTasks(null, null);
    }
    
    /**
     * Return a Cursor over the list of all tasks in the database, order by the specified parameters
     * 
     * @param orderKey The column name to sort by
     * @param orderDirection One of either ASC or DESC
     * @return Cursor over all tasks
     */
    public Cursor fetchAllTasks(String orderKey, String orderDirection) {
    	String orderBy = null;
    	if (orderKey != null && orderDirection != null) {
    		orderBy = orderKey + " " + orderDirection;
    	}
        return mDb.query(TASK_TABLE, new String[] {KEY_TASKID, KEY_TASKNAME, KEY_TASKCREATED, KEY_TASKDUE},
        		null, null, null, null, orderBy);
    }
    /**
     * Return a Cursor positioned at the task that matches the given rowId
     * 
     * @param rowId id of task to retrieve
     * @return Cursor positioned to matching task, if found
     * @throws SQLException if task could not be found/retrieved
     */
    public Cursor fetchTask(long rowId) throws SQLException {
        Cursor mCursor =

                mDb.query(true, TASK_TABLE, new String[] {KEY_TASKID,
                        KEY_TASKNAME, KEY_TASKDESCRIPTION, KEY_TASKDUE}, KEY_TASKID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchOverdueTasks() throws SQLException {
    	Cursor mCursor = 
    		mDb.query(true, TASK_TABLE, new String[] {KEY_TASKID, KEY_TASKNAME, KEY_TASKDUE},  KEY_TASKDUE + " < current_timestamp AND " + KEY_HASALARM + "==1", null, null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }
    
    public void clearOverdueTasks() throws SQLException {
    	Log.w(TAG, "Clearing");
    	mDb.execSQL("UPDATE " + TASK_TABLE + " SET " + KEY_HASALARM + "=0 WHERE " + KEY_TASKDUE + " < current_timestamp");
    	Log.w(TAG, "Cleared");
    }
    
    protected static class DbHelper extends SQLiteOpenHelper {

		DbHelper(Context context) {        	
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
		    Log.w(TAG, "Constructing DatabaseHelper");
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, "Creating new database. Version " + DATABASE_VERSION);
		    db.execSQL(DATABASE_CREATE_TASK);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		    Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
	    	if (oldVersion < 2) {
	    		db.execSQL("ALTER TABLE task ADD COLUMN has_alarm NOT NULL DEFAULT 1");
	    	}
		    //db.execSQL("DROP TABLE IF EXISTS task");
	    	//onCreate(db);
		}
	}
}
