package kwygonjin.com.homeinternetradio;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import io.realm.Realm;

//Class to work with Realm DB
class RealmDBManager {
    static void initData(Realm mRealm, Context context){
        insertRadio(mRealm, context, "Europa Plus", R.drawable.europaplus, false, context.getString(R.string.genre_pop), context.getString(R.string.europa_plus_url), false);
        insertRadio(mRealm, context, "Hit FM", R.drawable.hitfm, false, context.getString(R.string.genre_pop), context.getString(R.string.hit_fm_url), false);
        insertRadio(mRealm, context, "Rus Radio", R.drawable.rusradio, false, context.getString(R.string.genre_dance), context.getString(R.string.rus_radio_url), false);
        insertRadio(mRealm, context, "Kiss FM", R.drawable.kissfm, false, context.getString(R.string.genre_pop), context.getString(R.string.kiss_fm_url), false);
        insertRadio(mRealm, context, "Radio Roks", R.drawable.radioroks, false, context.getString(R.string.genre_rock), context.getString(R.string.radio_rocks_url), false);
        insertRadio(mRealm, context, "Radio Relax", R.drawable.radiorelax, false, context.getString(R.string.genre_pop), context.getString(R.string.radio_relax_url), false);
        insertRadio(mRealm, context, "Lux FM", R.drawable.luxfm, false, context.getString(R.string.genre_pop), context.getString(R.string.lux_fm_url), false);

        insertRadio(mRealm, context, "Add station", R.drawable.add_radiostation, false, "", "", true);
    }

    private static void insertRadio(Realm mRealm, Context context, String name, int img_res, boolean fav, String genre, String url, boolean isAddItemPlaceHolder) {
        mRealm.beginTransaction();

        RadioItem radioItem = mRealm.createObject(RadioItem.class);
        radioItem.setName(name);
        radioItem.setImgresource(context.getResources().getResourceEntryName(img_res));
        radioItem.setFavorite(fav);
        radioItem.setGenre(genre);
        radioItem.setURL(url);
        radioItem.setAddItem(isAddItemPlaceHolder);
        mRealm.commitTransaction();
    }
}
