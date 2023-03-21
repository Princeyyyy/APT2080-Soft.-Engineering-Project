package com.prince.project.models;

public class Activity {
    String activity_id;
    String user_id;
    String organization_id;
    String name;
    String timeSent;

    public Activity() {
    }

    public Activity(String activity_id, String user_id, String organization_id, String name, String timeSent) {
        this.activity_id = activity_id;
        this.user_id = user_id;
        this.organization_id = organization_id;
        this.name = name;
        this.timeSent = timeSent;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
