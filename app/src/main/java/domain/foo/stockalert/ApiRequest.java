package domain.foo.stockalert;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Jan Paul Schulz
 * Nick Herold
 * Hauke Hoppe
 */


public class ApiRequest extends AsyncTask<String,Integer,Void> {

    private JSONObject json;
    private ApiCaller activity;

    public ApiRequest(ApiCaller caller) {
        activity=caller;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    protected Void doInBackground(String...params){
        URL url;
        try {
            url = new URL(params[0]);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            InputStream is=con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            json = new JSONObject(jsonText);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void result){
        activity.requestDone(json);
    }
}