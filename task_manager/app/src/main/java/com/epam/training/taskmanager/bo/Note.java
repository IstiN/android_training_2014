package com.epam.training.taskmanager.bo;

import org.json.JSONObject;

/**
 * Created by IstiN on 21.10.2014.
 */
public class Note {

    private static final String TITLE = "t";
    private static final String CONTENT = "c";
    private static final String ID = "i";

    private JSONObject mJO;

    public Note(JSONObject jsonObject) {
        mJO = jsonObject;
    }

    public String getTitle() {
        return mJO.optString(TITLE);
    }

    public String getContent() {
        return mJO.optString(CONTENT);
    }

    public Long getId() {
        return mJO.optLong(ID);
    }

}
