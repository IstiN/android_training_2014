package com.epam.training.taskmanager.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IstiN on 19.12.2014.
 */
public class MyCoolService extends IntentService {

    public static final String TAG = "MyCoolService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyCoolService() {
        super("my service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (ServiceResult.ACTION_EXECUTE_WITH_RESULT_RECEIVER.equals(intent.getAction())) {
                Log.d(TAG, "ACTION_EXECUTE_WITH_RESULT_RECEIVER");
                ResultReceiver resultReceiver = intent.getParcelableExtra(ServiceResult.KEY_RESULT_RECEIVER);
                Log.d(TAG, "send 0 start");
                resultReceiver.send(0, null);
                Log.d(TAG, "send 0 end");
                Bundle resultData = new Bundle();
                resultData.putString("test_key", "test_string");
                Log.d(TAG, "send 1 start");
                resultReceiver.send(1, resultData);
                Log.d(TAG, "send 1 end");
            } else if (ServiceResult.ACTION_EXECUTE_WITH_SERVICE_CONNECTION.equals(intent.getAction())) {
                Log.d(TAG, "ACTION_EXECUTE_WITH_SERVICE_CONNECTION");
                int i = 0;
                for (Runnable runnable : event) {
                    Log.d(TAG, "send "+i+" start");
                    runnable.run();
                    i++;
                }
            }
        }
    }

    private Set<Runnable> event = new HashSet<>();

    public void addRunnable(Runnable runnable) {
        event.add(runnable);
    }

    public void removeRunnable(Runnable runnable) {
        event.remove(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    public class ServiceBinder<S> extends Binder {

        private final WeakReference<S> mService;

        public ServiceBinder(S service){
            mService = new WeakReference<S>(service);
        }

        public S getService() {
            return mService.get();
        }

    }

}
