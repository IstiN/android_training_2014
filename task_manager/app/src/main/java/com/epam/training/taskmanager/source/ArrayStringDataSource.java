package com.epam.training.taskmanager.source;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IstiN on 15.10.2014.
 */
public class ArrayStringDataSource implements DataSource<ArrayList<String>, Void> {

    private static final ArrayList<String> DATA;

    static {
        DATA = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            DATA.add("test value " + i);
        }
    }

    public static List<String> getData() throws Exception {
        Thread.currentThread().sleep((long)(DateUtils.SECOND_IN_MILLIS * 0.5f));
        return (List<String>) DATA.clone();
    }

    @Override
    public ArrayList<String> getResult(Void pVoid) throws Exception {
        return (ArrayList<String>) getData();
    }
}
