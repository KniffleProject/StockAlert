package domain.foo.stockalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    private String symbol;

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
        }
    }
}
