package com.epam.training.taskmanager.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.epam.training.taskmanager.Api;
import com.epam.training.taskmanager.CoreApplication;
import com.epam.training.taskmanager.auth.VkOAuthHelper;
import com.epam.training.taskmanager.utils.AuthUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by IstiN on 17.10.2014.
 */
public class VkDataSource extends HttpDataSource {

    public static final String KEY = "VkDataSource";

    public static VkDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        String signUrl = VkOAuthHelper.sign(p);
        String versionValue = Uri.parse(signUrl).getQueryParameter(Api.VERSION_PARAM);
        if (TextUtils.isEmpty(versionValue)) {
            signUrl = signUrl + "&" + Api.VERSION_PARAM + "=" + Api.VERSION_VALUE;
        }
        return super.getResult(signUrl);
    }

}
