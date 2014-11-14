package com.epam.training.taskmanager.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.epam.training.taskmanager.source.HttpDataSource;

import java.io.InputStream;

/**
 * Created by IstiN on 14.11.2014.
 */
public class BitmapProcessor implements Processor<Bitmap, InputStream> {

    @Override
    public Bitmap process(InputStream inputStream) throws Exception {
        try {
            return BitmapFactory.decodeStream(inputStream);
        } finally {
            HttpDataSource.close(inputStream);
        }
    }

}
