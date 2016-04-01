package com.das.yacalendar.network;

import android.os.Message;

import com.das.yacalendar.UUDecode;
import com.das.yacalendar.network.BasicAPICall;
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
public class SplashServerCall extends BasicAPICall
{
        public SplashServerCall(final yacalendar main)
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
                    String str = obj.getString("splash");
                    JSONArray array = new JSONArray(str);
                    obj = array.getJSONObject(0);
                    String splashImage = obj.getString("splash");
                    blob = splashImage.getBytes();
                    in = new ByteArrayInputStream(blob);
                    out = new ByteArrayOutputStream();
                    new UUDecode(in, out);
                    in.close();
                    Message msg = main.msgHandler.obtainMessage(main.kMessageSplashImage, 0, 0, out.toByteArray());
                    main.msgHandler.sendMessage(msg);
                    out.close();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
}