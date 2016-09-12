package estimote.com.estimotetest;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;

import java.util.ArrayList;

import estimote.com.estimotetest.estimote.BeaconNotificationsManager;
import estimote.com.estimotetest.utils.Utils;

public class CustomApplication extends Application {

    private static CustomApplication sCustomApplication;

    private BeaconNotificationsManager mBeaconNotificationsManager;

    public static CustomApplication getInstance() {
        return sCustomApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sCustomApplication = this;
        // TODO: put your App ID and App Token here
        // You can get them by adding your app on https://cloud.estimote.com/#/apps
        EstimoteSDK.initialize(getApplicationContext(), "<#App ID#>", "<#App Token#>");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
//        EstimoteSDK.enableDebugLogging(true);
    }

    public void enableBeaconNotifications() {
        if (mBeaconNotificationsManager != null) {
            ArrayList<Notification> notificationList = Utils.getNotificationsFromSharedPreferences(this);
            mBeaconNotificationsManager = new BeaconNotificationsManager(this);
            for (Notification id : notificationList) {
                mBeaconNotificationsManager.addNotification(id);
            }
            mBeaconNotificationsManager.startMonitoring();

        }
    }

    public void addNotification(Notification notification) {
        if (mBeaconNotificationsManager != null)
            mBeaconNotificationsManager.addNotification(notification);
    }
}