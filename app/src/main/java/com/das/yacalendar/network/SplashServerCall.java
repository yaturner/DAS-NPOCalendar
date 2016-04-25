package com.das.yacalendar.network;

import android.os.Message;
import android.util.Base64;

import com.das.yacalendar.Constants;
import com.das.yacalendar.yacalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by yaturner on 4/2/2015.
 */
public class SplashServerCall extends BasicAPICall {
    public SplashServerCall(final yacalendar main) {
        super(main);
    }

    @Override
    protected Void doInBackground(String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void parseResult(final String result) {
        byte[] blob = null;
        int flags = Base64.DEFAULT;

        if (result != null && result.length() > 0) {
                blob = result.getBytes();
                byte[] decoded = Base64.decode(blob, flags);
                Message msg = main.msgHandler.obtainMessage(Constants.HANDLER_MESSAGE_SPLASH_IMAGE,
                        0, 0, decoded);
                main.msgHandler.sendMessage(msg);
            }
        }
    }