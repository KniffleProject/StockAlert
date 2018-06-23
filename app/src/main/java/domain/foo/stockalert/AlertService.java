package domain.foo.stockalert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by hechtpapst on 22.06.2018.
 */

public class AlertService {

    private Context context;
    private ArrayList<Equity> equityList;
    private ScheduledExecutorService service;
    //private Map<String,Long> above = new HashMap<String, Long>();
    //private Map<String,Long> under = new HashMap<String, Long>();


    public AlertService(ArrayList<Equity> equityList,Context context){
        this.context = context;
        this.equityList = equityList;
        getAllAlertsAbove();
        getAllAlertsUnder();
    }

    public void getAllAlertsAbove(){
        // TODO
        //above.put("MSFT", (long) 100);
    }

    public void getAllAlertsUnder(){
        // TODO
        //above.put("MSFT", (long) 90);
    }

    public void startService(){
        Runnable runnable = new Runnable() {
            public void run() {
                // task to run goes here
                if(equityList.size() != 0){
                    for (Equity eq: equityList) {
                        long priceAbove = eq.getAbove();
                        long priceUnder = eq.getUnder();
                        eq.selfUpdate();
                        if(eq.isAbove()){
                            //notify // notify schon angezeigt?
                            System.out.println("Price above!");
                            PushNotification("The price has exceeded your upper pricelimit of: ",eq);
                        }
                        if(eq.isUnder()){
                            System.out.println("Price under!");
                            PushNotification("The price has fallen under your lower pricelimit of: ",eq);
                        }
                    }
                }

                //System.out.println("Hello !!");
            }
        };

        this.service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }

    public void stopAlertService(){
        this.service.shutdown();
    }

    public void PushNotification(String message, Equity eq)
    {
        long limit;
        if(eq.isAbove()){
            limit= eq.getAbove();
        }
        else{limit = eq.getUnder();}

        String symbol = eq.getSymbol();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notifyId = 1;
        String channelId = "some_channel_id";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)

                .setContentTitle("StockAlert: "+eq.getSymbol())
                .setContentText(message+limit);


        notificationManager.notify(1, mBuilder.build());
    }
}
