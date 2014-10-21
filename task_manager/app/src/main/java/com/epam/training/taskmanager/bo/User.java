package com.epam.training.taskmanager.bo;

import org.json.JSONObject;

/**
 * Created by IstiN on 21.10.2014.
 */
public class User extends JSONObjectWrapper {

    private static final String NAME = "name";
    private static final String ID = "id";

    public User(String jsonObject) {
        super(jsonObject);
    }

    public User(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getName() {
        return getString(NAME);
    }

    public Long getId() {
        return getLong(ID);
    }

}
