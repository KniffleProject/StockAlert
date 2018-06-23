package domain.foo.stockalert;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
    private String CHANNEL_ID="some_channel_id";
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
        service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
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
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyId = 1;



        createNotificationChannel();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle("StockAlert: "+eq.getSymbol())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentText(message+limit);


        notificationManager.notify(1, mBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myChannel";
            String description = "This is my Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
