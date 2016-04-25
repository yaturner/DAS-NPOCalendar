/*********************************************************************************
 *                                                                               *
 * D'arc Angel CONFIDENTIAL                                                      *
 * __________________                                                            *
 *                                                                               *
 *  [2010] D'arc Angel LLC                                                       *
 *  All Rights Reserved.                                                         *
 *                                                                               *
 * NOTICE:  All information contained herein is, and remains                     *
 * the property of D'arc Angel Software LLC and its suppliers,                   *
 * if any.  The intellectual and technical concepts contained                    *
 * herein are proprietary to D'arc Angel Software LLC                            *
 * and its suppliers and may be covered by U.S. and Foreign Patents,             *
 * patents in process, and are protected by trade secret or copyright law.       *
 * Dissemination of this information or reproduction of this material            *
 * is strictly forbidden unless prior written permission is obtained             *
 * from D'arc Angel Software LLC.                                                *
 *********************************************************************************/

package com.das.yacalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.das.yacalendar.adapters.GridCellAdapter;
import com.das.yacalendar.adapters.NotesListItemAdapter;
import com.das.yacalendar.calendar.CalendarInfo;
import com.das.yacalendar.database.CalendarContract;
import com.das.yacalendar.database.DBHelper;
import com.das.yacalendar.database.DatabaseVersion;
import com.das.yacalendar.dialog.AddNoteDialog;
import com.das.yacalendar.dialog.CancelWarningDialog;
import com.das.yacalendar.listeners.CalendarGestureListener;
import com.das.yacalendar.listeners.MonthNameTouchListener;
import com.das.yacalendar.listeners.SlideInAnimationListener;
import com.das.yacalendar.listeners.SlideOutAnimationListener;

import com.das.yacalendar.network.ImageInfo;
import com.das.yacalendar.network.InfoServerCall;
import com.das.yacalendar.network.ImageNamesServerCall;
import com.das.yacalendar.network.ImageServerCall;
import com.das.yacalendar.network.NotesServerCall;
import com.das.yacalendar.network.SplashServerCall;
import com.das.yacalendar.notes.Note;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class yacalendar extends FragmentActivity
{
    private static yacalendar singleton;

    final static public boolean SUPPORT_NOTE_PRIORITY = false;

    final static private String TAG = yacalendar.class.getSimpleName();

    final static private String SAVEFILENAME = "test.data";
    // Menu Ids
    // Month View menu
    private static final int MENU_DAY_VIEW = Menu.FIRST + 1;
    private static final int MENU_SETTINGS = Menu.FIRST + 2;
    private static final int MENU_HELP = Menu.FIRST + 3;
    private static final int MENU_EXIT = Menu.FIRST + 4;

    // Day View Menu
    private static final int MENU_CANCEL = Menu.FIRST + 5;
    private static final int MENU_DONE = Menu.FIRST + 6;

    //total number of strings for the day view, if the note has less than this number
    // then the view is padded with empty strings
    private static final int NUMBER_OF_NOTE_STRINGS = 6;

    public static String IMAGES_DIR;

    public Animation mSlideLeftIn = null;
    public Animation mSlideLeftOut = null;
    public Animation mSlideRightIn = null;
    public Animation mSlideRightOut = null;
    public Animation mSlideDownOut = null;
    public Animation mSlideUpIn = null;

    // The currently selected month and year
    public int mCurrentMonthIndex;
    public int mCurrentYear;
    public int mCurrentDay;
    public Calendar mStartDate;
    public Calendar mEndDate;

    public View mCurrentMonthView = null;
    //Currently display calendar (month)
    public Calendar mCalendar = null;

    private GridCellAdapter adapter = null;

    //Cached layouts
    RelativeLayout mMonthView1 = null;
    RelativeLayout mMonthView2 = null;
    RelativeLayout mMainScreen = null;
    RelativeLayout mHelpScreen = null;
    RelativeLayout mDayViewScreen = null;
    private int dayOfMonthId[];
    private int mMonthNameStringId[] =
            {R.string.month00, R.string.month01, R.string.month02, R.string.month03, R.string.month04, R.string.month05,
                    R.string.month06, R.string.month07, R.string.month08, R.string.month09, R.string.month10,
                    R.string.month11,};
    private int mWeekId[] = null;
    private int mDayId[] = null;
    private int monthBackgroundId[] = null;

    private DateButton mCurrentDateBtn = null;

    private Drawable splashImage = null;

    //	private OnFocusChangeListener mCalendarFocusChangeListener = null;
    private ViewFlipper mMonthViewFlipper = null;
    private ViewFlipper mHelpViewFlipper = null;
    //Context menu
    private AdapterView.AdapterContextMenuInfo mContextMenuInfo = null;
    //Day View
    private int mNoteListSelectedItemIndex = -1;
    private ArrayList<String> mCurrentNotesList = null;
    private ArrayList<String> monthImageNames = null;
    private NotesListItemAdapter mNotesListItemAdapter = null;
    //This method handles the direction arrows for the month name
    private OnTouchListener mMonthNameTouchListener = null;
    //This method handles fling gestures for the main screen
    private GestureDetector mCalendarGestureDetector = null;
    private View.OnTouchListener mCalendarTouchListener = null;
    //This handles scrolling events for the footer to keep them from
    // being handled by the mCalendarTouchListener
    private View.OnTouchListener mFooterTouchListener = null;
    // Edit Note Dialog
    private Dialog mEditNoteDialog = null;
    private View mEditNoteView = null;
    // Warning for canceling edit note view without saving
    private Dialog mWarningCancelDialog = null;
    private View mWarningCancelView = null;
    // Help Screen Flipper ( from main <--> help )
    private View mMainFlipImage;
    private View mHelpFlipImage;
    private boolean isMainFlipImage = true;
    private CalendarInfo calendarInfo = null;
    private ProgressDialog progressDialog = null;
    public Handler msgHandler = null;

    //Bundle keys
    public static final String KEY_DATE = "date";


    public DBHelper dbHelper = null;
    public int currentCalendarVersion = -1;

    //Singleton access
    public static yacalendar getInstance()
    {
        return singleton;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
//		Debug.startMethodTracing( "yaCalendar" );

        super.onCreate(savedInstanceState);
        singleton = this;

        IMAGES_DIR = getFilesDir()+"/Images/";

        //Create the image directories if needed
        File dir = new File(IMAGES_DIR);
        if(!dir.exists())
        {
            if(!dir.mkdirs())
            {
                throw new RuntimeException("Internal Error - could not create images directory");
            }
        }
        dir = null;

        // window features - must be set prior to calling setContentView...
        //////////requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        mMainScreen = (RelativeLayout) findViewById(R.id.main_screen);
        mHelpScreen = (RelativeLayout) findViewById(R.id.help_screens);
        mMonthView1 = (RelativeLayout) findViewById(R.id.MonthView1);
        mMonthView2 = (RelativeLayout) findViewById(R.id.MonthView2);
        mDayViewScreen = (RelativeLayout) findViewById(R.id.day_view_screen);

        mMainScreen.setVisibility(View.INVISIBLE);

        msgHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                byte[] blob = null;
                ImageInfo imageInfo = null;
                String urlString = null;
                Bitmap bitmap = null;
                switch (msg.what)
                {
                    case Constants.HANDLER_MESSAGE_INFO:
                        calendarInfo = (CalendarInfo) msg.obj;
                        //Get the calendar info, if the versions match, skip the rest
                        //get the current version and create the database if needed
                        dbHelper = new DBHelper(singleton);
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        Cursor c = null;
                        try {
                            c = db.query(CalendarContract.INFO_TABLE_NAME, null, null, null, null, null, null);
                        }
                        catch(Exception e)
                        {
                            c  = null;
                        }

                        if(c != null && c.getCount() > 0) {
                            c.moveToFirst();
                            currentCalendarVersion = c.getInt(c.getColumnIndex(CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_CALENDAR_VERSION));
                        }
                        else {
                            currentCalendarVersion = -1;
                        }

                        //If calendarInfo is null then we couldn't connect to the server
                        //  use the database values if we can
//                        if (calendarInfo == null || currentCalendarVersion == calendarInfo.version)
//                        {
//                            mStartDate = Calendar.getInstance();
//                            mEndDate = Calendar.getInstance();
//                            String startDateString = c.getString(c.getColumnIndex(CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_START_DATE));
//                            mStartDate.setTime(parseDate(startDateString, Constants.DATABASE_SHORT_DATE_FORMAT));
//                            String endDateString = c.getString(c.getColumnIndex(CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_END_DATE));
//                            mEndDate.setTime(parseDate(endDateString, Constants.DATABASE_SHORT_DATE_FORMAT));
//                            InitializeData();
//                            InitializeGUI();
//                        }
//                        else
                        {
                            mStartDate = calendarInfo.startDate;
                            mEndDate = calendarInfo.endDate;

                            dbHelper.addInfo(calendarInfo.version,
                                    formatDate(mStartDate, Constants.DATABASE_SHORT_DATE_FORMAT),
                                    formatDate(mEndDate, Constants.DATABASE_SHORT_DATE_FORMAT));

                            //Get the splash image
                            urlString = Constants.SERVER_SPLASH_URI;
                            SplashServerCall taskSplash = new SplashServerCall(singleton);
                            taskSplash.execute(urlString);
                        }
                        break;
                    case Constants.HANDLER_MESSAGE_NOTES:
                        ArrayList<Note> notes = (ArrayList<Note>) msg.obj;
                        if (notes.size() > 0)
                        {
                            for (Note aNote : notes)
                            {
                                long id = dbHelper.addNote(aNote);
                                aNote.setId(id);
                            }
                        }
                        InitializeData();
                        InitializeGUI();
                        break;
                    case Constants.HANDLER_MESSAGE_SPLASH_IMAGE:
                        blob = (byte[])msg.obj;
                        createAndSaveBitmap(IMAGES_DIR + "/splash.png", blob);
                        urlString = Constants.SERVER_IMAGE_NAMES_URI;
                        ImageNamesServerCall taskMonthImageNames = new ImageNamesServerCall(singleton);
                        taskMonthImageNames.execute(urlString);
                        break;
                    case Constants.HANDLER_MESSAGE_IMAGE:
                        imageInfo = (ImageInfo)msg.obj;
                        createAndSaveBitmap(IMAGES_DIR + "/month/" + imageInfo.name, imageInfo.decoded);
                        break;
                    case Constants.HANDLER_MESSAGE_IMAGE_NAMES:
                        ArrayList<ImageInfo> imageNames = (ArrayList<ImageInfo>)msg.obj;
                        for(ImageInfo info : imageNames) {
                            urlString = Constants.SERVER_IMAGE_URI+info.path;
                            ImageServerCall taskMonth = new ImageServerCall(singleton, info);
                            taskMonth.execute(urlString);
                        }
                        urlString = Constants.SERVER_NOTES_URI;
                        NotesServerCall taskNotes = new NotesServerCall(singleton);
                        taskNotes.execute(urlString);
                        break;
                    case Constants.HANDLER_MESSAGE_NEW_NOTE:
                        Note note = (Note)msg.obj;
                        if(note != null)
                        {
                            long id = dbHelper.addNote(note);
                            note.setId(id);
                            mNotesListItemAdapter.notifyDataSetInvalidated();
                            mNotesListItemAdapter.notifyDataSetChanged();
                            notes = (ArrayList<Note>) mCurrentDateBtn.getTag();
                            notes.add(note);
                            mCurrentDateBtn.setTag(notes);
                            UpdateFooter(note);
                        }
                }

            }
        };

        //Eula.show(this);

        ///////////////////showBusyDialog();

        String result = null;
        String urlString = null;

        //Get the calendar info, if the versions match, skip the rest
        //get the current version and create the database if needed
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query(CalendarContract.INFO_TABLE_NAME, null, null, null, null, null, null);
        }
        catch(Exception e)
        {
            c  = null;
        }

//        if(c != null && c.getCount() > 0)
//        {
//            c.moveToFirst();
//            currentCalendarVersion = c.getInt(c.getColumnIndex(CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_CALENDAR_VERSION));
//            mStartDate = Calendar.getInstance();
//            mEndDate = Calendar.getInstance();
//            String startDateString = c.getString(c.getColumnIndex(CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_START_DATE));
//            mStartDate.setTime(parseDate(startDateString, Constants.DATABASE_SHORT_DATE_FORMAT));
//            String endDateString = c.getString(c.getColumnIndex(CalendarContract.CalendarInfoEntry.COLUMN_NAME_INFO_END_DATE));
//            mEndDate.setTime(parseDate(endDateString, Constants.DATABASE_SHORT_DATE_FORMAT));
//            calendarInfo = new CalendarInfo(currentCalendarVersion, startDateString, endDateString);
//        }
//        else
//        {
            //TODO get this info from the server
            urlString = Constants.SERVER_ADDRESS + "/getInfo?npo=das";
            new InfoServerCall(this).execute(urlString);
//        }

        //InitializeData() here, InitializeGUI() is called after the calendar info is retrieved
        //InitializeData();
        //InitializeGUI();
/*
        //Get the calendar notes
        urlString = Constants.SERVER_ADDRESS + "/getCalendar/npo/das";
        new NotesServerCall(this).execute(urlString);

        //Get the first month image, the messageHandler will do the rest
        // months in the database are 01:12
        // always start with the current month
        Calendar now = Calendar.getInstance();
        int thisMonth = now.get(Calendar.MONTH) + 1;
        urlString = Constants.SERVER_ADDRESS + "/getMonth/npo/das/month/"+(thisMonth<10?"0"+thisMonth:""+thisMonth);
        new ImageServerCall(this).execute(urlString);
*/
    }


    @Override
    public void onDestroy()
    {
//		Debug.stopMethodTracing();
        super.onDestroy();
    }

    private void InitializeData()
    {
        mCalendar = Calendar.getInstance();
        mCurrentMonthIndex = mCalendar.get(Calendar.MONTH) + 1;
        mCurrentYear = mCalendar.get(Calendar.YEAR);

//        if(currentCalendarVersion == 1)
//        {
//            return;
//        }
//
//        File root;
//        File fin;
//        FileInputStream in;
//        FileOutputStream out;
//        int numNotes = 0;
//
//        try
//        {
//            root = Environment.getExternalStorageDirectory();
//            if( root.canRead() )
//            {
//                fin = new File( root, SAVEFILENAME );
//            }
//            else
//            {
//                // ShowErrorDialog(
//                // "Could not access Saved/Restore data, The saved simulation will not be restored."
//                // );
//                throw new IOException();
//            }
//        }
//        catch ( IOException e )
//        {
//            SetDefaultValuesAndInitializeGUI();
//            return;
//        }
//
//        if( !fin.exists() )
//        {
//            InputStream ins = getResources().openRawResource( R.raw.fundraiser );
//
//            int size;
//            try
//            {
//                size = ins.available();
//                if( size <= 0 )
//                {
//                    throw new IOException();
//                }
//
//                // Read the entire resource into a local byte buffer.
//                byte[] buffer = new byte[size];
//                ins.read( buffer );
//                ins.close();
//                if( root.canWrite() )
//                {
//                    File fout = new File( root, SAVEFILENAME );
//                    out = new FileOutputStream( fout );
//                    out.write( buffer );
//                    out.flush();
//                    out.close();
//                }
//            }
//            catch ( IOException e2 )
//            {
//                SetDefaultValuesAndInitializeGUI();
//                return;
//            }
//        }
//
//        // Read using DataInputStream.
//        try
//        {
//            in = new FileInputStream( fin );
//        }
//        catch ( FileNotFoundException e2 )
//        {
//            SetDefaultValuesAndInitializeGUI();
//            return;
//        }
//
//        DataInputStream obj_in = null;
//        obj_in = new DataInputStream( in );
//
//        try
//        {
//            byte[] dateBuffer = new byte[8]; // mmddyyyy
//            byte[] b = null;
//            int red = -1;
//            int len = -1;
//
//            if( mStartDate == null )
//            {
//                mStartDate = Calendar.getInstance();
//                mEndDate = Calendar.getInstance();
//            }
//
//            /**
//             * Start reading the database
//             */
//
//            //version string for the calendar, already checked in isDatabaseHealthy
//            //			len = obj_in.readInt();
//            //			b = new byte[len];
//            //			red = obj_in.read( b, 0, len );
//
//            //Start Date for the calendar
//            len = obj_in.readInt();
//            b = new byte[len];
//            red = obj_in.read( b, 0, len );
//            mStartDate.setTime(parseDate( new String(b), Constants.INTERNAL_SHORT_DATE_FORMAT));
//
//            //End date for the calendar
//            len = obj_in.readInt();
//            b = new byte[len];
//            red = obj_in.read( b, 0, len );
//            mEndDate.setTime(parseDate(new String(b), Constants.INTERNAL_SHORT_DATE_FORMAT));
//
//            //Current state info
//            mCurrentDay = obj_in.readInt();
//            Log.d( TAG, "Restore\\\\mCurrentDay = " + mCurrentDay );
//            mCurrentMonthIndex = obj_in.readInt();
//            Log.d( TAG, "Restore\\\\mCurrentMonthIndex = " + mCurrentMonthIndex );
//            mCurrentYear = obj_in.readInt();
//            Log.d( TAG, "Restore\\\\mCurrentYear = " + mCurrentYear );
//            numNotes = obj_in.readInt();
//            Log.d( TAG, "Restore\\\\numNotes = " + numNotes );
//        }
//        catch ( IOException e1 )
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        dbHelper.addInfo("test", 1,
//                formatDate(mStartDate, Constants.DATABASE_SHORT_DATE_FORMAT),
//                formatDate(mEndDate, Constants.DATABASE_SHORT_DATE_FORMAT));

        //Set the calendar
        mCalendar.set( mCurrentYear, mCurrentMonthIndex, mCurrentDay );

//        //Notes
//        for( int iNote = 0; iNote < numNotes; iNote++ )
//        {
//            Note note = new Note();
//            try
//            {
//                note.Restore( obj_in );
//            }
//            catch ( IOException e )
//            {
//                ReportIOException( e );
//                e.printStackTrace();
//            }
//            dbHelper.addNote( note );
//        }
//
//        try
//        {
//            in.close();
//        }
//        catch ( IOException e )
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        // release it
//        in = null;
    }

    /**
     * @param dateString
     *            the date to set
     */
    public static Date parseDate( String dateString, final String format )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( format );
        Date date = null;
        try
        {
            date = sdf.parse( dateString );
        }
        catch ( ParseException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDate(Calendar date, final String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat( format );
        String dateString = null;
        dateString = sdf.format(date.getTime());

        return dateString;
    }

    /**
     * InitializeGUI
     */
    private void InitializeGUI()
    {

        //touch listener for direction arrows, must be before the MonthViewFlipper
        if (mMonthNameTouchListener == null)
        {
            mMonthNameTouchListener = new MonthNameTouchListener(this);
        }

        // Get the ViewFlippers
        if (mMonthViewFlipper == null)
        {
            mMonthViewFlipper = (ViewFlipper) findViewById(R.id.monthviewflipper);
            mMonthViewFlipper.setDisplayedChild(0);
            TextView tv = (TextView) findViewById(R.id.TextMonthName);
            //attach the touch listener to the month name view
            tv.setOnTouchListener(mMonthNameTouchListener);
        }

        if (mHelpViewFlipper == null)
        {
            mHelpViewFlipper = (ViewFlipper) findViewById(R.id.help_screens).findViewById(R.id.helpflipper);
            mHelpViewFlipper.setDisplayedChild(0);
        }

        if (mSlideLeftIn == null)
        {
            mSlideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            mSlideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            mSlideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            mSlideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            mSlideDownOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
            mSlideUpIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);

            mSlideDownOut.setAnimationListener(new SlideOutAnimationListener(this));
            mSlideUpIn.setAnimationListener(new SlideInAnimationListener(this));

        }

        mMainFlipImage = mMainScreen;
        mMainFlipImage.setTag("MainFlipImage");

        mHelpFlipImage = mHelpScreen;
        mHelpFlipImage.setTag("HelpFlipImage");

        mHelpFlipImage.setVisibility(View.GONE);

        TextView tv = (TextView) mHelpFlipImage.findViewById(R.id.helpscreen_version_info);
        String versionString = "Version 1.0";
        PackageInfo pInfo = null;

        try
        {
            pInfo = getPackageManager().getPackageInfo("com.das.yacalendar", PackageManager.GET_META_DATA);

        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }

        if (pInfo != null)
        {
            versionString = "Version " + pInfo.versionName;
        }

        tv.setText(versionString);

        //Create the gesture detector for the main view
        if (mCalendarGestureDetector == null)
        {
            mCalendarGestureDetector = new GestureDetector(new CalendarGestureListener(this));
            mCalendarTouchListener = new OnTouchListener()
            {

                public boolean onTouch(View v, MotionEvent event)
                {
                    if (mCalendarGestureDetector.onTouchEvent(event))
                    {
                        return true;
                    }
                    return false;
                }
            };

            mMainScreen.setOnTouchListener(mCalendarTouchListener);
        }

        if (mFooterTouchListener == null)
        {
            View footer = (View) findViewById(R.id.MonthFooter);
            mFooterTouchListener = new OnTouchListener()
            {

                public boolean onTouch(View v, MotionEvent event)
                {
                    // TODO Auto-generated method stub
                    return false;
                }
            };

            footer.setOnTouchListener(mFooterTouchListener);
        }

        //Set the calendar
//        mCurrentDisplayedDate = Calendar.getInstance();
//
//        mCalendar.set(Calendar.YEAR, mCurrentYear);
//        mCalendar.set(Calendar.MONTH, mCurrentMonthIndex); //January == 0
//        mCalendar.set(Calendar.DATE, mCurrentDisplayedDate);

//        GridView calendarView = (GridView)findViewById(R.id.calendar);
//        adapter = new GridCellAdapter(this, R.layout.day, mCurrentMonthIndex, mCurrentYear);
//        adapter.notifyDataSetChanged();
//        calendarView.setAdapter(adapter);
//        calendarView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                BtnDayClickHandler(view);
//                ShowDayView();
//                return true;
//            }
//        });

        mCurrentMonthView = mMonthView1;

        View splash = (View)findViewById( R.id.splash_screen );

        mMainScreen.setVisibility(View.VISIBLE);
        mMainScreen.bringToFront();
        splash.setVisibility(View.GONE);

        UpdateFromPrefreneces();

        displayMonth(mMonthView1, false); //current date was set from the data file

    }

    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig)
    {
        if (newConfig.orientation != Configuration.ORIENTATION_PORTRAIT)
        {
            newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        }

        super.onConfigurationChanged(newConfig);
    }

    public void showBusyDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getResources().getString(R.string.Working));
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.hide();
        }
    }

    /**
     * Handle Save and Restore here
     */
    // ===========================================================
    //
    // On pause()
    // called when every the app is no longer in the foreground
    // including being stopped
    //
    // ============================================================
    @Override
    public void onPause()
    {
        FileOutputStream out = null;
        boolean ret;

        super.onPause();

//        File root = Environment.getExternalStorageDirectory();
//
//        if (root.canWrite()) {
//            File fout = new File(root, SAVEFILENAME);
//            try {
//                out = new FileOutputStream(fout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // ShowErrorDialog(
//            // "Could not create Save/Restore Data, The current simulation will be lost" );
//            return;
//        }
//
//        // Use an ObjectOutputStream to send object data to the
//        // FileOutputStream for writing to disk.
//        DataOutputStream obj_out = null;
//        obj_out = new DataOutputStream(out);
//        // Save our state first
//        //
//        int numNotes = mNotes.size();
//
//        try {
//            String key = getCalendarDateAsExternalString(mStartDate);
//            int len = key.length();
//            obj_out.writeInt(len);
//            obj_out.writeBytes(key);
//            Log.d(TAG, "Save\\\\mStartDate = " + key);
//
//            key = getCalendarDateAsExternalString(mEndDate);
//            len = key.length();
//            obj_out.writeInt(len);
//            obj_out.writeBytes(key);
//            Log.d(TAG, "Save\\\\mEndDate = " + key);
//
//            obj_out.writeInt(mCurrentDisplayedDate);
//            Log.d(TAG, "Save\\\\mCurrentDisplayedDate = " + mCurrentDisplayedDate);
//            obj_out.writeInt(mCurrentMonthIndex);
//            Log.d(TAG, "Save\\\\mCurrentMonthIndex = " + mCurrentMonthIndex);
//            obj_out.writeInt(mCurrentYear);
//            Log.d(TAG, "Save\\\\mCurrentYear = " + mCurrentYear);
//            obj_out.writeInt(numNotes);
//            Log.d(TAG, "Save\\\\numNotes = " + numNotes);
//            obj_out.flush();
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//
//        Iterator iterator = mNotes.keySet().iterator();
//        while (iterator.hasNext()) {
//            Object key = iterator.next();
//            Note note = mNotes.get(key);
//            try {
//                note.Save(obj_out);
//            } catch (IOException e) {
//                ReportIOException(e);
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            obj_out.flush();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        try {
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
    }

    // ===============================================================================
    // OnResume()
    // called when the app is being brought into the foreground including initial
    // start
    //
    // ===============================================================================
    @Override
    public void onResume()
    {
        FileInputStream in = null;
        FileOutputStream out = null;
        File fin = null;

        int numNotes = 0;
        File root = null;

        super.onResume();

        //If this is not the initial call to the application, then do NOT restore the data
        //  this will give a false positive if there is an empty database
/*
        if (mNotes == null || mNotes.size() == 0) {
            try {
                root = Environment.getExternalStorageDirectory();
                if (root.canRead()) {
                    fin = new File(root, SAVEFILENAME);
                } else {
                    // ShowErrorDialog(
                    // "Could not access Saved/Restore data, The saved simulation will not be restored."
                    // );
                    throw new IOException();
                }
            } catch (IOException e) {
                SetDefaultValuesAndInitializeGUI();
                return;
            }
*/

        //If the savefile does not exist or if the version number is not current,
        //  copy it to the sdcard from the raw resource

        //boolean healthy = true;

        //		try
        //        {
        //	        healthy = isDatabaseHealthy( fin );
        //        }
        //        catch ( IOException e3 )
        //        {
        //	        healthy = false;
        //	        e3.printStackTrace();
        //        }

//        if (!fin.exists()) {
//            InputStream ins = getResources().openRawResource(R.raw.fundraiser);
//
//            int size;
//            try {
//                size = ins.available();
//                if (size <= 0) {
//                    throw new IOException();
//                }
//
//                // Read the entire resource into a local byte buffer.
//                byte[] buffer = new byte[size];
//                ins.read(buffer);
//                ins.close();
//                if (root.canWrite()) {
//                    File fout = new File(root, SAVEFILENAME);
//                    out = new FileOutputStream(fout);
//                    out.write(buffer);
//                    out.flush();
//                    out.close();
//                }
//            } catch (IOException e2) {
//                SetDefaultValuesAndInitializeGUI();
//                return;
//            }
//        }

        // Read using DataInputStream.
//        try {
//            in = new FileInputStream(fin);
//        } catch (FileNotFoundException e2) {
//            SetDefaultValuesAndInitializeGUI();
//            return;
//        }
//
//        DataInputStream obj_in = null;
//        obj_in = new DataInputStream(in);
//
//        try {
//            byte[] dateBuffer = new byte[8]; // mmddyyyy
//            byte[] b = null;
//            int red = -1;
//            int len = -1;
//
//            if (mStartDate == null) {
//                mStartDate = Calendar.getInstance();
//                mEndDate = Calendar.getInstance();
//            }
//
//            /**
//             * Start reading the database
//             */
//
//            //version string for the calendar, already checked in isDatabaseHealthy
//            //			len = obj_in.readInt();
//            //			b = new byte[len];
//            //			red = obj_in.read( b, 0, len );
//
//            //Start Date for the calendar
//            len = obj_in.readInt();
//            b = new byte[len];
//            red = obj_in.read(b, 0, len);
//            setStartDate(new String(b));
//
//            //End date for the calendar
//            len = obj_in.readInt();
//            b = new byte[len];
//            red = obj_in.read(b, 0, len);
//            setEndDate(new String(b));
//
//            //Current state info
//            mCurrentDisplayedDate = obj_in.readInt();
//            Log.d(TAG, "Restore\\\\mCurrentDisplayedDate = " + mCurrentDisplayedDate);
//            mCurrentMonthIndex = obj_in.readInt();
//            Log.d(TAG, "Restore\\\\mCurrentMonthIndex = " + mCurrentMonthIndex);
//            mCurrentYear = obj_in.readInt();
//            Log.d(TAG, "Restore\\\\mCurrentYear = " + mCurrentYear);
//            numNotes = obj_in.readInt();
//            Log.d(TAG, "Restore\\\\numNotes = " + numNotes);
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        //Set the calendar
//        mCalendar.set(mCurrentYear, mCurrentMonthIndex, mCurrentDisplayedDate);
//
//        //Notes
//        for (int iNote = 0; iNote < numNotes; iNote++) {
//            Note note = new Note();
//            try {
//                note.Restore(obj_in);
//            } catch (IOException e) {
//                ReportIOException(e);
//                e.printStackTrace();
//            }
//            mNotes.put(note.mDate, note);
//            note = null;
//        }
//
//        try {
//            in.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        // release it
//        in = null;

/////////////////////////////////////        InitializeGUI();
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);
        UpdateFromPrefreneces();
    }

    // ==========================================================
    // CheckMediaState
    // ==========================================================
    private boolean CheckMediaState()
    {
        boolean externalStorageAvailable = false;
        boolean externalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            // We can read and write the media
            externalStorageAvailable = externalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            // We can only read the media
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        } else
        {
            // Something else is wrong. It may be one of many other states, but all we need
            // to know is we can neither read nor write
            externalStorageAvailable = externalStorageWriteable = false;
        }

        return externalStorageAvailable & externalStorageWriteable;
    }

    /*******************************************************************************/
    /*                                                                             */
    /*                     Listeners                                               */
    /*                                                                             */

    /**
     * ***************************************************************************
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mDayViewScreen.getVisibility() == View.VISIBLE)
            {
                //the exit action is taken in the dialog OnClick method
                FragmentManager fm = getSupportFragmentManager();
                CancelWarningDialog dlg = new CancelWarningDialog();
                dlg.show(fm, "dialog_cancel_warning");
                return true;
            } else if (mHelpScreen.getVisibility() == View.VISIBLE)
            {
                BtnHelpDoneHandler(null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*******************************************************************************/
	/*                                                                             */
	/*                     Button Click Handler                                    */
	/*                                                                             */

    /**
     * ***************************************************************************
     */
    public void MonthTouchListener(View v)
    {
        mCalendar.add(Calendar.MONTH, 1);
        //if we have gone past the end date, wrap around
        if(mCalendar.after(calendarInfo.getEndDate()))
        {
            mCalendar = calendarInfo.getStartDate();
        }
/*
        ++mCurrentMonthIndex;
        if (mCurrentMonthIndex >= kNumMonths)
        {
            mCurrentMonthIndex = 0;
            mCurrentYear++;
        }
*/

        if (mMonthView1.getVisibility() == View.VISIBLE)
        {
            mCurrentMonthView = mMonthView2;
            displayMonth(mMonthView2, true);
            mMonthViewFlipper.setDisplayedChild(1);
        } else
        {
            mCurrentMonthView = mMonthView1;
            displayMonth(mMonthView1, true);
            mMonthViewFlipper.setDisplayedChild(0);
        }

    }

    /**
     * BtnStatsLeftArrowClickHandler
     *
     * @param v
     */
    public void BtnMonthLeftArrowClickHandler(View v)
    {
        RelativeLayout arrows = (RelativeLayout) findViewById(R.id.framearrows);
        arrows.setVisibility(View.INVISIBLE);
        SlideInPreviousMonthView();
    }

    /**
     * BtnStatsRightArrowClickHandler
     *
     * @param v
     */
    public void BtnMonthRightArrowClickHandler(View v)
    {
        RelativeLayout arrows = (RelativeLayout) findViewById(R.id.framearrows);
        arrows.setVisibility(View.INVISIBLE);
        SlideInNextMonthView();
    }

    /**
     * BtnDayClickHandler
     *
     * @param v
     */
    public void BtnDayClickHandler(View v)
    {
//		//If the help screen is visible ignore touch events
//		if( mHelpScreen.getVisibility() == View.VISIBLE )
//		{
//			return;
//		}

        if(mCurrentDateBtn != null) {
            mCurrentDateBtn.setBtnAsSelected(false);
        }
        mCurrentDateBtn = (DateButton) v;
        mCurrentDateBtn.setBtnAsSelected(true);

        //Update the current date
        try
        {
            //change the day in the current month
            int date = Integer.parseInt((String) mCurrentDateBtn.getText());
            mCalendar.set(Calendar.DATE, date);
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        List<Note> notes = (List<Note>) v.getTag();
        UpdateFooter(notes);
    }

    public void BtnVisitWebsiteClickHandler(View v)
    {

    }

    /**
     * BtnHelpNextClickHandler
     *
     * @param v
     */
    public void BtnHelpNextClickHandler(View v)
    {
        SlideInNextHelpView();
    }

    /**
     * BtnHelpNextClickHandler
     *
     * @param v
     */
    public void BtnHelpPrevClickHandler(View v)
    {
        SlideInPreviousHelpView();
    }

    /**
     * BtnHelpDoneHandler
     *
     * @param v
     */
    public void BtnHelpDoneHandler(View v)
    {
        applyRotation(0, -90);
        isMainFlipImage = !isMainFlipImage;
    }

    /*******************************************************************************/
	/*                                                                             */
	/*                          Dialogs                                            */
	/*                                                                             */

    /**
     * ***************************************************************************
     */

    // ==================================================================
    // ShowErrorDialog
    // ==================================================================
    private void ShowErrorDialog(final String msg)
    {
        Context context = this;

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Error");
        ad.setMessage(msg);
        ad.setNeutralButton(R.string.Menu_Done, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }

        });
        ad.show();
    }

    /*******************************************************************************/
	/*                                                                             */
	/*                          Menus                                              */
	/*                                                                             */
    /*******************************************************************************/

    /**
     * onCreateContextMenu
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
//        if (v.getId() == R.id.day_listview)
//        {
//            //			mContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
//            //			mNoteListSelectedItemIndex = mContextMenuInfo.position;
//
//            menu.setHeaderTitle(getResources().getString(R.string.Menu_Context_Title));
//
//            menu.clear();
//            menu.add(Menu.NONE, v.getId(), 0, R.string.Menu_Delete);
//            menu.add(Menu.NONE, v.getId(), 0, R.string.Menu_Edit);
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
//        if (item.getTitle().equals(getResources().getString(R.string.Menu_Delete)))
//        {
//            DeleteItemFromNotes();
//        } else if (item.getTitle().equals(getResources().getString(R.string.Menu_Edit)))
//        {
//            //If the this is the first time this dialog is shown, onCreateDialog will
//            // handle initializing the values, other wise we do it here
//            if (mEditNoteDialog != null)
//            {
//                InitializeEditNoteDialog();
//            }
//            showDialog(DIALOG_EDIT_NOTE);
//        }
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //If the help screen is visible, then disable the menu
        if (mHelpScreen.getVisibility() == View.VISIBLE)
        {
            return false;
        }

        menu.clear();

        if (mDayViewScreen.getVisibility() == View.VISIBLE)
        {
//            menu.add(Menu.NONE, MENU_NEW, 0, R.string.Menu_New).setIcon(
//                    getResources().getDrawable(R.drawable.ic_menu_compose));
//            menu.add(Menu.NONE, MENU_CLEAR_ALL, 1, R.string.Menu_Clear_All).setIcon(
//                    getResources().getDrawable(R.drawable.ic_menu_clear_playlist));
            menu.add(Menu.NONE, MENU_CANCEL, 2, R.string.Menu_Cancel).setIcon(
                    getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
            menu.add(Menu.NONE, MENU_DONE, 3, R.string.Menu_Done).setIcon(
                    getResources().getDrawable(android.R.drawable.ic_menu_save));
        } else
        {
            menu.add(Menu.NONE, MENU_DAY_VIEW, 0, R.string.Menu_Day).setIcon(
                    getResources().getDrawable(android.R.drawable.ic_menu_day));
            menu.add(Menu.NONE, MENU_SETTINGS, 1, R.string.Menu_Preferences).setIcon(
                    getResources().getDrawable(android.R.drawable.ic_menu_preferences));
            menu.add(Menu.NONE, MENU_HELP, 2, R.string.Menu_Help).setIcon(
                    getResources().getDrawable(android.R.drawable.ic_menu_help));
            menu.add(Menu.NONE, MENU_EXIT, 3, R.string.Menu_Exit).setIcon(
                    getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
        }

        super.onPrepareOptionsMenu(menu);

        return true;
    }

    /**
     * onOptionsItemSelected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Note note = null;
        List<Note> notes = null;
        if (mCurrentDateBtn != null)
        {
            notes = (List<Note>) mCurrentDateBtn.getTag();
        }
        String key = generateKeyFromCalendar(mCalendar);

        // Handle item selection
        switch (item.getItemId())
        {
            case MENU_DAY_VIEW:
                ShowDayView();
                return true;
            case MENU_HELP:
                if (isMainFlipImage)
                {
                    applyRotation(0, 90);
                    isMainFlipImage = !isMainFlipImage;
                } else
                {
                    applyRotation(0, -90);
                    isMainFlipImage = !isMainFlipImage;
                }
                return true;
            case MENU_SETTINGS:
                Intent intent = new Intent().setClass(this, com.das.yacalendar.Preferences.class);
                this.startActivityForResult(intent, 0);
                return true;
            case MENU_EXIT:
                this.finish();
                return true;
//            case MENU_NEW:
//                composeNewNote(mCalendar);
//                return true;
//            case MENU_CLEAR_ALL:
//                mNotesListItemAdapter.clear();
//                for (int pos = 0; pos < NUMBER_OF_NOTE_STRINGS; pos++)
//                {
//                    mNotesListItemAdapter.add("");
//                }
//
//                mNotesListItemAdapter.notifyDataSetChanged();
//                UpdateFooter(note);
//                return true;
            case MENU_CANCEL:
                HideDayView();
                return true;
            case MENU_DONE:
                ListView lv = (ListView) findViewById(R.id.day_listview);
                int num = lv.getChildCount();
                //do this in two passes becuse of the remove
                for(int index = 0; index < num; index++) {
                    RelativeLayout v = (RelativeLayout) lv.getChildAt(index);
                    NoteListItemView iv = (NoteListItemView) v.findViewById(R.id.noteitem_text);
                    note = notes.get(index);
                    note.setText(iv.getText().toString());
                }
                for(int index = 0; index < num; index++) {
                    RelativeLayout v = (RelativeLayout) lv.getChildAt(index);
                    if(v.getVisibility() == View.GONE) {
                        note = notes.get(index);
                        notes.remove(index);
                        dbHelper.removeNote(note);
                    }
                }
                mCurrentDateBtn.setTag(notes);
                HideDayView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*******************************************************************************/
	/*                                                                             */
	/*                          Getters & Setters                                  */
	/*                                                                             */
    /*******************************************************************************/



    /*******************************************************************************/
	/*                                                                             */
	/*                          Utility Methods                                    */
	/*                                                                             */
    /*******************************************************************************/

    /**
     *
     * @param path - where to save the png
     * @param blob - png
     * @return the bitmap
     *
     */
    private Bitmap createAndSaveBitmap(final String path, final byte[] blob)
    {
        if(blob != null && blob.length > 0) {
            try {
                File out = new File(path);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));
                bos.write(blob);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getLocalizedMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        else
        {
            throw new RuntimeException("Internal Error - could not save image " + path);
        }
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }

    /**
     * composeNewNote
     */
    public void composeNewNote(final Calendar date) {

        ListView lv = (ListView) findViewById(R.id.day_listview);
//        mNotesListItemAdapter.add(""); //make this empty so that the hint text will appear
//        mNotesListItemAdapter.notifyDataSetChanged();
//        mNoteListSelectedItemIndex = mNotesListItemAdapter.getCount() - 1;

        showAddNoteDialog(date);
        //If the this is the first time this dialog is shown, onCreateDialog will
        // handle initializing the values, other wise we do it here
//                if (mEditNoteDialog != null)
//                {
//                    InitializeNewNoteDialog();
//                }
//                showDialog(DIALOG_EDIT_NOTE);
    }

    /**
     * InitializeEditNoteDialog
     */
    private void InitializeEditNoteDialog()
    {
        //the current selected date is set in BtnDayClickHandler
        String title = getResources().getString(R.string.Menu_Edit) + " : "
                + getResources().getString(mMonthNameStringId[mCalendar.get(Calendar.MONTH)]) + " "
                + mCalendar.get(Calendar.DATE) + ", " + mCalendar.get(Calendar.YEAR);
        mEditNoteDialog.setTitle(title);

        //Set the text
        LinearLayout l = (LinearLayout) mEditNoteView.findViewById(R.id.edit_note);
        EditText et = (EditText) l.findViewById(R.id.edit_note_text);
        et.setHint(R.string.Text_Edit_Hint);
        et.setText(mCurrentNotesList.get(mNoteListSelectedItemIndex));

        //Set the cursor at the end of the string
        Editable eText = et.getEditableText();
        int pos = eText.length();
        Selection.setSelection(eText, pos);

    }

    /**
     * InitializeEditNoteDialog
     */
    private void InitializeNewNoteDialog()
    {
        //the current selected date is set in BtnDayClickHandler
        String title = getResources().getString(R.string.Menu_New) + " : "
                + getResources().getString(mMonthNameStringId[mCalendar.get(Calendar.MONTH)]) + " "
                + mCalendar.get(Calendar.DATE) + ", " + mCalendar.get(Calendar.YEAR);
        mEditNoteDialog.setTitle(title);

        //Set the text
        LinearLayout l = (LinearLayout) mEditNoteView.findViewById(R.id.edit_note);
        EditText et = (EditText) l.findViewById(R.id.edit_note_text);
        et.setHint(R.string.Text_Edit_Hint);
        et.setText("");
    }

    /**
     * ShowDayView
     */
    private void ShowDayView()
    {
        LinearLayout dayView = (LinearLayout) findViewById(R.id.day_view);

        //if the dayView screen is visible, then this menu item is monthView not dayView
        String title = getResources().getString((R.string.RemindersFor)) +
                " " +
                getResources().getString(mMonthNameStringId[mCalendar.get(Calendar.MONTH)]) +
                " " +
                mCalendar.get(Calendar.DATE) +
                ", " +
                mCalendar.get(Calendar.YEAR);
        TextView tv = (TextView) dayView.findViewById(R.id.day_view_title);
        tv.setText(title);

        mCurrentNotesList = new ArrayList<>();
        List<Note> notes = (List<Note>)mCurrentDateBtn.getTag();
        for(Note note : notes)
        {
            mCurrentNotesList.add(note.getText());
        }

        if(mCurrentNotesList != null && mCurrentNotesList.size() > 0) {
            int resID = R.layout.notelist_item;

            mNotesListItemAdapter = new NotesListItemAdapter(this, resID, notes);
            ListView lv = (ListView) dayView.findViewById(R.id.day_listview);
            lv.setAdapter(mNotesListItemAdapter);
            lv.setDividerHeight(4);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setItemsCanFocus(true);
            registerForContextMenu(lv);
            lv.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                    mNoteListSelectedItemIndex = position;
                    v.performLongClick();
                }
            });

            ImageButton fabBtn = (ImageButton) mDayViewScreen.findViewById(R.id.fab);
            fabBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddNoteDialog(mCalendar);
                }
            });
            mMainScreen.setVisibility(View.GONE);
            mDayViewScreen.setVisibility(View.VISIBLE);
            mDayViewScreen.bringToFront();
        }
    }

    public void HideDayView()
    {
        mDayViewScreen.setVisibility(View.GONE);
        mMainScreen.setVisibility(View.VISIBLE);
        mMainScreen.bringToFront();
    }

    /**
     * DeleteItemFromNotes
     */
    private void DeleteItemFromNotes()
    {
        Note deletedItem = mNotesListItemAdapter.getItem(mNoteListSelectedItemIndex);

        //this will delete it from mCurrentNotesList also
        mNotesListItemAdapter.remove(deletedItem);
    }

    /**
     * ParseNoteStingIntoArray
     */
    private ArrayList<String> ParseNoteStingIntoArray(final Note note)
    {
        ArrayList<String> listItems = new ArrayList<String>();
        String[] items;

        if (note == null || note.getText().equals(""))
        {
            items = new String[0];
        } else
        {
            items = note.getText().split("\n");
        }

        for (int itemNo = 0; itemNo < NUMBER_OF_NOTE_STRINGS; itemNo++)
        {
            if (itemNo < items.length)
            {
                listItems.add(itemNo, items[itemNo]);
            } else
            {
                listItems.add(itemNo, ""); //empty text so that hint text will be shown
            }
        }

        return listItems;
    }

    private String join(ArrayList<String> list, String delimiter)
    {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = list.iterator();

        while (iter.hasNext())
        {
            buffer.append(iter.next());
            if (iter.hasNext())
            {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    private ArrayList<Note> getNotesFromAdapter()
    {
        ListView lv = (ListView) findViewById(R.id.day_listview);
        ArrayList<Note> list = new ArrayList<Note>();

        for (int item = 0; item < mNotesListItemAdapter.getCount(); item++)
        {
            Note note = mNotesListItemAdapter.getItem(item);

            //don't add empty lines
            String string = note.getText();
            if (!string.equals("") && !string.equals("- "))
            {
                list.add(note);
            }
        }

        return list;
    }

    /**
     * UpdatePrefreneceResults
     */
    private void UpdateFromPrefreneces()
    {
        SharedPreferences prefs = getSharedPreferences("com.das.yacalendar_preferences", 0);

//		String method = prefs.getString( this.getResources().getString( R.string.preference_highlight_method ), "0" );
//		int methodValue = Integer.parseInt( method );
//		switch( methodValue )
//			{
//			case 0: //Color
//				DateButton.mHighlightMethod = DateButton.HIGHTLIGHT_BY_COLOR;
//				break;
//			case 1: //Border
//				DateButton.mHighlightMethod = DateButton.HIGHTLIGHT_BY_BORDER;
//				break;
//			case 2: //Line
//				DateButton.mHighlightMethod = DateButton.HIGHTLIGHT_BY_LINE;
//				break;
//			}
        DateButton.mHighlightMethod = DateButton.HIGHTLIGHT_BY_COLOR;

//		String bgColor = prefs.getString( this.getResources().getString( R.string.preference_DOM_background_color ),
//		        "0" );
//		int bgColorValue = Integer.parseInt( bgColor );
//		int color = 0;
//		switch( bgColorValue )
//			{
//			case 0: //Red
//				color = 0xA0FF0000;
//				break;
//			case 1: //Green
//				color = 0xA000FF00;
//				break;
//			case 2: //Blue
//				color = 0xA00000FF;
//				break;
//			case 3: //Yellow
//				color = 0xA0FFFF00;
//				break;
//			case 4: //Cyan
//				color = 0xA000FFFF;
//				break;
//			case 5: //Magenta
//				color = 0xA0FF00FF;
//				break;
//			}
        int color = 0xA00000FF;

        TableLayout table = (TableLayout) mMonthView1.findViewById(R.id.month);
        TableRow tr = (TableRow) table.findViewById(R.id.DOM_header_row);
        tr.setBackgroundColor(color);

        table = (TableLayout) mMonthView2.findViewById(R.id.month);
        tr = (TableRow) table.findViewById(R.id.DOM_header_row);
        tr.setBackgroundColor(color);

        DateButton.mOutlineCell = prefs.getBoolean(this.getResources().getString(R.string.preference_date_outline),
                true);

//		DateButton.mAlpha = prefs.getInt( this.getResources().getString( R.string.preference_alpha ), 100 );
        DateButton.mAlpha = 100;

        mCurrentMonthView.invalidate();
    }

    private boolean isDatabaseHealthy(File fin) throws IOException
    {
        FileInputStream in = null;
        DataInputStream obj_in = null;
        boolean retValue = true;

        if (!fin.exists())
        {
            return false;
        } else
        {
            // Read using DataInputStream.
            try
            {
                in = new FileInputStream(fin);
            } catch (FileNotFoundException e2)
            {
                return false;
            }

            obj_in = new DataInputStream(in);

            String version;
            byte[] b = null;
            int red = -1;
            int len = -1;

            //version string for the calendar
            len = obj_in.readInt();
            b = new byte[len];
            red = obj_in.read(b, 0, len);
            version = new String(b);
            if (!version.equals(DatabaseVersion.version))
            {
                retValue = false;
            }
        }

        in.close();
        obj_in.close();

        return retValue;
    }

    /**
     * isNextMonthInCalendar
     */
    public Boolean isNextMonthInCalendar()
    {
        Calendar calNext = (Calendar) mCalendar.clone();
        calNext.add(Calendar.MONTH, 1);

        if (calNext.after(calendarInfo.getEndDate()))
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * isPrevMonthInCalendar
     */
    public Boolean isPrevMonthInCalendar()
    {
        Calendar calPrev = (Calendar) mCalendar.clone();
        calPrev.add(Calendar.MONTH, -1);

        if (calPrev.before(calendarInfo.getStartDate()))
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * SetDefaultValuesAndInitializeGUI
     */
    private void SetDefaultValuesAndInitializeGUI()
    {
        // If the input file doesn't exist or is corrupted, ignore it
        // and initialize using the defaults
        Calendar cal = Calendar.getInstance();
/*
        mCurrentDisplayedDate = 1;
        mCurrentMonthIndex = cal.get(Calendar.MONTH);
        mCurrentYear = cal.get(Calendar.YEAR);
*/
        //////////////TODO calendarInfo.getNotes().clear();
        InitializeGUI();
    }

    /**
     * generateKeyFromCalendar
     * <p/>
     * return the calendar date as "mmddyyyy" with January = 0
     */
    private String generateKeyFromCalendar(final Calendar cal)
    {
        return String.format("%02d%02d%04d", cal.get(Calendar.MONTH), cal.get(Calendar.DATE),
                cal.get(Calendar.YEAR));
    }

    /**
     * getCalendarDateAsExternalString
     * <p/>
     * return the calendar date as "mmddyyyy" with January = 1
     */

    private String getCalendarDateAsExternalString(final Calendar cal)
    {
        return String.format("%02d%02d%04d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE),
                cal.get(Calendar.YEAR));
    }

    public Handler getMsgHandler() {
        return msgHandler;
    }


    /**
     * displayMonth in view v
     */
    private void displayMonth(View v, final Boolean setDate)
    {
        Note note = null;
        int dateOnCalendar = 1;
        int firstDayOfWeek = 1;
        Calendar cal = Calendar.getInstance();


//        if (setDate)
//        {
//            mCurrentDisplayedDate = 1;
//            mCalendar.set(mCurrentYear, mCurrentMonthIndex, 1);
//        } else
//        {
//            cal.set(mCurrentYear, mCurrentMonthIndex, 1);
//        }
//
//        firstDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1; //Calendar.SUNDAY = 1
        //Set the adapter for this month
        GridView calendarView = (GridView)v.findViewById(R.id.calendar);
        adapter = new GridCellAdapter(this, R.layout.day,
                mCalendar.get(Calendar.MONTH)+1, //zero based
                mCalendar.get(Calendar.YEAR));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
        calendarView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BtnDayClickHandler(view);
                ShowDayView();
                return true;
            }
        });

        TextView monthName = (TextView) findViewById(R.id.TextMonthName);
        //monthName.setText(getString(mMonthNameStringId[mCurrentMonthIndex]) + " " + mCurrentYear);
        monthName.setText(new SimpleDateFormat("MMM - yyyy").format(mCalendar.getTime()));
        //Set the calendar background picture, if it available from the server
        Bitmap bitmap = BitmapFactory.decodeFile(IMAGES_DIR+"/month"+(mCalendar.get(Calendar.MONTH)+1)+".png");
        if(bitmap != null)
        {
            BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
            mMainScreen.setBackground(d);
        }

        //last but not least update the footer
        if(mCurrentDateBtn != null)
        {
            UpdateFooter((List<Note>) mCurrentDateBtn.getTag());
        }
    }

    private void showAddNoteDialog(final Calendar date) {
        FragmentManager fm = getSupportFragmentManager();
        AddNoteDialog editNameDialog = AddNoteDialog.newInstance(date);
        editNameDialog.show(fm, "fragment_add_note");
    }

    private void UpdateFooter(final Note note)
    {
        List<Note> notes = (List < Note >) mCurrentDateBtn.getTag();
        notes.add(note);
        mCurrentDateBtn.setTag(notes);
        UpdateFooter(notes);
    }

    private void UpdateFooter(final List<Note> notes) {
        View footer = findViewById(R.id.MonthFooter);

        if (notes == null || notes.size() == 0) {
            footer.setVisibility(View.INVISIBLE);
        } else {
            TextView tv = (TextView) findViewById(R.id.TextMonthFooter);
            StringBuilder sb = new StringBuilder();
            for (Note note : notes) {
                sb.append(note.getText());
                sb.append("\n");
            }
            tv.setText(sb.toString());
            footer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * SlideInPreviousMonthView
     */
    public void SlideInPreviousMonthView()
    {
        //first check to see if the previous month is on the calendar
        if (!isPrevMonthInCalendar())
        {
            return;
        }

        mMonthViewFlipper.setInAnimation(mSlideLeftIn);
        mMonthViewFlipper.setOutAnimation(mSlideRightOut);

        // Decrement to previous month
        String debug = mCalendar.toString();
        Log.d(TAG, "\\\\SlideInPreviousMonthView before add set: \n" + debug);
        mCalendar.add(Calendar.MONTH, -1);
//        mCurrentMonthIndex = mCalendar.get(Calendar.MONTH);
//        mCurrentYear = mCalendar.get(Calendar.YEAR);
        debug = mCalendar.toString();
        Log.d(TAG, "\\\\SlideInPreviousMonthView after add set: \n" + debug);

        if (mMonthView1.getVisibility() == View.VISIBLE)
        {
            mCurrentMonthView = mMonthView2;
            displayMonth(mMonthView2, true);
            mMonthViewFlipper.showNext();
        } else
        {
            mCurrentMonthView = mMonthView1;
            displayMonth(mMonthView1, true);
            mMonthViewFlipper.showPrevious();
        }
    }

    /**
     * SlideInNextMonthView
     */
    public void SlideInNextMonthView()
    {
        //first check to see if the next month is on the calendar
        if (!isNextMonthInCalendar())
        {
            return;
        }

        mMonthViewFlipper.setInAnimation(mSlideRightIn);
        mMonthViewFlipper.setOutAnimation(mSlideLeftOut);

        mCalendar.add(Calendar.MONTH, 1);
/*
        mCurrentMonthIndex = mCalendar.get(Calendar.MONTH);
        mCurrentYear = mCalendar.get(Calendar.YEAR);
*/

        if (mMonthView1.getVisibility() == View.VISIBLE)
        {
            mCurrentMonthView = mMonthView2;
            displayMonth(mMonthView2, true);
            mMonthViewFlipper.showNext();
        } else
        {
            mCurrentMonthView = mMonthView1;
            displayMonth(mMonthView1, true);
            mMonthViewFlipper.showPrevious();
        }
    }

    /**
     * SlideInPreviousHelpView
     */
    public void SlideInPreviousHelpView()
    {
        mHelpViewFlipper.setInAnimation(mSlideLeftIn);
        mHelpViewFlipper.setOutAnimation(mSlideRightOut);

        mHelpViewFlipper.showPrevious();
    }

    /**
     * SlideInNextHelpView
     */
    public void SlideInNextHelpView()
    {
        mHelpViewFlipper.setInAnimation(mSlideRightIn);
        mHelpViewFlipper.setOutAnimation(mSlideLeftOut);

        mHelpViewFlipper.showNext();
    }

    /**
     * SlideOutMonthView
     * <p/>
     * Called by CalendarGestureListener for a down swipe, hide the month
     */
    public void SlideOutMonthView()
    {
        mMonthViewFlipper.startAnimation(mSlideDownOut);
        UpdateFooter(new ArrayList<Note>());
        FrameLayout title = (FrameLayout) findViewById(R.id.MonthName);
        title.setVisibility(View.INVISIBLE);
    }

    /**
     * SlideInMonthView
     * <p/>
     * Called by CalendarGestureListener for an up swipe, show the month
     */
    public void SlideInMonthView()
    {
        mMonthViewFlipper.startAnimation(mSlideUpIn);
        UpdateFooter((List<Note>) mCurrentDateBtn.getTag());
        FrameLayout title = (FrameLayout) findViewById(R.id.MonthName);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * applyRotation
     *
     * @param start
     * @param end
     */
    private void applyRotation(float start, float end)
    {
        // Find the center of image
        final float centerX = mMainFlipImage.getWidth() / 2.0f;
        final float centerY = mMainFlipImage.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextFlipView(isMainFlipImage, mMainFlipImage, mHelpFlipImage));

        if (isMainFlipImage)
        {
            mMainFlipImage.startAnimation(rotation);
        } else
        {
            mHelpFlipImage.startAnimation(rotation);
        }

    }

    /**
     * ReportIOException
     *
     * @param e
     */
    private void ReportIOException(Throwable e)
    {
        String msg = e.getMessage();
        if (msg.equals(""))
        {
            msg = "Unknown Error";
        }

        ShowErrorDialog(msg);
    }

    /**
     * @param startDate the mStartDate to set
     */
/*
    public void setStartDate(String startDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date date = null;
        try
        {
            date = sdf.parse(startDate);
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        calendarInfo.setStartDate(date);
    }
*/

    /**
     * @param endDate the mEndDate to set
     */
/*
    public void setEndDate(String endDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date date = null;
        try
        {
            date = sdf.parse(endDate);
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.mEndDate.setTime(date);
    }
*/

    // end of class, go home
}