package estimote.com.estimotetest;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.estimote.sdk.DeviceId;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.nio.charset.Charset;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class LogInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int signOutCode = 0xCAFE;
    private static final int signInCode = 0xC001;

    @OnClick(R.id.sign_in_button)
    public void signInOnClick() {
        signInIntent();
    }

    private GoogleApiClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mGoogleClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this::onConnectionFailed)
                .addApi(Auth.GOOGLE_SIGN_IN_API, buildSignInOptions())
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        signInIntent();
    }

    private void signInIntent(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleClient);
        startActivityForResult(signInIntent, signInCode);
    }

    private GoogleSignInOptions buildSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == signInCode) {
            signIn(data);
        } else if (requestCode == signOutCode) {
            signOut();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                        BeaconManagerSingleton.getInstance().addBeaconToList(beaconId);
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

    private void signIn(Intent data) {
        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (signInResult.isSuccess()) {
            signInTo(signInResult.getSignInAccount());
        } else {
            toast("Google Sign In failed");
        }
    }

    private void signInTo(GoogleSignInAccount account) {
        FirebaseAuth.getInstance()
                .signInWithCredential(fromToken(account.getIdToken()))
                .addOnCompleteListener(this, this::onSignInComplete);
    }

    private void onSignInComplete(Task<AuthResult> signinResult) {
        if (signinResult.isSuccessful()) {
            GoogleUser.saveIfNew();
            startActivityForResult(new Intent(this, MainActivity.class), signOutCode);
        } else {
            toast("Authentication failed.");
        }
    }

    private AuthCredential fromToken(String token) {
        return GoogleAuthProvider.getCredential(token, null);
    }

    private void signOut() {
      /*  GoogleUser.signOut();
        mGoogleClient.registerConnectionCallbacks((GoogleSignInAccount.ConnectionCallbacks) bundle -> {
            Auth.GoogleSignInApi.signOut(mGoogleClient);
            toast("Signed Out.");
        });
        mGoogleClient.connect();*/
    }

    private void toast(String message) {
        makeText(this, message, LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("Google Play Services error.");
    }
}
