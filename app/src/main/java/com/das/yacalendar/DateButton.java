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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.Button;

import com.das.yacalendar.notes.Note;

import java.util.List;

/**
 * @author yaturner
 */
public class DateButton extends Button
{
    final static public int HIGHTLIGHT_BY_COLOR = 1;
    final static public int HIGHTLIGHT_BY_BORDER = 2;
    final static public int HIGHTLIGHT_BY_LINE = 3;

    //These are global for all buttons
    static public int mHighlightMethod = HIGHTLIGHT_BY_LINE;
    static public int mAlpha = 0;
    static public Boolean mOutlineCell = false;

    private Boolean mSelected = false;

    /**
     * @param context
     */
    public DateButton(Context context)
    {
        super(context);
        Initialize();
    }

    /**
     * @param context
     * @param attrs
     */
    public DateButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        Initialize();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public DateButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        Initialize();
    }

    private void Initialize()
    {
        mAlpha = 128;
        mSelected = false;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //There are two considerations here:
        //  1. is this date selected, i.e. the user clicked on it
        //  2. does the date have a note attached to it

        int h = getHeight();
        int w = getWidth();
        float[] outerR = new float[]
                {0, 0, 0, 0, 0, 0, 0, 0};
        RectF inset = new RectF(2, 2, 2, 2);
        float[] innerR = new float[]
                {0, 0, 0, 0, 0, 0, 0, 0};

        Drawable d = getResources().getDrawable(R.drawable.btn_date_normal);

        List<Note> notes = (List<Note>) getTag();
        if (notes != null && notes.size() > 0)
        {
            //set priority based on first note if there is more than one for this day
            Note note = notes.get(0);

            int priority = Constants.NOTE_PRIORITY_LOW;
            if (yacalendar.SUPPORT_NOTE_PRIORITY)
            {
                priority = note.getPriority();
            }

            if (mHighlightMethod == HIGHTLIGHT_BY_COLOR)
            {
                switch (priority)
                {
                    case Constants.NOTE_PRIORITY_HIGH:
                        d = getResources().getDrawable(R.drawable.btn_date_red);
                        break;
                    case Constants.NOTE_PRIORITY_MEDIUM:
                        d = getResources().getDrawable(R.drawable.btn_date_yellow);
                        break;
                    case Constants.NOTE_PRIORITY_LOW:
                        d = getResources().getDrawable(R.drawable.btn_date_green);
                        break;
                }
            } else if (mHighlightMethod == HIGHTLIGHT_BY_BORDER)
            {
                ShapeDrawable border = new ShapeDrawable(new RoundRectShape(outerR, inset, innerR));

                switch (priority)
                {
                    case Constants.NOTE_PRIORITY_HIGH:
                        border.getPaint().setColor(0xFFFF0000);
                        break;
                    case Constants.NOTE_PRIORITY_MEDIUM:
                        border.getPaint().setColor(0xFF00FF00);
                        break;
                    case Constants.NOTE_PRIORITY_LOW:
                        border.getPaint().setColor(0xFF00FFFF);
                        break;
                }

                border.setBounds(2, 2, 10 + w, 10 + h);
                border.draw(canvas);
            } else if (mHighlightMethod == HIGHTLIGHT_BY_LINE)
            {

                ShapeDrawable line = new ShapeDrawable(new RectShape());

                switch (priority)
                {
                    case Constants.NOTE_PRIORITY_HIGH:
                        line.getPaint().setColor(0xFFFF0000);
                        break;
                    case Constants.NOTE_PRIORITY_MEDIUM:
                        line.getPaint().setColor(0xFF00FF00);
                        break;
                    case Constants.NOTE_PRIORITY_LOW:
                        line.getPaint().setColor(0xFF00FFFF);
                        break;
                }

                line.setBounds(w - 8, 8, w - 6, h - 8);

                line.draw(canvas);

            }
        }

        if (mOutlineCell)
        {
            ShapeDrawable outline = new ShapeDrawable(new RoundRectShape(outerR, inset, innerR));
            outline.getPaint().setColor(0xFF000000);
            outline.setBounds(0, 0, w, h);
            outline.draw(canvas);
        }

        if (mSelected)
        {
            d.mutate().setAlpha(mAlpha + 100);
        } else
        {
            d.mutate().setAlpha(mAlpha);
        }

        setBackgroundDrawable(d);

        super.onDraw(canvas);
    }

    /**
     * @param mSelected the mSelected to set
     */
    public void setBtnAsSelected(Boolean mSelected)
    {
        this.mSelected = mSelected;
        this.invalidate();
    }

}
