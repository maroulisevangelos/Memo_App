package com.example.memo.Gallery;

import java.util.Date;

public class StringDatePair {
    private String value;
    private Date date;

    public StringDatePair(String value, Date date) {
        this.value = value;
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
    public String getSDate() {
        return date.toString();
    }
}

