package com.epam.training.taskmanager.helper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IstiN on 05.12.2014.
 */
public class ConcurrencySampleHelper {

    public static void success() {
        final List<String> array = new CopyOnWriteArrayList<String>();
        generateTestValues(array);
        concurrencyThreadTest(array);
    }

    public static void fail() {
        final List<String> array = new ArrayList<String>();
        generateTestValues(array);
        concurrencyThreadTest(array);
    }

    private static void generateTestValues(List<String> array) {
        for (int i = 0; i < 10; i++) {
            generateNewValue(array);
        }
    }

    private static void concurrencyThreadTest(final List<String> array) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String value : array) {
                    try {
                        Log.d("concurrent", "foreach array size " + array.size());
                        Thread.currentThread().sleep(700l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(500l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                generateNewValue(array);
                Log.d("concurrent", "new value array size " + array.size());
            }
        }).start();
    }

    private static void generateNewValue(List<String> array) {
        array.add(UUID.randomUUID().toString());
    }

}
