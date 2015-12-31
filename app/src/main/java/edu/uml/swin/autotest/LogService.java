package edu.uml.swin.autotest;

import android.accessibilityservice.AccessibilityService;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by honghao on 12/27/2015.
 */
public class LogService extends AccessibilityService {
    private final String TAG = "LogService";
    public static Boolean logEnable = false;
    private long lastEventTime = 0, lastSysTime = 0;
    SQLiteDatabase db;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //Log.e(TAG, "*****" + String.valueOf(logEnable) + "**********");
        if(!logEnable){ return;}

        String type = getEventType(event);
        if(type.equals("OTHER_EVENT")) { return; }
        Log.d(TAG,type);
        String pkgName = event.getPackageName().toString();
        String activityName = event.getClassName().toString();
        String isPass = String.valueOf(event.isPassword());
        int removeCount = event.getRemovedCount(), addCount = event.getAddedCount();
        int windowID = event.getWindowId();
        long eventTime = event.getEventTime(), sysTime = System.currentTimeMillis();
        long eventTimeDur, sysTimeDur;
        AccessibilityNodeInfo source = event.getSource();

        String windowInfo = null;
        StringBuilder sb = new StringBuilder();
        if(type.equalsIgnoreCase("TYPE_VIEW_CLICKED")) {
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if(rootNode == null){
                return;
            }
            else{
                recycle(sb, rootNode, 1);
                windowInfo = sb.toString();
            }

        }
        //calculate duration
        if(lastEventTime == 0)
            eventTimeDur = 0;
        else
            eventTimeDur = eventTime - lastEventTime;

        if(lastSysTime == 0)
            sysTimeDur = 0;
        else
            sysTimeDur = sysTime - lastSysTime;

        //get bounds, source name and view resource id
        Rect boundsInParent = new Rect();
        Rect boundsInScreen = new Rect();

        String sourceName = source.getClassName().toString();
        source.getBoundsInParent(boundsInParent);
        source.getBoundsInScreen(boundsInScreen);

        String viewResourceId = getViewResourceId(source);
        StringBuilder pBounds = new StringBuilder();
        StringBuilder sBounds = new StringBuilder();

        pBounds.append(String.format("%s, %s, %s, %s", boundsInParent.top, boundsInParent.left, boundsInParent.bottom, boundsInParent.right));
        sBounds.append(String.format("%s, %s, %s, %s", boundsInScreen.top, boundsInScreen.left, boundsInScreen.bottom, boundsInScreen.right));

        ContentValues values = new ContentValues();
        values.put(DBcontract.LogEntry.COLUMN_EVENT_TYPE, type);
        values.put(DBcontract.LogEntry.COLUMN_APP_NAME, pkgName);
        values.put(DBcontract.LogEntry.COLUMN_ACTIVITY_NAME, activityName);
        values.put(DBcontract.LogEntry.COLUMN_EVENT_TIME, eventTime);
        values.put(DBcontract.LogEntry.COLUMN_EVENT_TIME_DURATION,eventTimeDur);
        values.put(DBcontract.LogEntry.COLUMN_SYS_TIME, sysTime);
        values.put(DBcontract.LogEntry.COLUMN_SYS_TIME_DURATION, sysTimeDur);
        values.put(DBcontract.LogEntry.COLUMN_WINDOW_ID, windowID);
        values.put(DBcontract.LogEntry.COLUMN_SOURCE_CLASS, sourceName);
        values.put(DBcontract.LogEntry.COLUMN_VIEW_RESOURCE_ID, viewResourceId);
        values.put(DBcontract.LogEntry.COLUMN_BOUNDS_IN_PARENT, pBounds.toString());
        values.put(DBcontract.LogEntry.COLUMN_BOUNDS_IN_SCREEN, sBounds.toString());
        values.put(DBcontract.LogEntry.COLUMN_WINDOW_INFO, windowInfo);
        values.put(DBcontract.LogEntry.COLUMN_IS_PASSWORD, isPass);
        values.put(DBcontract.LogEntry.COLUMN_REMOVED_COUNT, removeCount);
        values.put(DBcontract.LogEntry.COLUMN_ADDED_COUNT, addCount);
        db.insert(DBcontract.LogEntry.TABLE_INTERACTION_INFO, null, values);
        lastEventTime = eventTime;
        lastSysTime = sysTime;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        DBhelper helper = new DBhelper(this);
        db = helper.getWritableDatabase();
    }

    public static void setLogEnable(){ logEnable = true; }

    public static void setLogDisable(){ logEnable = false; }

    public static Boolean isLogEnable(){ return logEnable; }

    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                return "TYPE_VIEW_HOVER_ENTER";
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                return "TYPE_VIEW_HOVER_EXIT";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE_WINDOW_CONTENT_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                return "TYPE_VIEW_SCROLLED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                return "TYPE_TOUCH_INTERACTION_START";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                return "TYPE_TOUCH_INTERACTION_END";
        }
        return "OTHER_EVENT";
    }

    protected void recycle(StringBuilder sb, AccessibilityNodeInfo info, int level) {
        if (info == null) return;
        String levelInfo = generateLevel(level);
        sb.append(levelInfo);
        sb.append("[ClassName] "+ info.getClassName()+ " [ViewId] "+this.getViewResourceId(info)+
                " [Text] "+ info.getText() + " [WINDOW_ID] "+ info.getWindowId() + " [WINDOW_TYPE] " + info.getWindow().getType());
        sb.append("\n");
        Log.i(TAG, levelInfo + "[ClassName] " + info.getClassName() + " [ViewId] " + this.getViewResourceId(info) +
                " [Text] " + info.getText() + " [WINDOW_ID] " + info.getWindowId() + "[WINDOW_TYPE] " + info.getWindow().getType());

        ContentValues values = new ContentValues();
        values.put(DBcontract.LogEntry.COLUMN_LEVEL, level);
        values.put(DBcontract.LogEntry.COLIMN_CLASS_NAME, String.valueOf(info.getClassName()));
        values.put(DBcontract.LogEntry.COLUMN_VIEW_ID, this.getViewResourceId(info));
        values.put(DBcontract.LogEntry.COLUMN_WIDGET_TEXT, String.valueOf(info.getText()));
        values.put(DBcontract.LogEntry.COLUMN_WINDOW_ID, info.getWindowId());
        values.put(DBcontract.LogEntry.COLUMN_WINDOW_TYPE, info.getWindow().getType());
        db.insert(DBcontract.LogEntry.TABLE_SOURCE_INFO, null, values);

        if(info.getChildCount()!=0){
            for (int i = 0; i < info.getChildCount(); i++) {
                if(info.getChild(i)!=null){
                    recycle(sb, info.getChild(i), level + 1);
                }
            }
        }
    }

    protected String getViewResourceId(AccessibilityNodeInfo info){
        String viewResourceId = "";
        if(Build.VERSION.SDK_INT >=18){
            viewResourceId = info.getViewIdResourceName();
        }
        return viewResourceId;
    }

    private String generateLevel(int level){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < level; ++i){
            sb.append("--");
        }

        return sb.toString();
    }
}
