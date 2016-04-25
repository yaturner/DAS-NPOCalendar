package com.das.yacalendar.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Base64;

import com.das.yacalendar.Constants;
import com.das.yacalendar.yacalendar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by yaturner on 4/2/2015.
 */
public class ImageServerCall extends BasicAPICall {
    private final static String TAG = "ImageServerCall";

    private ImageInfo imageInfo = null;



    public ImageServerCall(final yacalendar main, final ImageInfo imageInfo) {
        super(main);
        this.imageInfo = imageInfo;
    }

    @Override
    protected Void doInBackground(String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void parseResult(final String result) {
        if (result != null && result.length() > 0) {
            imageInfo.decoded = Base64.decode(result, Base64.DEFAULT);
        }

        Message msg = main.msgHandler.obtainMessage(Constants.HANDLER_MESSAGE_IMAGE, 0, 0, imageInfo);
        main.msgHandler.sendMessage(msg);
    }
}

