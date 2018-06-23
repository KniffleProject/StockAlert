package domain.foo.stockalert;

import android.support.annotation.NonNull;

import com.anychart.anychart.DateTime;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;


/**
 * Created by hechtpapst on 22.06.2018.
 */

public class Equity implements ApiCaller {

    private String symbol;
    private double latestClose;
    private String latestDate;
    private String lastRefreshed = "";
    private String timeZone = "";
    private String latestInterval = "";
    private String apiRequestCurrentShare = "";
    private long id;
    private boolean isAbove = false;
    private boolean isUnder = false;

    public void setAbove(long above) {
        this.above = above;
    }

    public void setUnder(long under) {
        this.under = under;
    }

    private long above = -1;
    private long under = -1;

    private ArrayList<Observation> stock = new ArrayList<>();

    public long getAbove() {
        return above;
    }

    public boolean isAbove() {
        return isAbove;
    }

    public boolean isUnder() {
        return isUnder;
    }

    public long getUnder() {
        return under;
    }

    public void selfUpdate() {
        ApiRequest ar = new ApiRequest(this);
        ar.execute(apiRequestCurrentShare);
    }


    @Override
    public void requestDone(JSONObject json) {
        if (json != null) {
            System.out.println(json.toString());
            if (!json.toString().contains("Error Message") && !json.toString().contains("call frequency")) {
                try {
                    Iterator<String> response = json.keys();
                    String interval = "";
                    while (response.hasNext()) {
                        interval = (String) response.next();
                        if (interval.equals("Meta Data")) {
                            JSONObject meta = (JSONObject) json.get(interval);
                            lastRefreshed = (String) meta.get("3. Last Refreshed");
                        }
                    }
                    JSONObject dataSeries = null;
                    dataSeries = (JSONObject) json.get(interval);
                    if (interval.contains("Time Series")) {
                        latestInterval = interval;
                        updateObservations(dataSeries,false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    class Observation implements Comparable<Observation> {
        double open;
        double close;
        double high;
        double low;
        double volume;
        Date datetime;


        public Observation(double open, double high, double low, double close, double volume, String datetime) {
            this.open = open;
            this.close = close;
            this.low = low;
            this.high = high;
            this.volume = volume;
            if (datetime.length() < 11) {
                datetime += " 00:00:00";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                this.datetime = formatter.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public String printDatetime() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(datetime);
        }

        @Override
        public int compareTo(@NonNull Observation o) {
            return this.datetime.compareTo(o.datetime);
        }
    }


    public void sortStockByDate() {
        Collections.sort(stock, new Comparator<Observation>() {
            @Override
            public int compare(Observation o1, Observation o2) {
                return o1.compareTo(o2);
            }
        });
    }


    public Equity(JSONObject response) {
        if (response != null) {
            fillFromJson(response);
        }
    }


    public Equity(long id, String symbol, double latestClose, String latestDate, String timezone) {
        this.id = id;
        this.symbol = symbol;
        this.latestClose = latestClose;
        this.latestDate = latestDate;
        this.timeZone = timezone;
        this.apiRequestCurrentShare = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol +
                "&interval=1min&outputsize=compact&apikey=CEVLFXSSNL84YSA5"; //Api Request der letzten 100 min für diese Equity

    }

    public long getId() {
        return id;
    }

    public String getLatestDate() {
        return latestDate;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public String getLatestInterval() {
        return latestInterval;
    }

    public ArrayList<Observation> getStock() {
        return stock;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLatestClose() {
        return latestClose;
    }


    public String gsonMe() {
        String json = "";
        Gson gson = new Gson();
        json = gson.toJson(this);
        return json;
    }

    public void fillFromJson(JSONObject json) {
        try {
            String interval = "";
            Iterator<String> response = json.keys();
            while (response.hasNext()) {
                interval = response.next();
                if (interval.equals("Meta Data")) {
                    JSONObject meta = (JSONObject) json.get(interval);
                    symbol = (String) meta.get("2. Symbol");
                    apiRequestCurrentShare = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol +
                            "&interval=1min&outputsize=compact&apikey=CEVLFXSSNL84YSA5"; //Api Request der letzten 100 min für diese Equity
                    lastRefreshed = (String) meta.get("3. Last Refreshed");
                    timeZone = (String) meta.get("4. Time Zone");
                }
            }
            JSONObject dataSeries = (JSONObject) json.get(interval);
            if (interval.contains("Time Series")) {
                latestInterval = interval;
                updateObservations(dataSeries,true);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
        latestClose = stock.get(1).close;
        latestDate = stock.get(1).printDatetime();

    }

    public boolean updateObservations(JSONObject dataSeries, boolean keepData) {
        Iterator<String> observations = dataSeries.keys();
        boolean success = true;
        try {
            while (observations.hasNext()) {
                JSONObject observationData = null;
                String datetime = observations.next();
                observationData = (JSONObject) dataSeries.get(datetime);
                Iterator<String> keys = observationData.keys();
                double open = 0;
                double close = 0;
                double high = 0;
                double low = 0;
                double volume = 0;
                while (keys.hasNext()) {
                    String key = keys.next();
                    switch (key) {
                        case "1. open":
                            open = Double.parseDouble((String) observationData.get(key));
                            break;
                        case "2. high":
                            high = Double.parseDouble((String) observationData.get(key));
                            break;
                        case "3. low":
                            low = Double.parseDouble((String) observationData.get(key));
                            break;
                        case "4. close":
                            close = Double.parseDouble((String) observationData.get(key));
                            break;
                        case "5. volume":
                            volume = Double.parseDouble((String) observationData.get(key));
                            break;
                    }
                }
                if ((above != -1) && (high > above)) isAbove = true;
                if ((under != -1) && (low < under)) isUnder = true;
                if(keepData) {
                    Observation newEntry = new Observation(open, high, low, close, volume, datetime);
                    stock.add(newEntry);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public void addObservation(double open, double high, double low, double close, double volume, String datetime) {
        Observation newEntry = new Observation(open, high, low, close, volume, datetime);
        stock.add(newEntry);
    }

    public double getPrice() {
        return latestClose;
    }

    public String getDate() {
        return latestDate;
    }

    public String getTimezone() {
        return timeZone;
    }

    public void resetLimits() {
        above = -1;
        under = -1;
        isAbove=false;
        isUnder=false;
    }


}
