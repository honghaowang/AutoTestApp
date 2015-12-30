package edu.uml.swin.autotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {

    private static String TAG = "MainActivity";
    private String appName, taskName;
    private String[] appArray, pkgArray, taskArray;
    private Button startBtn, endBtn;
    private Spinner appList, taskList;
    private Menu mMenu;
    private long startTime;
    private String deviceID;
    private Context myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myself = this;

        startBtn = (Button) findViewById(R.id.start);
        endBtn = (Button) findViewById(R.id.end);
        appList = (Spinner) findViewById(R.id.app_list);
        taskList = (Spinner) findViewById(R.id.task_list);

        appArray = getResources().getStringArray(R.array.app);
        pkgArray = getResources().getStringArray(R.array.pkgNmae);
        deviceID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        //设置两个spinner联动
        appList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayAdapter<CharSequence> adapter = null;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Domino_task, R.layout.support_simple_spinner_dropdown_item);
                        taskArray = getResources().getStringArray(R.array.Domino_task);
                        break;
                    case 1:
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Pinterest_task, R.layout.support_simple_spinner_dropdown_item);
                        taskArray = getResources().getStringArray(R.array.Pinterest_task);
                        break;
                    default:
                        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Domino_task, R.layout.support_simple_spinner_dropdown_item);
                        taskArray = getResources().getStringArray(R.array.Domino_task);
                        break;
                }
                taskList.setAdapter(adapter);
                appName = appArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Domino_task, R.layout.support_simple_spinner_dropdown_item);
                taskArray = getResources().getStringArray(R.array.Domino_task);
                appName = appArray[0];
            }
        });

        taskList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskName = taskArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                taskName = taskArray[0];
            }

        });
    }

    public void startBtnClick(View target){
        DBhelper helper = new DBhelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //helper.deleteDatabase(db);
        helper.createDatabase(db);
        ContentValues values = new ContentValues();

        if(LogService.isLogEnable()){
            long end = System.currentTimeMillis();
            startBtn.setText(R.string.startBtn);
            endBtn.setText(R.string.endBtn);
            values.put(DBcontract.LogEntry.COLUMN_END_TIME, end);
            values.put(DBcontract.LogEntry.COLUMN_TASK_DURATION, end - startTime);
            values.put(DBcontract.LogEntry.COLUMN_TASK_STATE, getResources().getString(R.string.successful));
            db.update(DBcontract.LogEntry.TABLE_BASIC_INFO, values, DBcontract.LogEntry.COLUMN_START_TIME + "=" + startTime, null);
            Log.e(TAG, myself.toString());
            FileUploader uploader = new FileUploader(myself);
            uploader.execute();
            LogService.setLogDisable();
        }
        else{
            startTime = System.currentTimeMillis();
            startBtn.setText(R.string.successful);
            endBtn.setText(R.string.giveUp);

            int random = (int) (Math.random()*100 +1);
            values.put(DBcontract.LogEntry.COLUMN_ID, deviceID + '_' + String.valueOf(random));
            values.put(DBcontract.LogEntry.COLUMN_DEVICE_ID, deviceID);
            values.put(DBcontract.LogEntry.COLUMN_APP_NAME, appName);
            values.put(DBcontract.LogEntry.COLUMN_TASK_NAME, taskName);
            values.put(DBcontract.LogEntry.COLUMN_START_TIME, startTime);
            values.put(DBcontract.LogEntry.COLUMN_END_TIME, " ");
            values.put(DBcontract.LogEntry.COLUMN_TASK_DURATION, " ");
            values.put(DBcontract.LogEntry.COLUMN_TASK_STATE, "Start");
            Log.d(TAG, "Values====" + values.toString());
            db.insert(DBcontract.LogEntry.TABLE_BASIC_INFO, DBcontract.LogEntry.COLUMN_ID, values);
            LogService.setLogEnable();
        }
    }

    public void endBtnClick(View target){
        DBhelper helper = new DBhelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();

        if(LogService.isLogEnable()){
            long end = System.currentTimeMillis();
            startBtn.setText(R.string.startBtn);
            endBtn.setText(R.string.endBtn);
            values.put(DBcontract.LogEntry.COLUMN_END_TIME, end);
            values.put(DBcontract.LogEntry.COLUMN_TASK_DURATION, end-startTime);
            values.put(DBcontract.LogEntry.COLUMN_TASK_STATE, getResources().getString(R.string.giveUp));
            db.update(DBcontract.LogEntry.TABLE_BASIC_INFO, values, DBcontract.LogEntry.COLUMN_START_TIME + "=" + startTime, null);
            FileUploader uploader = new FileUploader(myself);
            uploader.execute();
            LogService.setLogDisable();
        }
        else {
            startBtn.setText(R.string.successful);
            endBtn.setText(R.string.giveUp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.BasicInfo:
                showTable(DBcontract.LogEntry.TABLE_BASIC_INFO);
                return true;
            case R.id.InteractionInfo:
                showTable(DBcontract.LogEntry.TABLE_INTERACTION_INFO);
                return true;
            case R.id.deleteDB:
                DBhelper helper = new DBhelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                helper.deleteDatabase(db);
                return true;
            case R.id.createDB:
                helper = new DBhelper(this);
                db = helper.getWritableDatabase();
                helper.createDatabase(db);
                return true;
            default:
                return false;
        }
    }

    public void showTable(String tableName){
        DBhelper helper = new DBhelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor queryResult = db.query(tableName, null, null, null, null, null, null);
        if(queryResult==null)
            return;
        queryResult.moveToFirst();
        String[] colunmnName = queryResult.getColumnNames();
        int colunmnCount = queryResult.getColumnCount();
        String r;
        Log.e(TAG, String.valueOf(queryResult.getCount()) + "recorders");
        for(int i = 0; i<queryResult.getCount(); i++)
        {
            Log.i("Query Result", "-----" + tableName + "-------------");
            for(int j = 0; j<colunmnCount; j++) {
                r = queryResult.getString(j);
                Log.i("Query Result", "[" + colunmnName[j] + "] = " + r);
            }
            queryResult.moveToNext();
        }

    }
}
