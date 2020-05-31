package com.example.mymoviememoir.entity;

public class MonthMovie {

    private String watchedMonth;
    private int count;

    public MonthMovie(String watchedMonth, int count) {
        this.watchedMonth = watchedMonth;
        this.count = count;
    }

    public String getWatchedMonth() {
        return watchedMonth;
    }

    public void setWatchedMonth(String watchedMonth) {
        this.watchedMonth = watchedMonth;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
