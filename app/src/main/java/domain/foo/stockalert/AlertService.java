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
 * Jan Paul Schulz
 * Nick Herold
 * Hauke Hoppe
 */

public class AlertService {

    private Context context;
    private DataSource dataSource;
    private ArrayList<Equity> equityList;
    private ScheduledExecutorService service;
    //private Map<String,Long> above = new HashMap<String, Long>();
    private Map<Equity, String> CHANNEL_IDS = new HashMap<Equity,String>();
    //private Map<String,Long> under = new HashMap<String, Long>();


    public AlertService(ArrayList<Equity> equityList,Context context, DataSource dataSource){
        this.context = context;
        this.equityList = equityList;
        this.dataSource=dataSource;
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
                ArrayList<Alert> alertArray = (ArrayList<Alert>) dataSource.getAllAlerts();
                if(alertArray.size() != 0) {
                    if (equityList.size() != 0) {
                        for (Equity eq : equityList) {
                            long priceAbove = eq.getAbove();
                            long priceUnder = eq.getUnder();
                            eq.selfUpdate();
                            if (eq.isAbove()) {
                                //notify // notify schon angezeigt?
                                System.out.println("Price above!");
                                PushNotification("The price has exceeded your upper pricelimit of: ", eq);
                            }
                            if (eq.isUnder()) {
                                System.out.println("Price under!");
                                PushNotification("The price has fallen under your lower pricelimit of: ", eq);
                            }
                        }
                    }
                }else{
                    stopAlertService();
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



        createNotificationChannel(eq);
        Intent intent_notification = new Intent(context, MainActivity.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_notification, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_IDS.get(eq))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle("StockAlert: "+eq.getSymbol())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .setContentText(message+limit);


        notificationManager.notify((int)eq.getId(), mBuilder.build());
        dataSource.deleteAlert(eq);
        eq.resetLimits();

    }

    private void createNotificationChannel(Equity equity) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myChannel";
            String description = "This is my Channel";
            String CHANNEL_ID="channel_for_" + equity.getSymbol();
            if(!CHANNEL_IDS.containsKey(equity)){
                CHANNEL_IDS.put(equity,CHANNEL_ID);
            }
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
