package org.me.gcu.equakestartercode.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.me.gcu.equakestartercode.interfaces.IAsyncResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetXMLString extends AsyncTask<String , Void , String >
{
    private IAsyncResponse listener;
    public GetXMLString(IAsyncResponse listener)
    {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        String url = strings[0];
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";

        try {
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;
            }
            in.close();
            return result;

        } catch (IOException ae) {
            Log.e("MyTag", "ioexception in run");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        listener.returnXML(s);
    }
}
