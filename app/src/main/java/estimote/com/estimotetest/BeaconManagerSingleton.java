package estimote.com.estimotetest;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import estimote.com.estimotetest.estimote.BeaconID;

public class BeaconManagerSingleton {
    private static BeaconManagerSingleton ourInstance = new BeaconManagerSingleton();

    public static BeaconManagerSingleton getInstance() {
        return ourInstance;
    }

    private BeaconManager mBeaconManager;
    private List<BeaconID> mTrackedBeaconList;

    private List<Notification> mNotificationsList;

    private Map<String, List<String>> mEnterMessages = new HashMap<>();

    private BeaconManagerSingleton() {

        mTrackedBeaconList = Utils.getNotificationsFromSharedPreferences();
        populateMessagesHashMap();

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for (BeaconID id : mTrackedBeaconList) {
                    mBeaconManager.startMonitoring(id.toBeaconRegion());
                }
            }
        });

        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                populateMessagesHashMap();
                for(String beaconId : mEnterMessages.keySet()){
                    for(String messageText : mEnterMessages.get(beaconId)){

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

    private void populateMessagesHashMap(){
        for(Notification notification : mNotificationsList){
            if(mEnterMessages.containsKey(notification.getBeaconID().toString())) {
                mEnterMessages.get(notification.getBeaconID().toString()).add(notification.getEnterMessage());
            } else {
                List<String> enterMessages = new ArrayList<String>();
                enterMessages.add(notification.getEnterMessage());
                mEnterMessages.put(notification.getBeaconID().toString(), enterMessages);
            }
        }
    }

}
