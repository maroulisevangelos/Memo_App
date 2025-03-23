package com.example.memo.Gallery;

public class StringCounterPair {
    private String value;
    private int counter;

    public StringCounterPair(String value, int counter) {
        this.value = value;
        this.counter = counter;
    }

    public String getValue() {
        return value;
    }

    public int getCounter() {
        return counter;
    }

    public String getSCounter() {
        return String.valueOf(counter);
    }

    public void addVisit(){
        counter += 1;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

