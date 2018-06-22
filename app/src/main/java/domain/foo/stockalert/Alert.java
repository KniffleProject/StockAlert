package domain.foo.stockalert;

//written by nick

public class Alert {
    long alert_id;
    long upper;
    long lower;
    //Boolean alert;
    long stock_id;

    public  Alert (long alert_id, long upper, long lower, long stock_id){
        this.alert_id = alert_id;
        this.upper = upper;
        this.lower = lower;
        //this.alert = alert;
        this.stock_id = stock_id;

    }

    public long getID() {return alert_id;}

    public void setID(long alert_id) {this.alert_id = alert_id;}

    //public Boolean getAlert() {return alert;}

    //public void setAlert(Boolean alert) { this.alert = alert; }

    public long getStockID() {return stock_id;}

    public void setStock_id(String symbol) { this.stock_id = stock_id; }

    public long getLower(){return lower;}

    public long getUpper(){return upper;}
}
