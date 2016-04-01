package com.das.yacalendar.network;

import android.os.Message;

import com.das.yacalendar.Constants;
import com.das.yacalendar.notes.Note;
import com.das.yacalendar.yacalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yaturner on 4/2/2015.
 */
public class NotesServerCall extends BasicAPICall
{

    public NotesServerCall(final yacalendar main)
    {
        super(main);
    }

    @Override
    protected Void doInBackground(String... params)
    {
       return super.doInBackground(params);
    }

    @Override
    protected void parseResult(final String result)
    {
        ArrayList<Note> notes = new ArrayList<Note>();

        if (result != null && result.length() > 0)
        {
            try
            {
                JSONObject obj = new JSONObject(result);
                JSONArray days = obj.getJSONArray("days");
                for (int iDay = 0; iDay < days.length(); iDay++)
                {
                    JSONObject day = days.getJSONObject(iDay);
                    Date date = yacalendar.parseDate(day.getString("Date"), Constants.INTERNAL_SHORT_DATE_FORMAT);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    Note note = new Note(day.getInt("id"), cal, day.getInt("priority"),
                            day.getString("note"));
                    notes.add(note);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Message msg = main.msgHandler.obtainMessage(main.kMessageNotes);
        msg.obj = (Object) notes;
        main.msgHandler.sendMessage(msg);

    }

}

