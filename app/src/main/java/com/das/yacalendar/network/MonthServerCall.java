package com.das.yacalendar.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Base64;

import com.das.yacalendar.yacalendar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by yaturner on 4/2/2015.
 */
public class MonthServerCall extends BasicAPICall {
    private final static String TAG = "MonthServerCall";

    private Bitmap bitmap = null;
    private Pattern patten = Pattern.compile("^.+month(\\d+)\\.png$");

    public MonthServerCall(final yacalendar main) {
        super(main);
    }

    @Override
    protected Void doInBackground(String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void parseResult(final String result) {
        if (result != null && result.length() > 0) {
            byte[] decodedString = Base64.decode(result, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            bitmap = null;
        }
        Matcher m = patten.matcher(urlString);
        m.find();
        String monthNumber = m.group(1);
        Message msg = main.msgHandler.obtainMessage(main.HANDLER_MESSAGE_MONTH_IMAGE, Integer.parseInt(monthNumber), 0, bitmap);
        main.msgHandler.sendMessage(msg);
    }
}

