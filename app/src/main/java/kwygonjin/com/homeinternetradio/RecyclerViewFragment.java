package kwygonjin.com.homeinternetradio;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import static android.app.Activity.RESULT_OK;

//Fragment with RecyclerView, that display RadiItems
public class RecyclerViewFragment extends Fragment implements RecyclerViewClickListener {
    private static final int SPAN_COUNT = 2;
    private Realm mRealm;
    final int REQUEST_CODE_NEWRADIOITEM = 1;

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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RadioItemActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEWRADIOITEM);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEWRADIOITEM:
                    mLayoutManager.scrollToPosition(RecyclerViewAdapter.radioItemList.size() - 1);
            }
        }
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
