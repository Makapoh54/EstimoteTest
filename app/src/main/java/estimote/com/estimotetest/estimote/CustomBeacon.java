package estimote.com.estimotetest.estimote;

public class CustomBeacon {

    private String name;
    private String color;
    private BeaconID beaconId;
    private String location;

    public CustomBeacon(String name, String color, BeaconID beaconId, String location) {
        this.name = name;
        this.color = color;
        this.beaconId = beaconId;
        this.location = location;
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

    public BeaconID getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(BeaconID beaconId) {
        this.beaconId = beaconId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
