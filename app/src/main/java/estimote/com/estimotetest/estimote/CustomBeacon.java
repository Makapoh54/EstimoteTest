package estimote.com.estimotetest.estimote;

public class CustomBeacon {

    public String name;
    public String color;
    public String beaconId;

    public CustomBeacon(String name, String color, String beaconId) {
        this.name = name;
        this.color = color;
        this.beaconId = beaconId;
    }

    public CustomBeacon() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }
}
