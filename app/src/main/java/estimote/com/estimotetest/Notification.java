package estimote.com.estimotetest;

import estimote.com.estimotetest.estimote.BeaconID;

public class Notification {
    private BeaconID mBeaconID;
    private String mEnterMessage;
    private String mExitMessage;

    public Notification(BeaconID beaconID, String enterMessage, String exitMessage) {
        mBeaconID = beaconID;
        mEnterMessage = enterMessage;
        mExitMessage = exitMessage;
    }

    public BeaconID getBeaconID() {
        return mBeaconID;
    }

    public void setBeaconID(BeaconID beaconID) {
        mBeaconID = beaconID;
    }

    public String getEnterMessage() {
        return mEnterMessage;
    }

    public void setEnterMessage(String enterMessage) {
        mEnterMessage = enterMessage;
    }

    public String getExitMessage() {
        return mExitMessage;
    }

    public void setExitMessage(String exitMessage) {
        mExitMessage = exitMessage;
    }
}
