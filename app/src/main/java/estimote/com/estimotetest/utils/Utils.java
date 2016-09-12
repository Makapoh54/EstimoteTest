package estimote.com.estimotetest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import estimote.com.estimotetest.Notification;

public class Utils {

    /**
     * Put String in apps shared prefs.
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putStringToPrefs(@NonNull Context context, @NonNull String key, @NonNull String value) {
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }

    /**
     * Get String from apps shared prefs.
     *
     * @param context
     * @param key
     * @return
     */
    public static String getStringFromPrefs(@NonNull Context context, @NonNull String key) {
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void setNotificationFromSharedPreferences(@NonNull Context context, @NonNull ArrayList<Notification> notifcationList) {
        Gson gson = new Gson();
        String jsonNotification = gson.toJson(notifcationList);

        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().putString("NotificationList", jsonNotification).apply();
    }

    public static List<Notification> getNotificationsFromSharedPreferences(@NonNull Context context) {
        Gson gson = new Gson();
        List<Notification> notificationsList;
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.getString("NotificationList", "");
        Type type = new TypeToken<List<Notification>>() {
        }.getType();
        notificationsList = gson.fromJson(prefs.getString("NotificationList", ""), type);
        return notificationsList;
    }
}
