/*********************************************************************************
 * *
 * D'arc Angel CONFIDENTIAL                                                      *
 * __________________                                                            *
 * *
 * [2010] D'arc Angel LLC                                                       *
 * All Rights Reserved.                                                         *
 * *
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

/**
 * @author yaturner
 */
public class Note
{
    private static final String TAG_NOTE = "NOTE";
    private SimpleFormatter sdf = new SimpleFormatter();
    private long id;
    private Calendar date;
    private int priority;
    private String text;


    public Note()
    {
        id = -1;
        date = Calendar.getInstance();
        text = "";
        priority = Constants.NOTE_PRIORITY_NONE;
    }

    public Note(final Calendar date, final int priority, final String text)
    {
        this.id = -1;
        this.date = date;
        this.priority = priority;
        this.text = text;
    }

    public Note(final long id, final Calendar date, final int priority, final String text)
    {
        this.id = id;
        this.date = date;
        this.priority = priority;
        this.text = text;
    }

    public String getDateAsString()
    {
        return yacalendar.getInstance().getSdf().format(date);
    }


    public void Save(final DataOutputStream obj_out) throws IOException
    {
//        obj_out.writeInt(priority);
//        Log.d(TAG_NOTE, "Save\\\\priority = " + priority);
//        int len = text.length();
//        obj_out.writeInt(len);
//        obj_out.writeBytes(text);
//        Log.d(TAG_NOTE, "Save\\\\len = " + len + " text = " + text);
//        len = date.length();
//        obj_out.writeInt(len);
//        obj_out.writeBytes(date);
//        Log.d(TAG_NOTE, "Save\\\\len = " + len + " date = " + date);
    }

    public void Restore(final DataInputStream obj_in) throws IOException
    {
        int len = -1;
        int red = -1;
        byte[] b = null;

//        priority = obj_in.readInt();
//        Log.d(TAG_NOTE, "Restore\\\\priority = " + priority);
//        len = obj_in.readInt();
//        b = new byte[len];
//        red = obj_in.read(b, 0, len);
//        text = new String(b);
//        Log.d(TAG_NOTE, "Restore\\\\ read = " + red + " len = " + len + " text = " + text);
//        len = obj_in.readInt();
//        b = new byte[len];
//        red = obj_in.read(b, 0, len);
//        date = new String(b);
//        Log.d(TAG_NOTE, "Restore\\\\ read = " + red + " len = " + len + " date = " + date);
    }

    public long getId()
    {
        return id;
    }

    public Calendar getDate()
    {
        return date;
    }

    public int getPriority()
    {
        return priority;
    }

    public String getText()
    {
        return text;
    }

}