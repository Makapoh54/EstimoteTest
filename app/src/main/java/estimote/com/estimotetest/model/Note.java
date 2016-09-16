package estimote.com.estimotetest.model;


import java.io.Serializable;

public class Note implements Serializable{

    public String title;
    public String content;
    public String creationTime;
    public String assignee;
    public String beaconId;

    public Note() {
    }

    public Note(String title, String content, String creationTime, String beaconId) {
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.beaconId = beaconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }
}
