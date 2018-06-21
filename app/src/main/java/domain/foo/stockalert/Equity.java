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
 * Created by hechtpapst on 19.06.2018.
 */

public class Equity {

    private String symbol;
    private String latestClose;
    private String latestDate;
    private String lastRefreshed="";
    private String timeZone="";
    private String latestInterval="";
    private long id;

    private ArrayList<Observation> stock= new ArrayList<>();

    class Observation implements Comparable<Observation>{
        double open;
        double close;
        double high;
        double low;
        double volume;
        Date datetime;


        public Observation(double open, double high, double low, double close, double volume, String datetime){
            this.open = open;
            this.close = close;
            this.low=low;
            this.high=high;
            this.volume=volume;
            if (datetime.length()<11){
                datetime+=" 00:00:00";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                this.datetime = formatter.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int compareTo(@NonNull Observation o) {
            return this.datetime.compareTo(o.datetime);
        }
    }


    public void sortStockByDate(){
        Collections.sort(stock, new Comparator<Observation>(){
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


    public Equity(long id, String symbol, double latestClose, String latestDate, String timezone){
        this.id = id;
        this.symbol = symbol;
        this.latestClose = ""+ latestClose;
        this.latestDate = latestDate;
        this.timeZone = timezone;

    }

    public long getId(){return id;}

    public String getLatestDate() {
        return latestDate;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public String getLatestInterval() {
        return latestInterval;
    }

    public ArrayList<Observation> getStock(){
        return stock;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLatestClose() {
        return latestClose;
    }

    public void setPrice(String price) {
        this.latestClose = price;
    }

    public String gsonMe(){
        String json ="";
        Gson gson = new Gson();
        json = gson.toJson(this);
        return json;
    }

    public void fillFromJson(JSONObject json) {
        try {
            String interval ="";
            String timeseries ="";
            Iterator<String> response = json.keys();
            while (response.hasNext()) {
                interval = (String) response.next();
                if(interval.equals("Meta Data")){
                    JSONObject meta = (JSONObject) json.get(interval);
                    symbol=(String) meta.get("2. Symbol");
                    lastRefreshed= (String) meta.get("3. Last Refreshed");
                    timeZone =(String) meta.get("4. Time Zone");
                }
            }
            JSONObject dataSeries = (JSONObject) json.get(interval);
            latestInterval=interval;
            Iterator<String> observations = dataSeries.keys();
            while (observations.hasNext()) {
                JSONObject observationData = null;
                String datetime=observations.next();
                observationData = (JSONObject) dataSeries.get(datetime);
                Iterator<String> keys = observationData.keys();
                double open=0;
                double close=0;
                double high=0;
                double low=0;
                double volume=0;
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
                Observation newEntry = new Observation(open,high,low,close,volume, datetime);
                stock.add(newEntry);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        latestClose =""+stock.get(1).close;
        latestDate ="Platzhalter"; //+stock.get(1).datetime;

        }

    public void addObservation(Observation obs){
        stock.add(obs);
    }

    public String getPrice() {
        return latestClose;
    }


    public String getDate() {
        return latestDate;
    }

    public void setDate(String date) {
        this.latestDate = date;
    }

    public String getTimezone() {
        return timeZone;
    }

    public void setTimezone(String timezone) {
        this.timeZone = timezone;
    }
}
