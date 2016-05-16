package com.kk.lp.welcome;

import java.io.Serializable;

/**
 * Created by lipeng on 2016 5-16.
 */
public class Welcome implements Serializable{

    private String title;
    private String description;
    private int img;
    private int backgroundColor;

    public Welcome(String title, String description, int img, int backgroundColor) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.backgroundColor = backgroundColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
