package kwygonjin.com.homeinternetradio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.*;
import io.realm.OrderedRealmCollection;

//Default adapter for RV
//TODO: add favorite radio functions

class RecyclerViewAdapter extends RealmRecyclerViewAdapter<RadioItem, RecyclerViewAdapter.RadioViewHolder> {
    static List<RadioItem> radioItemList;
    private Context context;
    private static RecyclerViewClickListener itemListener;

    class RadioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.cv)
        CardView cv;
        @BindView(R.id.radio_name)
        TextView radioName;
        @BindView(R.id.radio_logo)
        ImageView radioLogo;

        RadioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RadioItem radioItem = getItem(this.getLayoutPosition());
            if (radioItem != null) {
                itemListener.recyclerViewListClicked(v, radioItem, this.getLayoutPosition());
            }
        }
    }

    RecyclerViewAdapter(Context context, RecyclerViewClickListener itemListener, OrderedRealmCollection<RadioItem> data) {
        super(data, true);
        this.context = context;
        radioItemList = data;
        RecyclerViewAdapter.itemListener = itemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RadioViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);

        return new RadioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RadioViewHolder viewHolder, int position) {
        RadioItem radioItem = getItem(position);
        if (radioItem != null && radioItem.isCurrent())
            viewHolder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        else
            viewHolder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        if (radioItem != null){
            viewHolder.radioName.setText(radioItem.getName());
            viewHolder.radioLogo.setImageResource(context.getResources().getIdentifier(radioItem.getImg(), "drawable", context.getPackageName()));
        }
    }

}