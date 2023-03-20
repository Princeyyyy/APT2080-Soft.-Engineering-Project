package com.prince.project.models;

public class Activity {
    String id;
    String organization_id;
    String name;
    String timeSent;

    public Activity() {
    }

    public Activity(String id, String organization_id, String name, String timeSent) {
        this.id = id;
        this.organization_id = organization_id;
        this.name = name;
        this.timeSent = timeSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
