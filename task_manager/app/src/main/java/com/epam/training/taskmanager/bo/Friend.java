package com.epam.training.taskmanager.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by IstiN on 21.10.2014.
 */
public class Friend extends JSONObjectWrapper {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String NICKNAME = "nickname";
    private static final String PHOTO = "photo_200_orig";
    private static final String ONLINE = "online";
    private static final String ID = "id";

    //INTERNAL
    private static final String NAME = "NAME";

    public static final Parcelable.Creator<Friend> CREATOR
            = new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public Friend(String jsonObject) {
        super(jsonObject);
    }

    public Friend(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Friend(Parcel in) {
        super(in);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public String getNickname() {
        return getString(NICKNAME);
    }

    public String getPhoto() {
        return getString(PHOTO);
    }

    public void initName() {
        set(NAME, getFirstName() + " " + getLastName());
    }

    public String getName() {
        return getString(NAME);
    }

    public boolean isOnline() {
        Boolean isOnline = getBoolean(ONLINE);
        if (isOnline == null) {
            return false;
        }
        return isOnline;
    }

    public Long getId() {
        return getLong(ID);
    }

}
