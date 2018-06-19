package domain.foo.stockalert;

/**
 * Created by hechtpapst on 19.06.2018.
 */

public class Equity {

    private String symbol;
    private String price;

    public Equity(String symbol, String price){
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
