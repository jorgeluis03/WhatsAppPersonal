package com.example.whatsappclone.model;

import java.util.List;

public class UserStatus {
    private String name, profileImagen;
    private long lastUpdated;
    private List<Status> statusList;

    public UserStatus() {
    }

    public UserStatus(String name, String profileImagen, long lastUpdated, List<Status> statusList) {
        this.name = name;
        this.profileImagen = profileImagen;
        this.lastUpdated = lastUpdated;
        this.statusList = statusList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImagen() {
        return profileImagen;
    }

    public void setProfileImagen(String profileImagen) {
        this.profileImagen = profileImagen;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }
}
