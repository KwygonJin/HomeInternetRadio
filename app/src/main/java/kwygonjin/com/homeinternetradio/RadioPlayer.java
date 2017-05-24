package kwygonjin.com.homeinternetradio;

import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import io.realm.Realm;

/**
 * Created by KwygonJin on 03.04.2017.
 */

//This claa create and control MediaPlayer
//TODO: need change that class to service maybe

public class RadioPlayer {
    private static MediaPlayer mediaPlayer;
    private static boolean isInPrepareAsync = false;
    private static boolean needPlay = false;
    private static String playingURL;
    private static boolean mCancel = false;
    private static RadioItem mRadioItem;
    private static RadioItem mOldRadioItem;
    private static Realm mRealm;

    //Run or stop MP, depending on it current state
    public static void runPlayer(RadioItem radioItem, Context context, int position, boolean runFromReciever){

        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        if (mOldRadioItem != null)
            mOldRadioItem.setCurrent(!mOldRadioItem.isCurrent());
        mRadioItem = radioItem;
        mOldRadioItem = mRadioItem;
        radioItem.setCurrent(!mRadioItem.isCurrent());
        mRealm.commitTransaction();

        if (mediaPlayer != null &&  mediaPlayer.isPlaying()) {
            if (mRadioItem.getURL().equals(playingURL)) {
                stop();
            }
            else {
                stop();
                initData();
                start(context, runFromReciever);
            }
        }
        //We need to check, if MP is in PrepareAsync - set mCancel,
        //If was clicked new RadioItem(radiostation) and current state MP is in PrepareAsync
        //afte MP prepared it will stop, if mCancel is true and then MP will PrepareAsync new RadioItem URL if needPlay = true
        else {
            if (isInPrepareAsync) {
                mCancel = true;
                if (!mRadioItem.getURL().equals(playingURL))
                    needPlay = true;
            }
            else {
                initData();
                start(context, runFromReciever);
            }
        }


        NotificationManagerCompat notificationManager;
        Notification n;

        notificationManager = NotificationManagerCompat.from(context);

        n = MyNotificationBuilder.buildNotification(context, radioItem, position).build();

        notificationManager.notify(MyNotificationBuilder.NOTIFICATION_ID, n);
    }

    //Initialize MP and reset it before playing new URL
    private static void initData(){
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(mRadioItem.getURL());
            playingURL = mRadioItem.getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void stop(){
        mediaPlayer.stop();
    }

    //Start MP
    private static void start(final Context context, final boolean runFromReciever){

        //When MP are in Prepared, set isInPrepareAsync true
        isInPrepareAsync = true;

        //show dialog to Userm that MP are preparing
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Buffering");
        builder.setCancelable(false);

        final AlertDialog dlg = builder.create();

        if (!runFromReciever)
            dlg.show();

        mediaPlayer.prepareAsync();

        //When MP are Prepared, set isInPrepareAsync false
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                if (mCancel) {
                    stop();
                    mCancel = false;
                    if (needPlay){
                        initData();
                        start(context, runFromReciever);
                    }
                }
                else{
                    mediaPlayer.start();
                    dlg.dismiss();
                }

                isInPrepareAsync = false;
            }
        });
    }

}
