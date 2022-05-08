package com.example.bowling;

public class TimeItem {
    private String time;
    private String alley;

    public String getAlley() {
        return alley;
    }

    public String getTime() {
        return time;
    }
    public TimeItem(String time, String alley) {
        this.time=time;
        this.alley=alley;
    }
}
