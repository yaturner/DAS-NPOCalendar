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

/**
 * @author yaturner
 *
 */

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;

public final class SwapFlipViews implements Runnable
{
    View mFlipImage1;
    View mFlipImage2;
    private boolean mIsMainView;

    public SwapFlipViews(boolean isMainView, View mFlipImage1, View mFlipImage2)
    {
        mIsMainView = isMainView;
        this.mFlipImage1 = mFlipImage1;
        this.mFlipImage2 = mFlipImage2;
    }

    public void run()
    {
        final float centerX = mFlipImage1.getWidth() / 2.0f;
        final float centerY = mFlipImage1.getHeight() / 2.0f;
        Flip3dAnimation rotation;

        if (mIsMainView)
        {
            rotation = new Flip3dAnimation(-90, 0, centerX, centerY);
            rotation.setAnimationListener(new AnimationListener()
            {

                public void onAnimationEnd(Animation animation)
                {
                    mFlipImage1.setVisibility(View.GONE);
                    mFlipImage2.setVisibility(View.VISIBLE);
                    //this MUST be done so that the help screen gets events
                    mFlipImage2.requestFocus();
                    mFlipImage2.bringToFront();
                }

                public void onAnimationRepeat(Animation animation)
                {
                    // TODO Auto-generated method stub

                }

                public void onAnimationStart(Animation animation)
                {
                    // TODO Auto-generated method stub

                }
            });
        } else
        {
            rotation = new Flip3dAnimation(90, 0, centerX, centerY);
            rotation.setAnimationListener(new AnimationListener()
            {

                public void onAnimationEnd(Animation animation)
                {
                    mFlipImage2.setVisibility(View.GONE);
                    mFlipImage1.setVisibility(View.VISIBLE);
                    //this must be done to keep the help screen from getting events
                    mFlipImage1.requestFocus();
                    mFlipImage1.bringToFront();
                }

                public void onAnimationRepeat(Animation animation)
                {
                    // TODO Auto-generated method stub

                }

                public void onAnimationStart(Animation animation)
                {
                    // TODO Auto-generated method stub

                }
            });
        }

        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());

        if (mIsMainView)
        {
            mFlipImage2.startAnimation(rotation);
        } else
        {
            mFlipImage1.startAnimation(rotation);
        }
    }
}
