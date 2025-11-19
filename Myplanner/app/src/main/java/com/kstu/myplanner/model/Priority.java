package com.kstu.myplanner.model;

public enum Priority {
    HIGH("Высокий", "#F44336"),
    MEDIUM("Средний", "#FF9800"),
    LOW("Низкий", "#4CAF50");

    private final String displayName;
    private final String colorHex;

    Priority(String displayName, String colorHex) {
        this.displayName = displayName;
        this.colorHex = colorHex;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorHex() {
        return colorHex;
    }
}