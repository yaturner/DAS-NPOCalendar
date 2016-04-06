package com.das.yacalendar;

/**
 * Created by yaturner on 3/20/2015.
 */
public class Constants
{
    public final static int CURRENT_DB_VERSION = 2;

    public final static String LOCAL_TABLE = "local";
    public final static String REMOTE_TABLE = "remote";
    public final static String[] TABLE_NAMES = {LOCAL_TABLE, REMOTE_TABLE};

    public final static String SHORT_DATE_FORMAT = "M/d/yyyy";
    public final static String LONG_DATE_FORMAT = "M/d/yyyy h:m";
    public final static String INTERNAL_SHORT_DATE_FORMAT = "Mdyyyy h:m";
    public final static String DATABASE_SHORT_DATE_FORMAT = "yyyyMMdd hh:mm";

    //Note
    public static final int NOTE_PRIORITY_NONE = -1;
    public static final int NOTE_PRIORITY_LOW = 0;
    public static final int NOTE_PRIORITY_MEDIUM = 1;
    public static final int NOTE_PRIORITY_HIGH = 2;

    public final static String SERVER_ADDRESS = "http://192.168.0.54:8081";
}
