package com.chandra.mamischool.Class;

public class Achievement {
    public String title;
    public String year;
    String level;
    String number;
    String imgAchievement;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgAchievement() {
        return imgAchievement;
    }

    public void setImgAchievement(String imgAchievement) {
        this.imgAchievement = imgAchievement;
    }

    public Achievement(String title, String year, String level, String number) {
        this.title = title;
        this.year = year;
        this.level = level;
        this.number = number;
    }

    public Achievement(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
