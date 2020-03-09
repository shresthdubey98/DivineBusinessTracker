package com.example.divinebusinesstracker.models;

public class HistoryEntry {
    private String sno;
    private String id;
    private String date;
    private String points;
    private String status;

    public HistoryEntry(String sno, String id, String date, String points, String status) {
        this.sno = sno;
        this.id = id;
        this.date = date;
        this.points = points;
        this.status = status;
    }

    public String getSno() {
        return sno;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getPoints() {
        return points;
    }

    public String getStatus() {
        return status;
    }
}
