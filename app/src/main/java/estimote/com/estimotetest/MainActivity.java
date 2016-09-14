package estimote.com.estimotetest;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.estimote.sdk.DeviceId;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
        BeaconManagerSingleton.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                for (int i = 0; i < rawMsgs.length; i++) {
                    NdefMessage msg = (NdefMessage) rawMsgs[i];
                    DeviceId beaconId = findBeaconId(msg);
                    if (beaconId != null) {
                        BeaconManagerSingleton.getInstance().checkTouchedBeaconConsistency(beaconId);
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
}
