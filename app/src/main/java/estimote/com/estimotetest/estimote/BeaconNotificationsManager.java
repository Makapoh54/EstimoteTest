package estimote.com.estimotetest.estimote;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.Map;

import estimote.com.estimotetest.BeaconManagerSingleton;
import estimote.com.estimotetest.MainActivity;

public class BeaconNotificationsManager {

    private static final String TAG = "BeaconNotifications";

    private BeaconManager mBeaconManager;

    private Context mContext;

    private Map<String, List<String>> mEnterMessages;

    private int mNotificationID = 0;

    public BeaconNotificationsManager(Context context) {
        this.mContext = context;
        mBeaconManager = BeaconManagerSingleton.getInstance().getBeaconManager();
        mEnterMessages = BeaconManagerSingleton.getInstance().getEnterMessages();
        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                for(String beaconId : mEnterMessages.keySet()){
                    for(String messageText : mEnterMessages.get(beaconId)){
                        showNotification(messageText);
                    }
                }
            }

            @Override
            public void onExitedRegion(Region region) {

            }

        });
    }

    private void showNotification(String message) {
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Beacon Notifications")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationID++, builder.build());
    }
}

