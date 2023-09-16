package com.bkav.aiotcloud.screen.setting.face;

public class DayItem {
    public DayItem(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private String name;
    private boolean isActive = false;
}
