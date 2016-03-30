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

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * @author yaturner
 */
class CalendarGestureListener extends SimpleOnGestureListener
{
    // swipe gesture constants
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 400;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private yacalendar mMainApp = null;
    private Calendar mCalendar = null;

    public CalendarGestureListener(final yacalendar mainApp)
    {
        this.mMainApp = mainApp;
        mCalendar = mainApp.mCalendar;
    }

    @Override
    //Currently this has to be overriden to return true for fling to work
    public boolean onDown(MotionEvent e)
    {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {

        Boolean monthIsVisible = (mMainApp.mCurrentMonthView.getVisibility() == View.VISIBLE);

/*		if( Math.abs( e1.getY() - e2.getY() ) > SWIPE_MAX_OFF_PATH )
        {
			return false;
		} else*/
        if (monthIsVisible)
        // Only recognize these gestures if the month view is visible
        {
            // right to left swipe, show the previous month
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                mMainApp.SlideInNextMonthView();
            }
            // left to right swipe, show the next month
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                mMainApp.SlideInPreviousMonthView();
            }
            // top to bottom swipe, hide the calendar
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                mMainApp.SlideOutMonthView();
            }
        } else
        // Only recognize this gesture is the month is hidden ( offscreen )
        {
            // bottom to top swipe, show the calendar
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                mMainApp.SlideInMonthView();
            }
        }
        return true;

    }
}
