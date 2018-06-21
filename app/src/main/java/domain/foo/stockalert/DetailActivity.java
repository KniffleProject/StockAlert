package domain.foo.stockalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    private String symbol;
    private Equity eq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b!=null)
        {
            symbol = (String) b.get("SYMBOL");
            setTitle(symbol);
            eq = gsonToEquity((String) b.get("EQUITY_JSON"));
        }
    }

    public Equity gsonToEquity(String json){
        Gson gson = new Gson();
        Equity eq = gson.fromJson(json,Equity.class);
        return eq;
    }
}
