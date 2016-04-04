package com.das.yacalendar.calendar;

import android.graphics.Bitmap;
import android.util.Log;

import com.das.yacalendar.Constants;
import com.das.yacalendar.notes.Note;
import com.das.yacalendar.yacalendar;

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
    public int numMonths = 0;
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
        this.startDate.setTime(yacalendar.parseDate(startDate, Constants.SHORT_DATE_FORMAT));
        this.endDate.setTime(yacalendar.parseDate(endDate, Constants.SHORT_DATE_FORMAT));
        Log.d(TAG, "startDate = " + this.startDate.toString());
        Log.d(TAG, "endDate = " + this.endDate.toString());

        int diffYear = this.endDate.get(Calendar.YEAR) - this.startDate.get(Calendar.YEAR);
        //month is 0 based, full year is 11
        numMonths = diffYear * 12 + this.endDate.get(Calendar.MONTH) - this.startDate.get(Calendar.MONTH);
        //fill the monthImage array with null's so we can add the images in any order
        for(int index = 0; index < numMonths+1; index++)
        {
            monthImage.add(null);
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
