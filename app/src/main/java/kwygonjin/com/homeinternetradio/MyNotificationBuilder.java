package kwygonjin.com.homeinternetradio;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

//That class build custom notification to control radio stations via MediaPlayer
class MyNotificationBuilder {
    static final String ACTION_PREV = "ActionPrev";
    static final String ACTION_NEXT = "ActionNext";
    static final String ACTION_PAUSE_PLAY = "ActionPausePlay";
    private static final String ACTION_ACTIVITY = "ActionActivity";
    static final String POSITION_EXTRA = "currentRadioItemPosition";

    static final int NOTIFICATION_ID = 0;
    private static Context context;

    static NotificationCompat.Builder buildNotification(Context context, RadioItem radioItem, int currentRadioItemPosition) {
        MyNotificationBuilder.context = context;
        //Intent
        Intent intent = createIntent(ACTION_ACTIVITY, currentRadioItemPosition);
        Intent intentNext = createIntent(ACTION_NEXT, currentRadioItemPosition);
        Intent intentPrev = createIntent(ACTION_PREV, currentRadioItemPosition);
        Intent intentPausePlay = createIntent(ACTION_PAUSE_PLAY, currentRadioItemPosition);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent button_pIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent button_pIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent button_pIntentPausePlay = PendingIntent.getBroadcast(context, 0, intentPausePlay, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                // Set Ticker Message
                .setTicker("Ticker")
                // Dismiss Notification
                .setAutoCancel(false).setContentIntent(pIntent);

        // Build a simpler notification, without buttons
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // build a complex notification, with buttons and such
            //
            RemoteViews remoteViews = getComplexNotificationView(radioItem);
            remoteViews.setOnClickPendingIntent(R.id.status_btn_next, button_pIntentNext);
            remoteViews.setOnClickPendingIntent(R.id.status_btn_prev, button_pIntentPrev);
            remoteViews.setOnClickPendingIntent(R.id.status_btn_pausestop, button_pIntentPausePlay);
            builder = builder.setContent(remoteViews);
            builder = builder.setCustomBigContentView(remoteViews);
            //builder.setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.kissfm));
        } else {
            // Build a simpler notification, without buttons
            //
            builder = builder.setContentTitle("Simple")
                    .setContentText("simple")
                    .setSmallIcon(android.R.drawable.ic_menu_gallery);
        }

        return builder;
    }

    private static RemoteViews getComplexNotificationView(RadioItem radioItem) {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                context.getPackageName(),
                R.layout.status_bar
        );

        // Locate and set the Image into customnotificationtext.xml ImageViews
        notificationView.setImageViewResource(
                R.id.status_iv_radiologo,
                context.getResources().getIdentifier(radioItem.getImg(), "drawable", context.getPackageName()));

        // Locate and set the Text into customnotificationtext.xml TextViews
        notificationView.setTextViewText(R.id.status_tv_radioname, radioItem.getName());
        notificationView.setImageViewResource(R.id.status_btn_prev, android.R.drawable.ic_media_previous);
        notificationView.setImageViewResource(R.id.status_btn_next, android.R.drawable.ic_media_next);
        if (RadioPlayer.isPlaying)
            notificationView.setImageViewResource(R.id.status_btn_pausestop, android.R.drawable.ic_media_pause);
        else
            notificationView.setImageViewResource(R.id.status_btn_pausestop, android.R.drawable.ic_media_play);

        return notificationView;
    }

    private static Intent createIntent(String action, int currentRadioItemPosition) {
        Intent intent = new Intent(context, BCReciever.class);
        intent.setAction(action);
        intent.putExtra(POSITION_EXTRA, currentRadioItemPosition);
        return intent;
    }
}
