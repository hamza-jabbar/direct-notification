package com.ahzam.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public Button btnCreatNotification;

    //  Constants
    public final static String NOTIFICATION_REPLY = "NotificationReply";
    public final static String CHANNEL_ID = "DrNotif";
    public final static String CHANNEL_NAME = "DrNotif";
    public final static String CHANNEL_DESC = "This Application is used for the direct reply notification used in apps such as WhatsApp";

    public final static String KEY_INTENT_MORE = "keyintentmore";
    public static final String KEY_INTENT_HELP = "keyintenthelp";

    public static final int REQUEST_CODE_MORE = 100;
    public static final int REQUEST_CODE_HELP = 101;
    public static final int NOTIFICATION_ID = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreatNotification = findViewById(R.id.buttonCreateNotification);

        /*
        From Android Nougat, creating a notification channel is fardh for displaying notifications.
        Inside onCreate(), the device version is checked to see if Android N or greater to create a notification channel.
        */
        //  Check if current android version is older than or bugger than android O
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;   //  Declaration of the importance of the notification
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESC);  //  Sets the description of this channel
            mChannel.enableLights(true);    //  Allows the notification light to be shown
            mChannel.enableVibration(true); //  Allows the phone to vibrate when notification is received
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});  //  Pattern of notification
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        btnCreatNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNotification();
            }
        });
    }

    private void displayNotification() {

        //Pending intent for a notification button named More
        PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                MainActivity.this,
                REQUEST_CODE_MORE,
                new Intent(MainActivity.this, NotificationReceiver.class)
                        .putExtra(KEY_INTENT_MORE, REQUEST_CODE_MORE),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //Pending intent for a notification button help
        PendingIntent helpPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this,
                REQUEST_CODE_HELP,
                new Intent(MainActivity.this, NotificationReceiver.class)
                        .putExtra(KEY_INTENT_HELP, REQUEST_CODE_HELP),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // This object is used to allow the user to input directly to the notification
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY)
                .setLabel("Please enter your name")
                .build();

        //  For the remote input bit
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(android.R.drawable.ic_delete,
                        "Reply Now...", helpPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        //  Creating the notifiction builder object
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Hey this is DrNotif Example App...")
                .setContentText("Please share your name with us")
                .setAutoCancel(true)
                .setContentIntent(helpPendingIntent)
                .addAction(action)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .addAction(android.R.drawable.ic_menu_directions, "Help", helpPendingIntent);

        //  To display the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }
}
