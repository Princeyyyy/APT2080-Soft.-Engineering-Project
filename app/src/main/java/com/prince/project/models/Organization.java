package com.prince.project.models;

public class Organization {
    String id;
    String org_name;
    String org_email;

    public Organization() {
    }

    public Organization(String id, String org_name, String org_email) {
        this.id = id;
        this.org_name = org_name;
        this.org_email = org_email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_email() {
        return org_email;
    }

    public void setOrg_email(String org_email) {
        this.org_email = org_email;
    }
}
