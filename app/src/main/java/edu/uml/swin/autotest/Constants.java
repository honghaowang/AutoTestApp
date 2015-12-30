package edu.uml.swin.autotest;

/**
 * Created by honghao on 12/27/2015.
 */
public class Constants {
    public static String POST_FILE_URL = "http://swin06.cs.uml.edu/sleepfit/processdata/autotest";
    public static String FILE_NAME;
    public Constants(String deviceID){
        FILE_NAME = deviceID + ".db";
    }
}
