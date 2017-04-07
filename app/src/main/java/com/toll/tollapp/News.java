package com.toll.tollapp;

import android.util.Log;

/**
 * Created by Rohit33 on 30-03-2017.
 */

public class News {
    private String title,content,date;

    private static final String TAG=News.class.getSimpleName();

    public News(){}

    public News(String title,String content,String date){
        this.title=title;
        this.content=content;
        this.date=date;
        Log.d(TAG,title+content+date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String year) {
        this.date = year;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String genre) {
        this.content = genre;
    }

}
