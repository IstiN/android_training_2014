package com.epam.training.taskmanager.processing;

import com.epam.training.taskmanager.bo.JSONObjectWrapper;
import com.epam.training.taskmanager.bo.Note;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IstiN on 21.10.2014.
 */
public abstract class WrapperArrayProcessor<T extends JSONObjectWrapper> implements Processor<List<T>,InputStream> {

    @Override
    public List<T> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONArray(string);
        List<T> noteArray = new ArrayList<T>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            noteArray.add(createObject(jsonObject));
        }
        return noteArray;
    }

    protected abstract T createObject(JSONObject jsonObject);
}
