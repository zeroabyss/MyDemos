package com.example.aiy.crime;

import java.util.Date;
import java.util.UUID;

/**
 *任务描述： Bean
 *创建时间： 2017/7/30 17:52
 */

public class Crime {//单个例子

    private UUID id;
    private String title;
    private Date date;
    private boolean solved;
    private String suspect;

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Crime(){
        this(UUID.randomUUID());
    }

    public Crime(UUID id){
        this.id=id;
        date=new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
