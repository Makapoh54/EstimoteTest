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
import estimote.com.estimotetest.estimote.CustomBeacon;

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

    private static void setNotificationFromSharedPreferences(@NonNull Context context, @NonNull ArrayList<Notification> notifcationList) {
        Gson gson = new Gson();
        String jsonNotification = gson.toJson(notifcationList);
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().putString("NotificationList", jsonNotification).apply();
    }

    public static ArrayList<Notification> getNotificationsFromSharedPreferences(@NonNull Context context) {
        Gson gson = new Gson();
        ArrayList<Notification> notificationsList;
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        Type type = new TypeToken<List<Notification>>() {
        }.getType();
        notificationsList = gson.fromJson(prefs.getString("NotificationList", ""), type);
        return notificationsList == null ? new ArrayList<Notification>() : notificationsList;
    }

    public static void clearNotificationListFromSharedPreferences(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().remove("NotificationList").apply();
    }

    public static void addNotificationToSharedPreferences(@NonNull Context context, @NonNull Notification notification) {
        ArrayList<Notification> notificationsList = getNotificationsFromSharedPreferences(context);
        if (notificationsList != null) {
            clearNotificationListFromSharedPreferences(context);
        } else {
            notificationsList = new ArrayList<>();
        }
        notificationsList.add(notification);
        setNotificationFromSharedPreferences(context, notificationsList);
    }

    private static void setBeaconListFromSharedPreferences(@NonNull Context context, @NonNull ArrayList<CustomBeacon> customBeaconList) {
        Gson gson = new Gson();
        String jsonBeaconList = gson.toJson(customBeaconList);
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().putString("BeaconList", jsonBeaconList).apply();
    }

    public static ArrayList<CustomBeacon> getBeaconListFromSharedPreferences(@NonNull Context context) {
        Gson gson = new Gson();
        ArrayList<CustomBeacon> beaconsList;
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        Type type = new TypeToken<List<CustomBeacon>>() {
        }.getType();
        beaconsList = gson.fromJson(prefs.getString("BeaconList", ""), type);
        return beaconsList == null ? new ArrayList<CustomBeacon>() : beaconsList;
    }

    public static void clearBeaconListFromSharedPreferences(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Utils.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().remove("BeaconList").apply();
    }

    public static void addBeaconToSharedPreferences(@NonNull Context context, @NonNull CustomBeacon customBeacon) {
        ArrayList<CustomBeacon> customBeaconList = getBeaconListFromSharedPreferences(context);
        if (customBeaconList != null) {
            clearBeaconListFromSharedPreferences(context);
        } else {
            customBeaconList = new ArrayList<>();
        }
        customBeaconList.add(customBeacon);
        setBeaconListFromSharedPreferences(context, customBeaconList);
    }
}
