package estimote.com.estimotetest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.DeviceId;
import com.estimote.sdk.Region;
import com.estimote.sdk.cloud.CloudCallback;
import com.estimote.sdk.cloud.EstimoteCloud;
import com.estimote.sdk.cloud.model.BeaconInfo;
import com.estimote.sdk.exception.EstimoteServerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import estimote.com.estimotetest.database.FirebaseDb;
import estimote.com.estimotetest.estimote.BeaconID;
import estimote.com.estimotetest.estimote.CustomBeacon;
import estimote.com.estimotetest.utils.Utils;

public class BeaconManagerSingleton {
    private static BeaconManagerSingleton ourInstance = new BeaconManagerSingleton();

    public static BeaconManagerSingleton getInstance() {
        return ourInstance;
    }

    private BeaconManager mBeaconManager;
    private List<CustomBeacon> mTrackedBeaconList = new ArrayList<>();

    private List<Notification> mNotificationsList;
    private Map<String, List<String>> mEnterMessages = new HashMap<>();

    private int mNotificationID = 0;


    private BeaconManagerSingleton() {

        mBeaconManager = new BeaconManager(CustomApplication.getInstance());
        mTrackedBeaconList = Utils.getBeaconListFromSharedPreferences(CustomApplication.getInstance());
        mNotificationsList = Utils.getNotificationsFromSharedPreferences(CustomApplication.getInstance());

        //populateMessagesHashMap();

        mBeaconManager.connect(() -> {
            for (CustomBeacon beaconId : mTrackedBeaconList) {
                Log.d("StartedMonitoring", beaconId.getBeaconId().toString());
                mBeaconManager.startMonitoring(beaconId.getBeaconId().toBeaconRegion());
            }
        });

        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                for (Beacon beacon : list) {
                    for (Notification notification : mNotificationsList) {
                        if (notification.getBeaconID().equals(new BeaconID(beacon))) {
                            showNotification(notification.getEnterMessage());
                        }
                    }
                }
            }

            @Override
            public void onExitedRegion(Region region) {
            }
        });
    }

    public Map<String, List<String>> getEnterMessages() {
        return mEnterMessages;
    }

    public BeaconManager getBeaconManager() {
        return mBeaconManager;
    }

    public void checkTouchedBeaconConsistency(final DeviceId deviceId) {
        for (CustomBeacon beaconID : mTrackedBeaconList) {
            if (beaconID.getBeaconId().getDeviceId().equals(deviceId)) {
                return;
            }
        }
        EstimoteCloud.getInstance().fetchBeaconDetails(deviceId, new CloudCallback<BeaconInfo>() {

            @Override
            public void success(BeaconInfo beaconInfo) {
                Log.d("Good", "cloud good");
                BeaconID beaconID = new BeaconID(beaconInfo.uuid, beaconInfo.major, beaconInfo.minor, deviceId);
                CustomBeacon beacon = new CustomBeacon(beaconInfo.name, beaconInfo.color.toString(), beaconID, null);
                FirebaseDb.createBeacon(beacon);
                if (mTrackedBeaconList != null) {
                    mTrackedBeaconList.add(beacon);
                } else {
                    mTrackedBeaconList = new ArrayList<CustomBeacon>();
                    mTrackedBeaconList.add(beacon);
                }
                mBeaconManager.startMonitoring(beaconID.toBeaconRegion());
                Log.d("StartedMonitoring", beaconID.toString());

            }

            @Override
            public void failure(EstimoteServerException e) {
                Log.d("Failure", "cloud failure");
            }
        });
    }

    private void populateMessagesHashMap() {
        for (Notification notification : mNotificationsList) {
            if (mEnterMessages.containsKey(notification.getBeaconID().toString())) {
                mEnterMessages.get(notification.getBeaconID().toString()).add(notification.getEnterMessage());
            } else {
                List<String> enterMessages = new ArrayList<String>();
                enterMessages.add(notification.getEnterMessage());
                mEnterMessages.put(notification.getBeaconID().toString(), enterMessages);
            }
        }
    }

    private void showNotification(String message) {
        Intent resultIntent = new Intent(CustomApplication.getInstance(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                CustomApplication.getInstance(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(CustomApplication.getInstance())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Beacon Notifications")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) CustomApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationID++, builder.build());
    }

}
