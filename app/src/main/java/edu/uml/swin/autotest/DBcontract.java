package edu.uml.swin.autotest;

import android.provider.BaseColumns;

/**
 * Created by honghao on 12/27/2015.
 */
public class DBcontract {
    public DBcontract(){}

    public static abstract class LogEntry implements BaseColumns {
        public static final int dataBaseVersion = 1;
        public static final String dataBaseFile = "AutoTest.db";
        public static final String dataBaseName = "AutoTest";
        //Table name
        public static final String TABLE_TASK_LIST = "Task_List";
        public static final String TABLE_BASIC_INFO = "Basic_Information";
        public static final String TABLE_APP_INFO = "APP_Information";
        public static final String TABLE_ACTIVITY_INFO = "Activity_Information";
        public static final String TABLE_INTERACTION_INFO = "Interaction_Information";
        public static final String TABLE_SENSOR_INFO = "Sensor_Information";
        public static final String TABLE_CONTEXT_INFO = "Context_Information";

        //Attribute in table TASK LIST
        public static final String COLUMN_TASK_ID = "task_id";
        public static final String COLUMN_APP_NAME = "app_name";
        public static final String COLUMN_TASK_NAME = "task_name";
        public static final String COLUMN_TASK_DESCRIPTION = "task_description";

        //Attribute in table BASIC INFO
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_DEVICE_ID = "device_id";
        //COLUMN_APP_NAME & COLUMN_TASK_NAME from TASK LIST
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_TASK_DURATION = "task_duration";
        public static final String COLUMN_TASK_STATE = "task_state";

        //Attribute in table APP INFO
        public static final String COLUMN_ACTIVITY_ID = "activity_id";
        //COLUMN_APP_NAME from TASK LIST
        public static final String COLUMN_ACTIVITY_NAME = "activity_name";
        public static final String COLUMN_WIDGET_COUNT = "widget_count";

        //Attribute in table ACTIVITY INFO
        public static final String COLUMN_WIDGET_ID = "widget_id";
        //COLUMN_ACTIVITY_ID from APP INFO
        public static final String COLUMN_WIDGET_NAME = "widget_name";
        public static final String COLUMN_WIDGET_TYPE = "widget_type";
        public static final String COLUMN_WIDGET_TEXT = "text";
        public static final String COLUMN_LOCATION_X = "location_x";
        public static final String COLUMN_LOCATION_Y = "location_y";
        public static final String COLUMN_LEVEL = "level";

        //Attribute in table INTERACTION RECORDER
        public static final String COLUMN_INTERACTION_ID = "interaction_id";
        //COLUMN_ID from BASIC INFO
        public static final String COLUMN_EVENT_TYPE = "event_type";
        public static final String COLUMN_SCROLL_X = "scroll_x";
        public static final String COLUMN_SCROLL_Y = "scroll_y";
        public static final String COLUMN_EVENT_TIME = "event_time";
        public static final String COLUMN_SYS_TIME = "system_time";
        public static final String COLUMN_EVENT_TIME_DURATION = "event_time_dur";
        public static final String COLUMN_SYS_TIME_DURATION = "system_time_dur";
        public static final String COLUMN_WINDOW_ID = "window_id";
        public static final String COLUMN_SOURCE_CLASS = "source_class";
        public static final String COLUMN_VIEW_RESOURCE_ID = "view_resource_id";
        public static final String COLUMN_BOUNDS_IN_PARENT = "bound_in_parent";
        public static final String COLUMN_BOUNDS_IN_SCREEN = "bound_in_screen";
        public static final String COLUMN_WINDOW_INFO = "window_info";
        public static final String COLUMN_IS_PASSWORD = "is_password";
        public static final String COLUMN_REMOVED_COUNT = "removed_count";
        public static final String COLUMN_ADDED_COUNT = "add_count";

        //Attribute in table SENSOR INFO
        public static final String COLUMN_SENSOR_RECORD_ID = "sensor_record_id";
        //COLUMN_DEVICE_ID from table BASIC INFO
        public static final String COLUMN_ACCELEROMETER_X = "accelerometer_x";
        public static final String COLUMN_ACCELEROMETER_Y = "accelerometer_y";
        public static final String COLUMN_ACCELEROMETER_Z = "accelerometer_z";
        public static final String COLUMN_GYROSCOPE_X = "gyroscope_x";
        public static final String COLUMN_GYROSCOPE_Y = "gyroscope_y";
        public static final String COLUMN_GYROSCOPE_Z = "gyroscope_z";
        public static final String COLUMN_RECORD_TIME = "record_time";

        //Attribute in table CONTEXT INFO
        public static final String COLUMN_RECORD_ID = "record_id";
        //COLUMN_DEVICE_ID from table BASIC INFO
        public static final String COLUMN_BATTERY_STATE = "battery_state";
        public static final String COLUMN_NETWORK_STATE = "network_state";
        public static final String COLUMN_LIGHT_LEVEL = "light_level";
        //public static final String COLUMN_RECORD_TIME = "record_time";
    }
}
