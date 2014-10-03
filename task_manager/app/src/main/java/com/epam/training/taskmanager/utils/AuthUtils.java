package com.epam.training.taskmanager.utils;

/**
 * Created by Uladzimir_Klyshevich on 10/3/2014.
 */
public class AuthUtils {

    private static boolean IS_AUTHORIZED = false;

    public static void setLogged(boolean isLogged) {
        //TODO do not write it in real life
        IS_AUTHORIZED = isLogged;
    }

    public static boolean isLogged() {
        return IS_AUTHORIZED;
    }
}
