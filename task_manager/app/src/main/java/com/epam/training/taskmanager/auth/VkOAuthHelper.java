package com.epam.training.taskmanager.auth;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.epam.training.taskmanager.VkLoginActivity;

/**
 * Created by IstiN on 29.10.2014.
 */
public class VkOAuthHelper {

    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4611084&scope=offline,wall,photos,status&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";
    private static final String TAG = VkOAuthHelper.class.getSimpleName();


    public static boolean proceedRedirectURL(Activity activity, String url) {
        //https://oauth.vk.com/blank.html#
        //fragment: access_token=token&expires_in=0&user_id=308327
        //https://oauth.vk.com/blank.html#error=
        if (url.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(accessToken)) {
                Log.d(TAG, "token " + accessToken);
                //TODO save token to the secure store
                //TODO create account in account manager
                return true;
            } else {
                //TODO check access denied/finish
                //#error=access_denied&error_reason=user_denied&error_description=User denied your request
            }

        }
        return false;
    }
}
