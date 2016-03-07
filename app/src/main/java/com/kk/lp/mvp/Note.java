package com.kk.lp.mvp;

import java.util.Date;

/**
 * Created by lipeng on 2016 3-10.
 */
public class Note {
    private String text;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
