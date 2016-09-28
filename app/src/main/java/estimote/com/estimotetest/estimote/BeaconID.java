package estimote.com.estimotetest.estimote;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.DeviceId;
import com.estimote.sdk.Region;

import java.util.UUID;

public class BeaconID {

    private UUID proximityUUID;
    private int major;
    private int minor;
    private DeviceId deviceId;

    public BeaconID(UUID proximityUUID, int major, int minor, DeviceId deviceId) {
        this.proximityUUID = proximityUUID;
        this.major = major;
        this.minor = minor;
        this.deviceId = deviceId;
    }

    public BeaconID(String UUIDString, int major, int minor, DeviceId deviceId) {
        this(UUID.fromString(UUIDString), major, minor, deviceId);
    }

    public BeaconID(String UUIDString, int major, int minor, String deviceId) {
        this(UUID.fromString(UUIDString), major, minor, DeviceId.fromString(deviceId));
    }

    public BeaconID(Beacon beacon) {
        this(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor(), null);
    }

    public UUID getProximityUUID() {
        return proximityUUID;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public void setProximityUUID(UUID proximityUUID) {
        this.proximityUUID = proximityUUID;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public BeaconID(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public Region toBeaconRegion() {
        return new Region(toString(), getProximityUUID(), getMajor(), getMinor());
    }

    public String toString() {
        return getProximityUUID().toString() + ":" + getMajor() + ":" + getMinor() + ":" + getDeviceId();
    }

    public String toKey() {
        return getProximityUUID().toString() + ":" + getMajor() + ":" + getMinor();
    }

    public static BeaconID fromString(String beaconID) {
        String[] separated = beaconID.split(":");
        return new BeaconID(separated[0], Integer.valueOf(separated[1]), Integer.valueOf(separated[2]), separated[3]);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return super.equals(o);
        }

        BeaconID other = (BeaconID) o;

        return getProximityUUID().equals(other.getProximityUUID())
                && getMajor() == other.getMajor()
                && getMinor() == other.getMinor();
    }
}
