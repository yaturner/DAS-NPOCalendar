package com.das.yacalendar.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.das.yacalendar.UUDecode;
import com.das.yacalendar.yacalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by yaturner on 4/2/2015.
 */
public class MonthServerCall extends BasicAPICall {
    private final static String TAG = "MonthServerCall";

    private int monthNumber = -1;
    private Bitmap bitmap = null;

    public MonthServerCall(final yacalendar main, final int monthNumber) {
        super(main);
        this.monthNumber = monthNumber;
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
        Message msg = main.msgHandler.obtainMessage(main.kMessageMonthImage, monthNumber, 0, bitmap);
        main.msgHandler.sendMessage(msg);
    }
}

