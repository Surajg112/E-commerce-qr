package com.csi.model;

public class QRCodeData {

    String text;
    int width;
    int height;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public QRCodeData(String text, int width, int height) {
        this.text = text;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "QRCodeData{" +
                "text='" + text + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
