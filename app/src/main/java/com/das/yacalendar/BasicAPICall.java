package com.das.yacalendar;

import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by yaturner on 3/18/2015.
 */
public abstract class BasicAPICall extends AsyncTask<String, Void, Object>
{

    protected yacalendar main = null;

    public BasicAPICall(final yacalendar main)
    {
        this.main = main;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(String... params)
    {
        String urlString = params[0]; // URL to call
        String response = null;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;

        // appending params to url
        HttpGet httpGet = new HttpGet(urlString);
        httpGet.setHeader("Authorization", "Basic " + new String(Base64.encode("yaturner:1qaz@WSX".getBytes(), Base64.NO_WRAP)));

        try
        {
            httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if(response != null && response.length() > 0)
        {
            parseResult(response);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result)
    {
        super.onPostExecute(result);
    }

    abstract void parseResult(final String result);

}

