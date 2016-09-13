package estimote.com.estimotetest;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.MacAddress;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import estimote.com.estimotetest.estimote.BeaconID;
import estimote.com.estimotetest.utils.Utils;

public class BeaconManagerSingleton {
    private static BeaconManagerSingleton ourInstance = new BeaconManagerSingleton();

    public static BeaconManagerSingleton getInstance() {
        return ourInstance;
    }

    private BeaconManager mBeaconManager;
    private List<BeaconID> mTrackedBeaconList = new ArrayList<>();
    private List<MacAddress> mTouchedBeaconMacs = new ArrayList<>();


    private List<Notification> mNotificationsList;

    private Map<String, List<String>> mEnterMessages = new HashMap<>();

    private BeaconManagerSingleton() {

        mBeaconManager = new BeaconManager(CustomApplication.getInstance());
        //mTrackedBeaconList = Utils.getBeaconListFromSharedPreferences(CustomApplication.getInstance());
        mNotificationsList = Utils.getNotificationsFromSharedPreferences(CustomApplication.getInstance());

        populateMessagesHashMap();

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                mBeaconManager.startMonitoring(new Region("monitored region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null));
            }
        });

        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    for (Beacon beacon : list) {
                        if (mTouchedBeaconMacs.contains(beacon.getMacAddress())) {
                            mTrackedBeaconList.add(new BeaconID(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor()));
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

    public void addBeaconMacToList(MacAddress mac) {
        mTouchedBeaconMacs.add(mac);
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

}
