package com.das.yacalendar.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.das.yacalendar.DateButton;
import com.das.yacalendar.R;
import com.das.yacalendar.database.DBHelper;
import com.das.yacalendar.notes.Note;
import com.das.yacalendar.yacalendar;
import com.google.common.collect.ArrayListMultimap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by yaturner on 3/29/2016.
 */
public class GridCellAdapter extends BaseAdapter  {
    private static final String TAG = GridCellAdapter.class.getSimpleName();

    private Context context;

    private List<String> list;
    private static final int DAY_OFFSET = 1;
    private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
            "Wed", "Thu", "Fri", "Sat"};
    private final String[] months = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31};
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private DateButton gridcell;
    private TextView num_events_per_day;
    private ArrayListMultimap<Integer, Note> eventsPerMonthMap;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd-MMM-yyyy");

    // Days in Current Month
    public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
        super();
        this.context = context;
        this.list = new ArrayList<String>();
        Log.d(TAG, "==> Passed in Date FOR Month: " + month + " "
                + "Year: " + year);
        Calendar calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        Log.d(TAG, "New Calendar:= " + calendar.getTime().toString());
        Log.d(TAG, "CurrentDayOfWeek :" + getCurrentWeekDay());
        Log.d(TAG, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

// Print Month
        printMonth(month, year);

// Find Number of Events
        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
    }

    public String getMonthAsString(int i) {
        return months[i];
    }

    public String getWeekDayAsString(int i) {
        return weekdays[i];
    }

    public int getNumberOfDaysOfMonth(int index) {
        return daysOfMonth[index];
    }

    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Prints Month
     *
     * @param mm
     * @param yy
     */
    private void printMonth(int mm, int yy) {
        Log.d(TAG, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = mm - 1;
        String currentMonthName = getMonthAsString(currentMonth);
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        Log.d(TAG, "Current Month: " + " " + currentMonthName + " having "
                + daysInMonth + " days.");

        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
        Log.d(TAG, "Gregorian Calendar:= " + cal.getTime().toString());

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
            Log.d(TAG, "*->PrevYear: " + prevYear + " PrevMonth:"
                    + prevMonth + " NextMonth: " + nextMonth
                    + " NextYear: " + nextYear);
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
            Log.d(TAG, "**-> PrevYear: " + prevYear + " PrevMonth:"
                    + prevMonth + " NextMonth: " + nextMonth
                    + " NextYear: " + nextYear);
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            Log.d(TAG, "***—> PrevYear: " + prevYear + " PrevMonth:"
                    + prevMonth + " NextMonth: " + nextMonth
                    + " NextYear: " + nextYear);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        Log.d(TAG, "Week Day:" + currentWeekDay + " is "
                + getWeekDayAsString(currentWeekDay));
        Log.d(TAG, "No. Trailing space to Add: " + trailingSpaces);
        Log.d(TAG, "No. of Days in Previous Month: " + daysInPrevMonth);

        if (cal.isLeapYear(cal.get(Calendar.YEAR)))
            if (mm == 2)
                ++daysInMonth;
            else if (mm == 3)
                ++daysInPrevMonth;

// Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            Log.d(TAG,
                    "PREV MONTH:= "
                            + prevMonth
                            + " => "
                            + getMonthAsString(prevMonth)
                            + " "
                            + String.valueOf((daysInPrevMonth
                            - trailingSpaces + DAY_OFFSET)
                            + i));
            list.add(String
                    .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                            + i)
                    + "-GREY"
                    + "-"
                    + getMonthAsString(prevMonth)
                    + "-"
                    + prevYear);
        }

// Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            Log.d(currentMonthName, String.valueOf(i) + " "
                    + getMonthAsString(currentMonth) + " " + yy);
            //TODO only do this if we are in the month being shown
            Calendar now = Calendar.getInstance();
            if (i == getCurrentDayOfMonth() && currentMonth == now.get(Calendar.MONTH)) {
                list.add(String.valueOf(i) + "-ORANGE" + "-"
                        + getMonthAsString(currentMonth) + "-" + yy);
            } else {
                list.add(String.valueOf(i) + "-BLACK" + "-"
                        + getMonthAsString(currentMonth) + "-" + yy);
            }
        }
// Leading Month days
        for (int i = 0; i < list.size() % 7; i++) {
            Log.d(TAG, "NEXT MONTH:= " + getMonthAsString(nextMonth));
            list.add(String.valueOf(i + 1) + "-GREY" + "-"
                    + getMonthAsString(nextMonth) + "-" + nextYear);
        }
    }

    /**
     * Given the YEAR, MONTH, retrieve
     * ALL entries from a SQLite database for that month. Iterate over the
     * List of All entries, and get the dateCreated, which is converted into
     * day.
     *
     * @param year
     * @param month
     * @return
     */
    private ArrayListMultimap<Integer, Note> findNumberOfEventsPerMonth(int year, int month) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getNotesForMonth(month, year);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.day, parent, false);
        }

        // Get a reference to the Day gridcell
        gridcell = (DateButton) row.findViewById(R.id.BtnDay);

        // ACCOUNT FOR SPACING

        Log.d(TAG, "Current Day: " + getCurrentDayOfMonth());
        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];

        // Set the Day GridCell
        gridcell.setText(theday);

        // Set the item attributes

        if (day_color[1].equals("GREY")) {
            gridcell.setTextColor(context.getResources()
                    .getColor(android.R.color.darker_gray));
            gridcell.setTag(null);
        }
        else if (day_color[1].equals("BLACK")) {
            gridcell.setTextColor(context.getResources().getColor(
                    android.R.color.black));
            List<Note> notes = eventsPerMonthMap.get(Integer.parseInt(theday));
            gridcell.setTag(notes);
            Log.d(TAG, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);
            //check for a day image
            File day = new File(yacalendar.IMAGES_DIR+"/day04232016.jpg");
            if(day.exists())
            {

            }
        }
        else if (day_color[1].equals("ORANGE")) {
            gridcell.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
            gridcell.setTypeface(null, Typeface.BOLD);
            List<Note> notes = eventsPerMonthMap.get(Integer.parseInt(theday));
            gridcell.setTag(notes);
            Log.d(TAG, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);
        }
        return row;
    }

    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    public int getCurrentWeekDay() {
        return currentWeekDay;
    }
}
