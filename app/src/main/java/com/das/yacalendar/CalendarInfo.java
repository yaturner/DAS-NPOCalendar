package com.das.yacalendar;

import android.graphics.Bitmap;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by yaturner on 4/2/2015.
 */
public class CalendarInfo
{
    private final static String TAG = "CalendarInfo";

    public int version;
    public Calendar startDate = null;
    public Calendar endDate = null;
    // Notes (for each day of the any year).
    private TreeMap<Calendar, Note> notes = null;
    private Bitmap splashImage = null;
    private ArrayList<Bitmap> monthImage = null;

    public CalendarInfo(final int version, final String startDate, final String endDate)
    {
        this.startDate = Calendar.getInstance();
        this.endDate = Calendar.getInstance();
        this.monthImage = new ArrayList<Bitmap>();
        this.version = version;
        this.startDate.setTime(yacalendar.parseDate(startDate, Constants.INTERNAL_SHORT_DATE_FORMAT));
        this.endDate.setTime(yacalendar.parseDate(endDate, Constants.INTERNAL_SHORT_DATE_FORMAT));
        Log.d(TAG, "startDate = " + this.startDate.toString());
        Log.d(TAG, "endDate = " + this.endDate.toString());
        int diffYear = this.endDate.get(Calendar.YEAR) - this.startDate.get(Calendar.YEAR);
        //month is 0 based, full year is 11
        int diffMonth = diffYear * 12 + this.endDate.get(Calendar.MONTH) - this.startDate.get(Calendar.MONTH);
        //fill the monthImage array with null's so we can add the images in any order
        for(int index = 0; index < diffMonth+1; index++)
        {
            monthImage.add(null);
        }

        notes = new TreeMap<Calendar, Note>(new Comparator<Calendar>()
        {
            public int compare(Calendar lhs, Calendar rhs)
            {
                if (lhs.before(rhs))
                {
                    return -1;
                } else if (lhs.after(rhs))
                {
                    return 1;
                } else
                {
                    return 0;
                }
            }
        });
    }

    public TreeMap<Calendar, Note> getNotes()
    {
        return notes;
    }

    public void setNotes(TreeMap<Calendar, Note> notes)
    {
        this.notes = notes;
    }

    public Note getNote(final Calendar date)
    {
        if (notes != null)
        {
            return notes.get(date);
        } else
        {
            return null;
        }
    }

    public void setNote(final Note note)
    {
        if (note != null)
        {
            notes.put(note.getDate(), note);
        }
    }

    public final SortedMap<Calendar, Note> getNoteFromTo(final Calendar from, final Calendar to)
    {
        if(notes != null && notes.size() > 0)
        {
            return notes.subMap(from, to);
        }
        else
        {
            return null;
        }
    }

    public void addMonthImage(final int month, final Bitmap monthImage)
    {
        this.monthImage.add(month, monthImage);
    }

    public Bitmap getMonthImage(final int month)
    {
        if(month > -1 && month < monthImage.size())
        {
            return monthImage.get(month);
        }
        else
        {
            return null;
        }
    }

    public Bitmap getSplashImage()
    {
        return this.splashImage;
    }

    public void setSplashImage(Bitmap splashImage)
    {
        this.splashImage = splashImage;
    }

    public Calendar getStartDate()
    {
        return startDate;
    }


    public Calendar getEndDate()
    {
        return endDate;
    }


}
