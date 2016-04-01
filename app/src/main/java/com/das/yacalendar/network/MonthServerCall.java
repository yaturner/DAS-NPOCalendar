package com.das.yacalendar.network;

import android.os.Message;
import android.util.Log;

import com.das.yacalendar.UUDecode;
import com.das.yacalendar.yacalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by yaturner on 4/2/2015.
 */
public class MonthServerCall extends BasicAPICall
{
    private final static String TAG = "MonthServerCall";

    public MonthServerCall(final yacalendar main)
    {
        super(main);
    }

    @Override
    protected Void doInBackground(String... params)
    {
        return super.doInBackground(params);
    }

    @Override
    protected void parseResult(final String result)
    {
        byte[] blob = null;

            ByteArrayInputStream in = null;
            ByteArrayOutputStream out = null;

        if (result != null && result.length() > 0)
        {
            try
            {
                JSONObject obj = new JSONObject(result);
                String str = obj.getString("month");
                JSONArray array = new JSONArray(str);
                obj = array.getJSONObject(0);
                JSONArray name = obj.names();
                String monthName = (String) name.get(0);
                int monthNo = Integer.parseInt(monthName.substring(monthName.indexOf("_") + 1));
                String monthImage = obj.getString(monthName);
                blob = monthImage.getBytes();
                in = new ByteArrayInputStream(blob);
                out = new ByteArrayOutputStream();
                new UUDecode(in, out);
                in.close();
                Message msg = main.msgHandler.obtainMessage(main.kMessageMonthImage, monthNo, 0, out.toByteArray());
                main.msgHandler.sendMessage(msg);
                out.close();
            } catch (JSONException e)
            {
                String msg = e.getMessage();
                Log.d(TAG, msg.substring((msg.length() - 50)));
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}