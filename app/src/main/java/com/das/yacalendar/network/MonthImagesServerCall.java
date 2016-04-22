package com.das.yacalendar.network;

import android.os.Message;

import com.das.yacalendar.calendar.CalendarInfo;
import com.das.yacalendar.yacalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yaturner on 3/30/2015.
 */
public class MonthImagesServerCall extends BasicAPICall {
    private ArrayList<String> imageNames = new ArrayList<String>();

    public MonthImagesServerCall(final yacalendar main) {
        super(main);
    }

    @Override
    protected Void doInBackground(String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
    }

    protected void parseResult(final String result) {

        if (result != null && result.length() > 0) {
            try {
                JSONArray resultObject = new JSONArray(result);
                JSONObject images = resultObject.getJSONObject(0);
                JSONArray filenames = images.getJSONArray("images");
                for (int index = 0; index < filenames.length(); index++) {
                    String[] values = filenames.getString(index).split(",");
                    String name = values[0].substring(values[0].lastIndexOf("/") + 1);
                    if (!name.equalsIgnoreCase("splash.png")) {
                        imageNames.add(name);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Message msg = main.msgHandler.obtainMessage(main.HANDLER_MESSAGE_MONTH_IMAGE_NAMES);
        msg.obj = (Object) imageNames;
        main.msgHandler.sendMessage(msg);

    }
}
