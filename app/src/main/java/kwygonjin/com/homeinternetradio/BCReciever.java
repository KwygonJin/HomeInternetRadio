package kwygonjin.com.homeinternetradio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by KwygonJin on 19.05.2017.
 */

public class BCReciever extends BroadcastReceiver {
    private int currentRadioItemPosition;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (RecyclerViewAdapter.radioItemList == null)
            return;

        int size = RecyclerViewAdapter.radioItemList.size();
        currentRadioItemPosition = intent.getIntExtra(MyNotificationBuilder.POSITION_EXTRA, -1);
        if (currentRadioItemPosition == -1)
            return;

        if (intent.getAction().equals(MyNotificationBuilder.ACTION_PREV)){
            if (currentRadioItemPosition == 0)
                currentRadioItemPosition = size - 1;
            else
                currentRadioItemPosition -= 1;
        }else if (intent.getAction().equals(MyNotificationBuilder.ACTION_NEXT)){
            if (currentRadioItemPosition == size - 1)
                currentRadioItemPosition = 0;
            else
                currentRadioItemPosition += 1;
        }else if (intent.getAction().equals(MyNotificationBuilder.ACTION_PAUSE_PLAY)){

        }else {
            return;
        }

        RadioPlayer.runPlayer(RecyclerViewAdapter.radioItemList.get(currentRadioItemPosition), context, currentRadioItemPosition, true);
    }
}
