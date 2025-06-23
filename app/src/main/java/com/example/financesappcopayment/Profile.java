package com.example.financesappcopayment;

public class Profile {
    private int iconResId;
    private String title;
    private int iconId;

    // Полный конструктор
    public Profile(int iconResId, String title, int iconId) {
        this.iconResId = iconResId;
        this.title = title;
        this.iconId = iconId;
    }

    // Геттеры
    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconId() {
        return iconId;
    }

    // Сеттеры
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
