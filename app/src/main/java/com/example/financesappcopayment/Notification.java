package com.example.financesappcopayment;

public class Notification {
    private int iconResId;
    private String title;
    private String amount;
    private String type;

    // Полный конструктор
    public Notification(int iconResId, String title, String amount, String type) {
        this.iconResId = iconResId;
        this.title = title;
        this.amount = amount;
        this.type = type;
    }

    // Геттеры
    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    // Сеттеры
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }
}
