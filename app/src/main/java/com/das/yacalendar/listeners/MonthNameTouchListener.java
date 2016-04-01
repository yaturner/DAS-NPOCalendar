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

package com.das.yacalendar.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.das.yacalendar.R;
import com.das.yacalendar.yacalendar;

/**
 * @author yaturner
 */
public class MonthNameTouchListener implements OnTouchListener
{
    private static final int ARROW_FADE_DURATION = 5000;

    private yacalendar mMainApp = null;

    public MonthNameTouchListener(final yacalendar mainApp)
    {
        this.mMainApp = mainApp;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    // @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView leftarrow = (ImageView) mMainApp.findViewById(R.id.leftarrow);
        ImageView rightarrow = (ImageView) mMainApp.findViewById(R.id.rightarrow);
        RelativeLayout arrows = (RelativeLayout) mMainApp.findViewById(R.id.framearrows);

        arrows.setVisibility(View.VISIBLE);
        leftarrow.setVisibility(View.INVISIBLE);
        rightarrow.setVisibility(View.INVISIBLE);

        AlphaAnimation mFadeAnim = new AlphaAnimation(1.0f, 0.0f);
        mFadeAnim.setDuration(ARROW_FADE_DURATION);
        mFadeAnim.setAnimationListener(new AnimationListener()
        {

            // //@Override
            public void onAnimationEnd(Animation animation)
            {
                ImageView leftarrow = (ImageView) mMainApp.findViewById(R.id.leftarrow);
                ImageView rightarrow = (ImageView) mMainApp.findViewById(R.id.rightarrow);
                leftarrow.setVisibility(View.INVISIBLE);
                rightarrow.setVisibility(View.INVISIBLE);
            }

            // //@Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            // @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub

            }
        });

        if (mMainApp.isPrevMonthInCalendar())
        {
            leftarrow.setVisibility(View.VISIBLE);
            leftarrow.startAnimation(mFadeAnim);
        }

        if (mMainApp.isNextMonthInCalendar())
        {
            rightarrow.setVisibility(View.VISIBLE);
            rightarrow.startAnimation(mFadeAnim);
        }

        return true;
    }
}


