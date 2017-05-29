package kwygonjin.com.homeinternetradio;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

//Fragment with RecyclerView, that display RadiItems
public class RecyclerViewFragment extends Fragment implements RecyclerViewClickListener {
    private static final int SPAN_COUNT = 2;
    private Realm mRealm;
    @BindView(R.id.et_radiostation_name)
    EditText etRadioName;
    @BindView(R.id.et_radiostation_genre)
    EditText etRadioGenre;
    @BindView(R.id.et_radiostation_url)
    EditText etRadioURL;
    @BindView(R.id.et_radiostation_img)
    EditText etRadioImg;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void recyclerViewListClicked(View v, RadioItem radioItem, int position) {


        if (radioItem.isAddItem()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.new_radioitem_dialog, null);
            ButterKnife.bind(this, view);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Add action buttons
                    .setPositiveButton(R.string.add_radiostation, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Realm mRealm = Realm.getDefaultInstance();
                            mRealm.beginTransaction();
                            RadioItem radioItem = mRealm.createObject(RadioItem.class);
                            radioItem.setName(etRadioName.getText().toString());
                            radioItem.setImgresource(etRadioImg.getText().toString());
                            radioItem.setFavorite(false);
                            radioItem.setGenre(etRadioGenre.getText().toString());
                            radioItem.setURL(etRadioURL.getText().toString());
                            radioItem.setAddItem(false);
                            mRealm.commitTransaction();
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.create();

            builder.show();
        }
        else
            RadioPlayer.runPlayer(radioItem, getContext(), position, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //work with realm
        mRealm = Realm.getDefaultInstance();
        RealmDBManager.initData(mRealm, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new RecyclerViewAdapter(getContext(), this, mRealm.where(RadioItem.class).findAll());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     }

    //Set layout manager for different LayoutManagerType
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.setAdapter(null);
        mRealm.close();
    }
}
