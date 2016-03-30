package com.das.yacalendar;

/**
 * Created by yaturner on 3/20/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Helper class for DB creation/updating
public class DBHelper extends SQLiteOpenHelper
{
    // Your local name for the schema
    private static final String DB_NAME = "das_calendar";
    private static DBHelper sInstance;
    private SQLiteDatabase database;
    private int databaseOpenRefCount = 0;
    private final static String[] DELETE_STATEMENTS =
            {
                    "DROP TABLE IF EXISTS " + CalendarContract.NOTE_TABLE_NAME,
                    "DROP TABLE IF EXISTS " + CalendarContract.IMAGES_TABLE_NAME,
                    "DROP TABLE IF EXISTS " + CalendarContract.INFO_TABLE_NAME
            };

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, Constants.CURRENT_DB_VERSION);
    }

    /**
     * Creates any of our tables.
     * This function is only run once or after every Clear Data
     */
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        try
        {
//            database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS " + CalendarContract.LOCAL_TABLE_NAME + "(" +
//                            CalendarContract.CalendarEntry._ID + " integer primary key autoincrement," +
//                            CalendarContract.CalendarEntry.COLUMN_NAME_PRIORITY + " INTEGER" +
//                            CalendarContract.CalendarEntry.COLUMN_NAME_NOTE + " TEXT," +
//                            CalendarContract.CalendarEntry.COLUMN_NAME_DATE + "TEXT" +
//                            ");");

//            database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS " + CalendarContract.REMOTE_TABLE_NAME + "(" +
//                            CalendarContract.CalendarEntry._ID + " integer primary key autoincrement," +
//                            CalendarContract.CalendarEntry.COLUMN_NAME_PRIORITY + " INTEGER" +
//                            CalendarContract.CalendarEntry.COLUMN_NAME_NOTE + " TEXT," +
//                            CalendarContract.CalendarEntry.COLUMN_NAME_DATE + "TEXT" +
//                            ");");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + CalendarContract.INFO_TABLE_NAME + "(" +
                            CalendarContract.CalendarInfoEntry._ID + " integer primary key autoincrement," +
                            CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_NPO_NAME + "TEXT," +
                            CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_CALENDAR_VERSION + "INTEGER," +
                            CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_START_DATE + "TEXT," +
                            CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_END_DATE + "TEXT," +
                            ");");
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS "+ CalendarContract.IMAGES_TABLE_NAME + "(" +
                            CalendarContract.CalendarImagesEntry.COLUMN_NAME_IMAGE_ID + "INTEGER," +
                            CalendarContract.CalendarImagesEntry.COLUMN_NAME_IMAGE + "BLOB," +
                            ");");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized SQLiteDatabase openDatabase()
    {
        if (databaseOpenRefCount == 0)
        {
            databaseOpenRefCount++;
            database = this.getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase()
    {
        databaseOpenRefCount--;
        if (databaseOpenRefCount == 0)
        {
            database.close();
        }
    }

    public void clearDatabase(final String which_table)
    {
        SQLiteDatabase db = openDatabase();
        int index = which_table.equals(Constants.LOCAL_TABLE) ? 0 : 1;

        database.execSQL(DELETE_STATEMENTS[index]);
    }

    /**
     * addNote
     *
     * @param note users can only add notes to the local database
     */
    public long addNote(final Note note)
    {
        SQLiteDatabase db = this.openDatabase();

        if (note != null)
        {
            ContentValues values = new ContentValues();

            values.put(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_PRIORITY, note.getPriority());
            values.put(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_TEXT, note.getText());
            values.put(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_DATE, note.getDateAsString());

            long note_id = db.insertOrThrow(CalendarContract.INFO_TABLE_NAME, null, values);

            return note_id;
        }
        else
        {
            return -1L;
        }
    }

    public Note getNote(final int id)
    {
        SQLiteDatabase db = this.openDatabase();

        Cursor c =  db.query(CalendarContract.NOTE_TABLE_NAME, null, "_id=" + id, null, null, null, null);

        if(c.getCount() == 0)
        {
            return null;
        }

        Calendar date = Calendar.getInstance();

        c.moveToFirst();
        int _id = c.getInt(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_ID));
        int priority = c.getInt(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_PRIORITY));
        String text = c.getString(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_TEXT));
        String dateString = c.getString(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_DATE));

        try
        {
            date.setTime(yacalendar.getInstance().getSdf().parse(dateString));
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }

        Note note = new Note(_id, date, priority, text);

        return note;
    }

    /**
     * addNote
     *
     * @param note users can only add notes to the local database
     */
    public long addInfo()
    {
        SQLiteDatabase db = this.openDatabase();

//        if (note != null)
//        {
//            ContentValues values = new ContentValues();
//
//            values.put(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_PRIORITY, note.getPriority());
//            values.put(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_TEXT, note.getText());
//            values.put(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_DATE, note.getDateAsString());
//
//            long note_id = db.insertOrThrow(CalendarContract.INFO_TABLE_NAME, null, values);
//
//            return note_id;
//        }
//        else
//        {
            return -1L;
//        }
    }

    public CalendarInfo getInfo(final int id)
    {
        SQLiteDatabase db = this.openDatabase();

//        Cursor c =  db.query(CalendarContract.NOTE_TABLE_NAME, null, "_id=" + id, null, null, null, null);
//
//        if(c.getCount() == 0)
//        {
//            return null;
//        }
//
//        Calendar date = Calendar.getInstance();
//
//        c.moveToFirst();
//        int _id = c.getInt(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_ID));
//        int priority = c.getInt(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_PRIORITY));
//        String text = c.getString(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_TEXT));
//        String dateString = c.getString(c.getColumnIndex(CalendarContract.CalendarNoteEntry.COLUMN_NAME_NOTE_DATE));
//
//        try
//        {
//            date.setTime(yacalendar.getInstance().getSdf().parse(dateString));
//        }
//        catch(ParseException e)
//        {
//            throw new RuntimeException(e);
//        }
//
//        Note note = new Note(_id, date, priority, text);
//
//        return note;

        return null;
    }




    /**
     * Handle all database version upgrades
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.beginTransaction();
        boolean okay = true;


        /**
         * Check if a table exists. Useful for new table creation in onUpgrade
         */
    }

    private boolean checkTable(SQLiteDatabase pDatabase, String pTable)
    {
        try
        {
            Cursor c = pDatabase.query(pTable, null, null, null, null, null, null);
            if (c == null)
            {
                return false;
            }
            c.close();
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * Return current date as a string
     */
    public static String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        return sdf.format(date);
    }

    private String buildSQLStatementString(String start, String tableName, String[] fields)
    {
        StringBuilder sql = new StringBuilder(start + " INTO " + tableName + " (");

        int fieldsCount = fields.length;
        for (int i = 0; i < fieldsCount; i++)
        {
            sql.append(fields[i]);
            if (i < fieldsCount - 1)
            {
                sql.append(",");
            }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < fieldsCount; i++)
        {
            sql.append("?");
            if (i < fieldsCount - 1)
            {
                sql.append(", ");
            }
        }
        sql.append(");");

        return sql.toString();
    }

}