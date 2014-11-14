package com.epam.training.taskmanager.utils;

import com.epam.training.taskmanager.auth.VkOAuthHelper;

/**
 * Created by Uladzimir_Klyshevich on 10/3/2014.
 */
public class AuthUtils {

    public static boolean isLogged() {
        return VkOAuthHelper.isLogged();
    }

}
