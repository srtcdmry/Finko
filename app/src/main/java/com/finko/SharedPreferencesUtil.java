package com.finko;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;



public class SharedPreferencesUtil {
    public static final String PREF_USER_MAIL = "user_mail";
    public static final String PREF_IS_NEW_USER = "is_new_user";

    private SharedPreferencesUtil() {

    }

    public static SharedPreferences getPrefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String userMail(Context context) {
        return getPrefs(context).getString(PREF_USER_MAIL, null);
    }

    public static void setPrefUserMail(Context context, String userMail) {
        getPrefs(context).edit().putString(PREF_USER_MAIL, userMail).apply();
    }

    public static boolean isNewUser(Context context) {
        return getPrefs(context).getBoolean(PREF_IS_NEW_USER, true);
    }

    public static void setPrefIsNewUser(Context context, boolean isNewUser) {
        getPrefs(context).edit().putBoolean(PREF_IS_NEW_USER, isNewUser).apply();
    }

    public static void clear(Context context) {
        getPrefs(context).edit().clear().apply();
    }
}
