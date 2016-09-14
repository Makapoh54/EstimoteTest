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
        return getProximityUUID().toString() + ":" + getMajor() + ":" + getMinor();
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
