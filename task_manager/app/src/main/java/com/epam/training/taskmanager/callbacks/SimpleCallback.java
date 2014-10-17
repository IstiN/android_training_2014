package com.epam.training.taskmanager.callbacks;

import android.util.Log;

import com.epam.training.taskmanager.helper.DataManager;

/**
 * Created by IstiN on 17.10.2014.
 */
public abstract class SimpleCallback<Result> implements DataManager.Callback {

    @Override
    public void onDataLoadStart() {
        Log.d("SimpleCallback", "onDataLoadStart");
    }

    @Override
    public void onError(Exception e) {
        Log.e("SimpleCallback", "onError", e);
    }

}
