package com.shrewd.smart_gesture;

public class GestureButton {

    private int id, iconResId;
    private String title, description;

    public GestureButton(int id, int iconResId, String title, String description) {
        this.id = id;
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
