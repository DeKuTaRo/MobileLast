package com.example.firebase.Models;

public class NoteItem {

    private String name, place, date, time;

    public NoteItem() {

    }

    public NoteItem(String name, String place, String date, String time) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
