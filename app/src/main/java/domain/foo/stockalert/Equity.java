package domain.foo.stockalert;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by hechtpapst on 19.06.2018.
 */

public class Equity {

    private String symbol;
<<<<<<< HEAD
    private String latesstClose;
    private String latestDate;
    private String lastRefreshed="";
    private String timeZone="";
    private String latestInterval="";
    private long id;

    private ArrayList<Observation> stock= new ArrayList<>();

    public String getLatestDate() {
        return latestDate;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public String getLatestInterval() {
        return latestInterval;
    }


    class Observation{
        double open;
        double close;
        double high;
        double low;
        double volume;
        String datetime;

        //Datetime fehlt. Einheitliches Format wäre günstig. Ich weiß noch nicht wie die Graphbibliothek das haben möchte.
        public Observation(double open, double high, double low, double close, double volume){
            this.open = open;
            this.close = close;
            this.low=low;
            this.high=high;
            this.volume=volume;
        }
    }


    public Equity(String symbol, String price) {
        this.symbol = symbol;
        this.latestClose = price;
    }

    public Equity(JSONObject response){
    public Equity(JSONObject response) {
        if (response != null) {
            fillFromJson(response);
        }
    }
    private double price;
    private long id;
    private String date;


    public Equity(long id, String symbol, double latestClose, String latestDate, String timezone){
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.date = date;
        this.timezone = timezone;
        this.latestClose = ""+ latestClose;
        this.latestDate = latestDate;
        this.timeZone = timezone;

    }

    public long getId(){return id;}


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
                Observation newEntry = new Observation(open,high,low,close,volume);
                stock.add(newEntry);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        latestClose =""+stock.get(1).close;
        latestDate ="Platzhalter"; //+stock.get(1).datetime;

        }

    public double getPrice() {
    public String getPrice() {
        return latestClose;
    }

    public void setPrice(double price) {
        this.price = price;
>>>>>>> 6c6dd0c4e5a83566984601d17e6d71aa1723e871

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
