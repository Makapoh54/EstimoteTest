package estimote.com.estimotetest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.DeviceId;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private BeaconManager mBeaconManager;
    private Region mRegion;

    @BindView(R.id.beacon_status_tv)
    TextView mBeaconStatusTv;
    @BindView(R.id.beacon_distance_tv)
    TextView mBeaconDistanceTv;
    @BindView(R.id.beacons_around_tv)
    TextView mBeaconsAroundTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mBeaconManager = new BeaconManager(getApplicationContext());
        mRegion = new Region(
                "monitored region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                null, null);
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                mBeaconManager.startMonitoring(mRegion);
                mBeaconManager.startRanging(mRegion);
                mBeaconManager.startNearableDiscovery();
            }
        });

        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                mBeaconStatusTv.setText("Entered");
                mBeaconStatusTv.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
            }

            @Override
            public void onExitedRegion(Region region) {
                mBeaconStatusTv.setText("Left");
                mBeaconStatusTv.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));
            }

        });

        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {

            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                DecimalFormat f = new DecimalFormat("00.00");
                if (!list.isEmpty()) {
                    mBeaconStatusTv.setText("Entered");
                    mBeaconStatusTv.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
                    mBeaconDistanceTv.setText(Utils.computeProximity(list.get(0)).toString() + ": " + f.format(Utils.computeAccuracy(list.get(0))));
                    mBeaconDistanceTv.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
                } else {
                    mBeaconStatusTv.setText("Left");
                    mBeaconStatusTv.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));
                    mBeaconDistanceTv.setText("-");
                    mBeaconDistanceTv.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));
                }
                mBeaconsAroundTv.setText("Near: " + list.size());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mBeaconManager.startRanging(mRegion);

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                for (int i = 0; i < rawMsgs.length; i++) {
                    NdefMessage msg = (NdefMessage) rawMsgs[i];
                    DeviceId beaconId = findBeaconId(msg);
                    if (beaconId != null) {
                        showNotification("NFC detected", "Chivers");
                    }
                }
            }
        }
    }

    private static DeviceId findBeaconId(NdefMessage msg) {
        NdefRecord[] records = msg.getRecords();
        for (NdefRecord record : records) {
            if (record.getTnf() == NdefRecord.TNF_EXTERNAL_TYPE) {
                String type = new String(record.getType(), Charset.forName("ascii"));
                if ("estimote.com:id".equals(type)) {
                    return DeviceId.fromBytes(record.getPayload());
                }
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        mBeaconManager.stopRanging(mRegion);

        super.onPause();
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}
