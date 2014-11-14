package com.epam.training.taskmanager.processing;

import android.content.ContentValues;

import com.epam.training.taskmanager.bo.Friend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IstiN on 21.10.2014.
 */
public class FriendArrayProcessor implements Processor<List<Friend>,InputStream>{

    @Override
    public List<Friend> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONObject("response").getJSONArray("items");
        //TODO wrapper for array
        List<Friend> noteArray = new ArrayList<Friend>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            Friend friend = new Friend(jsonObject);
            friend.initName();
            noteArray.add(friend);
        }
        return noteArray;
    }

}
