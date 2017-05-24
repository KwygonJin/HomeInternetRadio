package kwygonjin.com.homeinternetradio;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by KwygonJin on 29.03.2017.
 */

//RadioItem class for desc RadioStations(RS)
//Contains:
// name - RS name;
// id - id in DB;
// genre - RS genre(rock, pop etc...);
// isFavorite - for display only favorites RS;
// img = name of res img from drawable;
// URL - url to play in MediaPlayer


public class RadioItem extends RealmObject {

//    public static final String TABLE_NAME = "RADIO_TB";
//    public static final String FIELD_ID = "_id";
//    public static final String FIELD_NAME = "NAME";
//    public static final String FIELD_GENRE = "GENRE";
//    public static final String FIELD_URL = "URL";
//    public static final String FIELD_IMG = "IMG";
//    public static final String FIELD_FAV = "FAVORITE";

    @Required
    private String name;
    //@PrimaryKey
    private String URL;
    private int id;
    private String img;
    private boolean favorite;
    private String genre;
    private boolean isCurrent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImgresource(String img) {
        this.img = img;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
