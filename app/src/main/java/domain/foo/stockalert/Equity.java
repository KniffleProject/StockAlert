package domain.foo.stockalert;

/**
 * Created by hechtpapst on 19.06.2018.
 */

public class Equity {

    private String symbol;
    private double price;
    private long id;
    private String date;
    private String timezone;


    public Equity(long id, String symbol, double price, String date, String timezone){
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.date = date;
        this.timezone = timezone;
    }

    public long getId(){return id;}


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
