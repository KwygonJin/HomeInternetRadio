package kwygonjin.com.homeinternetradio;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by KwygonJin on 03.04.2017.
 */

//This is interface for RecyclerView onClick action
public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, RadioItem item, int position);
}
