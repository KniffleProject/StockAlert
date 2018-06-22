package domain.foo.stockalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.*;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private String symbol = "";
    private ArrayList<Equity> equityList;
    private CustomAdapter lvAdapter;
    private ListView equityListView;
    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource = new DataSource(this);
        dataSource.open();
    }
    @Override
    protected void onStart() {
        super.onStart();
        createAdapter();
    }

    public void createAdapter() {
        equityList = (ArrayList<Equity>) dataSource.getAllEquities();

        lvAdapter = new CustomAdapter(equityList, getApplicationContext());
        equityListView = (ListView) findViewById(R.id.equityList);
        equityListView.setAdapter(lvAdapter);

        equityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                Equity equity = (Equity) parent.getAdapter().getItem(position);
                String symbol = equity.getSymbol();
                Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
                detailActivity.putExtra("SYMBOL", symbol);
                detailActivity.putExtra("EQUITY_JSON", equity.gsonMe());
                startActivityForResult(detailActivity,1);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        String eqgson=data.getStringExtra("EQ");
        if(eqgson!=null && eqgson !=""){
            Gson gson = new Gson();
            Equity eq = gson.fromJson(eqgson, Equity.class);
            dataSource.deleteEquity(eq);
            equityList.remove(eq);
            createAdapter();
        }
    }


    /***********************************
     * interval: TIME_SERIES_MONTHLY, TIME_SERIES_INTRADAY ...
     * symbol: Aktienkürzel z.B. AAPL, MSTF, ...
     *
     * https://www.alphavantage.co/documentation/
     * *********************************/
    public void getQuotes(String interval, String symbol) {
        ApiRequest ar = new ApiRequest(MainActivity.this);
        ar.execute("https://www.alphavantage.co/query?function=" + interval + "&symbol=" + symbol + "&apikey=CEVLFXSSNL84YSA5");
        System.out.println("String");
    }

    /*************************************
     * Wird aufgerufen sobald Daten da sind
     * **********************************/
    public void requestDone(JSONObject q) {
        String interval = "";
        String debugHelper = "";
        String response = (String) q.toString();
        if (q != null && !response.contains("Error Message")) {
            Equity eq = new Equity(q);
            Toast.makeText(MainActivity.this, "Course(" + eq.getLatestDate() + ") is " + eq.getLatestClose() + " at " + eq.getLatestInterval() + " interval.", Toast.LENGTH_LONG).show();
            equityList.add(eq);
            lvAdapter.notifyDataSetChanged();
            dataSource.createEquity(eq.getSymbol(), eq.getPrice(), eq.getDate(), eq.getTimezone());

        } else {
            Toast.makeText(MainActivity.this, "Symbol not found.", Toast.LENGTH_LONG).show();
        }
    }

    private void addSymbol() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The name of the Equity of your choice. For example: MSFT");

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
                symbol = input.getText().toString();
                getQuotes("TIME_SERIES_MONTHLY", symbol);
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
