package com.epam.training.taskmanager.source;

import android.content.Context;
import android.util.Log;

import com.epam.training.taskmanager.CoreApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

/**
 * Created by IstiN on 03.12.2014.
 */
public class CachedHttpDataSource extends HttpDataSource {

    public static final String KEY = "CachedHttpDataSource";
    public static final String TAG = "cache_http_data_source";

    private Context mContext;

    public CachedHttpDataSource(Context context) {
        mContext = context;
    }

    public static CachedHttpDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        Log.d(TAG, "getResult");
        File cacheDir = mContext.getCacheDir();
        File file = new File(cacheDir, "__cache");
        file.mkdirs();
        String path = file.getPath() + File.separator + generateFileName(p);
        File cacheFile = new File(path);
        if (cacheFile.exists()) {
            Log.d(TAG, "from file");
            return new FileInputStream(cacheFile);
        }
        InputStream inputStream = super.getResult(p);
        try {
            Log.d(TAG, "copy stream");
            copy(inputStream, cacheFile);
        } catch (Exception e) {
            cacheFile.delete();
            throw e;
        }
        Log.d(TAG, "copy stream success get from file");
        return new FileInputStream(cacheFile);
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static long copy(InputStream input, File file) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            return copy(input, fileOutputStream);
        } finally {
            close(fileOutputStream);
        }
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private String generateFileName(String p) {
        return md5(p);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
