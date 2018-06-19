package domain.foo.stockalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) !=
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // Inflate the main_menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main_menu, menu);
            // Find the menuItem to add your SubMenu
            MenuItem myMenuItem = menu.findItem(R.id.menuEntryAdd);
        }
        return true;
    }
    /***********************************
     * interval: TIME_SERIES_MONTHLY, TIME_SERIES_INTRADAY ...
     * symbol: Aktienkürzel z.B. AAPL, MSTF, ...
     *
     * https://www.alphavantage.co/documentation/
     * *********************************/
    public void getQuotes(String interval, String symbol) {
        ApiRequest ar = new ApiRequest(MainActivity.this);
        ar.execute("https://www.alphavantage.co/query?function="+interval+"&symbol="+symbol+"&apikey=CEVLFXSSNL84YSA5");
        System.out.println("String");
    }

    /*************************************
     * Wird aufgerufen sobald Daten da sind
     * **********************************/
    public void requestDone(JSONObject q){
        String interval="";
        try {
            Iterator<String> jsonInfo = q.keys();
            while(jsonInfo.hasNext()) {
                interval = (String) jsonInfo.next();
            }
            JSONObject dates = (JSONObject) q.get(interval);
            Iterator<String> b = dates.keys();
            //while(b.hasNext()) {
            //    System.out.println( (String)b.next());
            //}

            String date = (String)b.next();
            JSONObject values = (JSONObject) dates.get(date);
            Toast.makeText(MainActivity.this, "Course("+ date +") is "+values.get("1. open")+" at "+interval+" interval.",Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Symbol not found.",Toast.LENGTH_LONG).show();
        }
        System.out.print("");
    }

    private void addSymbol(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The name of the equity of your choice. For example: MSFT");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input.setText("MSFT");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                getQuotes("TIME_SERIES_MONTHLY",m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void clickMenuEntryAdd(MenuItem item) {
        addSymbol();
    }
}
