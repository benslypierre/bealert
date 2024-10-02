package com.example.bealert.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.bealert.AlarmActivity;
import com.example.bealert.Main2Activity;
import com.example.bealert.R;

public class ProximityReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 500;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        System.out.println(key);

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Toast.makeText(context, "Entering", Toast.LENGTH_LONG).show();
            createNotificationChannel(context);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, Main2Activity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentTitle("BeAlert")
                    .setContentText("You are approaching your destination")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .build();



            notificationManager.notify(NOTIFICATION_ID, notification);


        /*Intent alarmIntent = new Intent(context, AlarmBroadcastReceiver.class);
        alarmIntent.putExtra("TITLE", "Destination Approaching");
        alarmIntent.putExtra("DESC", "This is your reminder");
        alarmIntent.putExtra("DATE", "Happening Now");
        alarmIntent.putExtra("TIME", " ");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,0, alarmIntent, PendingIntent.FLAG_MUTABLE);
        IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        context.registerReceiver(new AlarmBroadcastReceiver(), filter);*/
            Toast.makeText(context,"Alarm Added",Toast.LENGTH_SHORT).show();


            String title, desc, date, time;
            title ="Destination Approaching";
            desc = "This is your reminder";
            date = "Happening Now";
            time = "";

            Intent i = new Intent(context, AlarmActivity.class);
            i.putExtra("TITLE", title);
            i.putExtra("DESC", desc);
            i.putExtra("DATE", date);
            i.putExtra("TIME", time);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else {
            Toast.makeText(context, "Exiting", Toast.LENGTH_LONG).show();
        }


    }
    private void createNotificationChannel(Context context) {
        //Build.VERSION.SDK_INT returns the API Level of the device where the app is installed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,"Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }

}
