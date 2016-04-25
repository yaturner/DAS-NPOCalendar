package com.das.yacalendar;

/**
 * Created by yaturner on 3/20/2015.
 */
public class Constants
{
    public final static int CURRENT_DB_VERSION = 2;

    public final static String SHORT_DATE_FORMAT = "M/d/yyyy";
    public final static String LONG_DATE_FORMAT = "M/d/yyyy h:m a";
    public final static String INTERNAL_SHORT_DATE_FORMAT = "Mdyyyy h:m";
    public final static String DATABASE_SHORT_DATE_FORMAT = "yyyyMMdd hh:mm";
    public final static String DAY_DATE_FORMAT = "MMddyyyy";

    //Note
    public static final int NOTE_PRIORITY_NONE = -1;
    public static final int NOTE_PRIORITY_LOW = 0;
    public static final int NOTE_PRIORITY_MEDIUM = 1;
    public static final int NOTE_PRIORITY_HIGH = 2;

    public final static String SERVER_ADDRESS = "http://celtic-current-96313.appspot.com";
    public final static String SERVER_SPLASH_URI = SERVER_ADDRESS + "/getSplash?npo=das";
    public final static String SERVER_IMAGE_NAMES_URI = SERVER_ADDRESS + "/getImageNames?npo=das";
    public final static String SERVER_IMAGE_URI = SERVER_ADDRESS + "/getImage?npo=das&image=";
    public final static String SERVER_NOTES_URI = SERVER_ADDRESS + "/getNotes?npo=das";

    public final static int HANDLER_MESSAGE_INFO = 101;
    public final static int HANDLER_MESSAGE_NOTES = 102;
    public final static int HANDLER_MESSAGE_SPLASH_IMAGE = 103;
    public final static int HANDLER_MESSAGE_IMAGE = 104;
    public final static int HANDLER_MESSAGE_NEW_NOTE = 105;
    public final static int HANDLER_MESSAGE_IMAGE_NAMES = 106;


}
