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

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.das.yacalendar.yacalendar;

/**
 * @author yaturner
 */
public class SlideOutAnimationListener implements AnimationListener
{
    private yacalendar mMainApp = null;

    public SlideOutAnimationListener(final yacalendar mainApp)
    {
        this.mMainApp = mainApp;
    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation)
     */
    public void onAnimationEnd(Animation animation)
    {
        mMainApp.mCurrentMonthView.setVisibility(View.INVISIBLE);

    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationRepeat(android.view.animation.Animation)
     */
    public void onAnimationRepeat(Animation animation)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
     */
    public void onAnimationStart(Animation animation)
    {
        // TODO Auto-generated method stub

    }

}
