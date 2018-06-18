package com.ahzam.example.myapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.widget.Toast;

/*
* Created by Hamza Jabbar 26/05/2018.
*/

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //  Get the remote input from bundle
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        //  If there is some input
        if(remoteInput != null) {
            //  Get input value
            CharSequence name = remoteInput.getCharSequence(MainActivity.NOTIFICATION_REPLY);

            //  Update notification with the input value
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_info_details)
                    .setContentTitle("Hey Thanks, " + name);

            NotificationManager notificationManager = (NotificationManager) context.
                    getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(MainActivity.NOTIFICATION_ID, mBuilder.build());
            }

            //  if help button is clicked
            if(intent.getIntExtra(MainActivity.KEY_INTENT_HELP, -1) == MainActivity.REQUEST_CODE_HELP) {
                Toast.makeText(context, "You clicked Help", Toast.LENGTH_LONG).show();
            }

            //  If the more button is clicked
            if(intent.getIntExtra(MainActivity.KEY_INTENT_MORE, -1) == MainActivity.REQUEST_CODE_MORE) {
                Toast.makeText(context, "You clicked More", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
