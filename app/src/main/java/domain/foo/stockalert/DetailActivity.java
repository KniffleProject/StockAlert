package domain.foo.stockalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesLine;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.MarkerType;
import com.anychart.anychart.Set;
import com.anychart.anychart.Stroke;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String symbol;
    private Equity eq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            symbol = (String) b.get("SYMBOL");
            setTitle(symbol);
            eq = gsonToEquity((String) b.get("EQUITY_JSON"));
            eq.sortStockByDate();
            anyChartView.setChart(buildCartesian(eq));
        }
    }

    public Equity gsonToEquity(String json) {
        Gson gson = new Gson();
        Equity eq = gson.fromJson(json, Equity.class);
        return eq;
    }


    private Set makeSeriesData(Equity eq) {
        ArrayList<DataEntry> dataSeries = new ArrayList<>();
        ArrayList<Equity.Observation> stock = eq.getStock();
        for (Equity.Observation obs : stock
                ) {
            dataSeries.add(new CustomDataEntry(obs.datetime.toString(), obs.close));
        }
        return new Set(dataSeries);
    }

    private class CustomDataEntry extends ValueDataEntry {
        // je nach dem was bzw. wie viel man darstellen möchte kann die DataEntry Klasse verändert werden.
        CustomDataEntry(String x, double value) { //, double value2, double value3) {
            super(x, value);
//            setValue("value2", value2);
//            setValue("value3", value3);
        }

    }


    private Cartesian buildCartesian(Equity eq) {
        Cartesian cartesian = AnyChart.line();

        cartesian.setAnimation(true);

        cartesian.setPadding(10d, 20d, 5d, 20d);

        cartesian.getCrosshair().setEnabled(true);
        cartesian.getCrosshair()
                .setYLabel(true)
                .setYStroke((Stroke) null, null, null, null, null);

        cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
        cartesian.getYAxis().setTitle("share value in €");
        cartesian.setTitle("Microsoft Aktien");
        cartesian.getXAxis().getLabels().setPadding(5d, 5d, 5d, 5d);
        Set set = makeSeriesData(eq);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
//        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
//        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
        CartesianSeriesLine series1 = cartesian.line(series1Mapping);
        series1.setName(eq.getSymbol());
        series1.getHovered().getMarkers().setEnabled(true);
        series1.getHovered().getMarkers()
                .setType(MarkerType.CIRCLE)
                .setSize(4d);
        series1.getTooltip()
                .setPosition("right")
                .setAnchor(EnumsAnchor.LEFT_CENTER)
                .setOffsetX(5d)
                .setOffsetY(5d);
        cartesian.getLegend().setEnabled(true);
        cartesian.getLegend().setFontSize(13d);
        cartesian.getLegend().setPadding(0d, 0d, 10d, 0d);



        return cartesian;
    }


}
