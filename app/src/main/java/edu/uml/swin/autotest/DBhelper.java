package edu.uml.swin.autotest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by honghao on 12/27/2015.
 */
public class DBhelper extends SQLiteOpenHelper{
    private String TAG = "DBhelper";

    public static final String CREATE_TASK_LIST_TABLE =  "CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_TASK_LIST + " ("
            + DBcontract.LogEntry.COLUMN_TASK_ID + " INTEGER PRIMARY KEY,"
            + DBcontract.LogEntry.COLUMN_APP_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_TASK_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_TASK_DESCRIPTION + " TEXT)";

    public static final String CREATE_BASIC_INFO_TABLE =  "CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_BASIC_INFO + " ("
            + DBcontract.LogEntry.COLUMN_ID + " TEXT PRIMARY KEY,"
            + DBcontract.LogEntry.COLUMN_DEVICE_ID + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_APP_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_TASK_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_START_TIME + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_END_TIME + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_TASK_DURATION + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_TASK_STATE + " TEXT)";

    public static final String CREATE_APP_INFO_TABLE =  " CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_APP_INFO + " ("
            + DBcontract.LogEntry.COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY,"
            + DBcontract.LogEntry.COLUMN_APP_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_ACTIVITY_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_WIDGET_COUNT + " INTEGER)";

    public static final String CREATE_ACTIVITY_INFO_TABLE =  "CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_ACTIVITY_INFO + " ("
            + DBcontract.LogEntry.COLUMN_WIDGET_ID + " INTEGER PRIMARY KEY,"
            + DBcontract.LogEntry.COLUMN_ACTIVITY_ID + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_WIDGET_NAME + " TEXT,"
            + DBcontract.LogEntry.COLUMN_WIDGET_TYPE + " TEXT,"
            + DBcontract.LogEntry.COLUMN_WIDGET_TEXT + " TEXT,"
            + DBcontract.LogEntry.COLUMN_LOCATION_X + "INTEGER"
            + DBcontract.LogEntry.COLUMN_LOCATION_Y + "INTEGER"
            + DBcontract.LogEntry.COLUMN_LEVEL + " INTEGER)";

    public static final String CREATE_INTERACTION_RECORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_INTERACTION_INFO + " ("
            //+ LogEntry.COLUMN_INTERACTION_ID + " INTEGER PRIMARY KEY,"
            //+ LogEntry.COLUMN_ID + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_EVENT_TYPE + " TEXT, "
            + DBcontract.LogEntry.COLUMN_APP_NAME + " TEXT, "
            + DBcontract.LogEntry.COLUMN_ACTIVITY_NAME + " TEXT, "
            //+ LogEntry.COLUMN_SCROLL_X + " INTEGER, "
            //+ LogEntry.COLUMN_SCROLL_Y + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_EVENT_TIME + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_EVENT_TIME_DURATION + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_SYS_TIME + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_SYS_TIME_DURATION + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_WINDOW_ID + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_SOURCE_CLASS + " TEXT, "
            + DBcontract.LogEntry.COLUMN_VIEW_RESOURCE_ID + " TEXT, "
            + DBcontract.LogEntry.COLUMN_BOUNDS_IN_PARENT + " TEXT, "
            + DBcontract.LogEntry.COLUMN_BOUNDS_IN_SCREEN + " TEXT, "
            + DBcontract.LogEntry.COLUMN_WINDOW_INFO + " TEXT, "
            + DBcontract.LogEntry.COLUMN_IS_PASSWORD + " TEXT, "
            + DBcontract.LogEntry.COLUMN_REMOVED_COUNT + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_ADDED_COUNT + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_WIDGET_TEXT + " TEXT)";

    public static final String CREATE_SENSOR_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_SENSOR_INFO + " ("
            + DBcontract.LogEntry.COLUMN_SENSOR_RECORD_ID + " INTEGER PRIMARY KEY,"
            + DBcontract.LogEntry.COLUMN_DEVICE_ID + " INTEGER, "
            + DBcontract.LogEntry.COLUMN_ACCELEROMETER_X + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_ACCELEROMETER_Y + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_ACCELEROMETER_Z + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_GYROSCOPE_X + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_GYROSCOPE_Y + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_GYROSCOPE_Z + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_RECORD_TIME + " INTEGER)";

    public static final String CREATE_CONTEXT_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + DBcontract.LogEntry.TABLE_CONTEXT_INFO + " ("
            + DBcontract.LogEntry.COLUMN_RECORD_ID + " INTEGER PRIMARY KEY,"
            + DBcontract.LogEntry.COLUMN_DEVICE_ID + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_BATTERY_STATE + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_NETWORK_STATE + " TEXT,"
            + DBcontract.LogEntry.COLUMN_LIGHT_LEVEL + " INTEGER,"
            + DBcontract.LogEntry.COLUMN_RECORD_TIME + " INTEGER)";

    public static final String DELETE_TASK_LIST_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_TASK_LIST;
    public static final String DELETE_BASIC_INFO_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_BASIC_INFO;
    public static final String DELETE_APP_INFO_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_APP_INFO;
    public static final String DELETE_ACTIVITY_INFO_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_ACTIVITY_INFO;
    public static final String DELETE_INTERACTION_INFOR_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_INTERACTION_INFO;
    public static final String DELETE_SENSOR_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_SENSOR_INFO;
    public static final String DELETE_CONTEXT_INFO_TABLE = "DROP TABLE IF EXISTS " + DBcontract.LogEntry.TABLE_CONTEXT_INFO;

    public DBhelper(Context context){
        super(context, DBcontract.LogEntry.dataBaseFile, null, DBcontract.LogEntry.dataBaseVersion);
    }

    public void createDatabase(SQLiteDatabase db){
        Log.e(TAG, "----------Create tables-----------");
        Log.e(TAG, CREATE_BASIC_INFO_TABLE);
        Log.e(TAG, CREATE_INTERACTION_RECORDER_TABLE);
        db.execSQL(CREATE_BASIC_INFO_TABLE);
        db.execSQL(CREATE_INTERACTION_RECORDER_TABLE);
    }

    public void deleteDatabase(SQLiteDatabase db){
        Log.e(TAG, "----------Delete tables-----------");
        db.execSQL(DELETE_BASIC_INFO_TABLE);
        db.execSQL(DELETE_INTERACTION_INFOR_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        createDatabase(db);
        //db.execSQL(CREATE_TASK_LIST_TABLE);
        //db.execSQL(CREATE_BASIC_INFO_TABLE);
        //db.execSQL(CREATE_APP_INFO_TABLE);
        //db.execSQL(CREATE_ACTIVITY_INFO_TABLE);
        //db.execSQL(CREATE_INTERACTION_RECORDER_TABLE);
        //db.execSQL(CREATE_SENSOR_INFO_TABLE);
        //db.execSQL(CREATE_CONTEXT_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
