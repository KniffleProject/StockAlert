package domain.foo.stockalert;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick(View view) {
        getQuotes();
    }

    public void getQuotes(String interval, String symbol) {
        interval = "TIME_SERIES_MONTHLY";
        symbol = "MSFT";
        ApiRequest ar = new ApiRequest(MainActivity.this);
        ar.execute("https://www.alphavantage.co/query?function=+"interval+"&symbol="+symbol+"&apikey=demo");
        System.out.println("String");
    }

    public void requestDone(JSONObject q){
        JSONObject t = q;
        try {
            JSONObject o = (JSONObject) t.get("Monthly Time Series");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print("");
    }
}
