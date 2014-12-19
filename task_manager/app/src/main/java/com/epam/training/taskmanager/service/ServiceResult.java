package com.epam.training.taskmanager.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by IstiN on 19.12.2014.
 */
public class ServiceResult {

    public static final String KEY_RESULT_RECEIVER = "result_receiver";
    public static final String ACTION_EXECUTE_WITH_RESULT_RECEIVER = "action_executeWithResultReceiver";
    public static final String ACTION_EXECUTE_WITH_SERVICE_CONNECTION = "action_executeWithServiceConnection";

    //BroadcastReceiver / local BroadcastReceiver
    //Singleton/bus messaging

    public static void executeWithResultReceiver(Activity activity) {
        Log.d(ACTION_EXECUTE_WITH_RESULT_RECEIVER, "executeWithResultReceiver");
        Intent serviceIntent = new Intent(activity, MyCoolService.class);
        serviceIntent.setAction(ACTION_EXECUTE_WITH_RESULT_RECEIVER);
        Handler handler = new Handler();
        ResultReceiver resultReceiver = new ResultReceiver(handler) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                Log.d(ACTION_EXECUTE_WITH_RESULT_RECEIVER, resultCode + " " + resultData);
            }
        };
        serviceIntent.putExtra(KEY_RESULT_RECEIVER, resultReceiver);
        activity.startService(serviceIntent);
    }

    public static void executeWithServiceConnection(final Activity activity) {
        Log.d(ACTION_EXECUTE_WITH_SERVICE_CONNECTION, "executeWithServiceConnection");
        final Intent serviceIntent = new Intent(activity, MyCoolService.class);
        serviceIntent.setAction(ACTION_EXECUTE_WITH_SERVICE_CONNECTION);
        activity.bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(ACTION_EXECUTE_WITH_SERVICE_CONNECTION, "onServiceConnected");
                final MyCoolService myCoolService = ((MyCoolService.ServiceBinder<MyCoolService>) service).getService();
                myCoolService.addRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(ACTION_EXECUTE_WITH_SERVICE_CONNECTION, "event ");
                        myCoolService.removeRunnable(this);
                    }
                });
                activity.startService(serviceIntent);
                //TODO make action
                activity.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(ACTION_EXECUTE_WITH_SERVICE_CONNECTION, "onServiceDisconnected");
            }
        }, Context.BIND_AUTO_CREATE);
    }

}
