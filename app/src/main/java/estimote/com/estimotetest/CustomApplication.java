package estimote.com.estimotetest;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;

import estimote.com.estimotetest.estimote.BeaconNotificationsManager;

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
        EstimoteSDK.initialize(getApplicationContext(), "app_1za3wwwu24", "669fdaca626fae4c9311a2ffeb555609");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
//        EstimoteSDK.enableDebugLogging(true);
    }

}