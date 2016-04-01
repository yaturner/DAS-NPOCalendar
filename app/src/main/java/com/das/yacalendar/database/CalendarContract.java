package com.das.yacalendar.database;

import android.provider.BaseColumns;

/**
 * Created by yaturner on 3/22/2015.
 */
public class CalendarContract
{
//    public final static String LOCAL_TABLE_NAME = "local_calendar";
//    public final static String REMOTE_TABLE_NAME = "remote_calendar";
    public final static String INFO_TABLE_NAME = "calendar_info";
    public final static String NOTE_TABLE_NAME = "calendar_note";
    public final static String IMAGES_TABLE_NAME = "calendar_images";

    public CalendarContract()
    {
    }

    public static abstract class CalendarNoteEntry implements BaseColumns
    {
        public final static String COLUMN_NAME_NOTE_ID = "noteId";
        public final static String COLUMN_NAME_NOTE_PRIORITY = "priority";
        public final static String COLUMN_NAME_NOTE_TEXT = "note";
        public final static String COLUMN_NAME_NOTE_DATE = "date";

    }

    public static abstract class CalendarInfoEntry implements BaseColumns
    {
        public final static String COLUMN_NAME_INFO_NPO_NAME = "npo_name";
        public final static String COLUMN_NAME_INFO_CALENDAR_VERSION = "version";
        public final static String COLUMN_NAME_INFO_START_DATE = "start_date";
        public final static String COLUMN_NAME_INFO_END_DATE = "end_date";
    }
    public static abstract class CalendarImagesEntry implements BaseColumns
    {
        public final static String COLUMN_NAME_IMAGE_ID = "imageId";
        public final static String COLUMN_NAME_IMAGE = "image";
    }
}
