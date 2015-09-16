package com.bawanj.criminalIntent.model;

import java.util.Date;
import java.util.UUID;


public class Crime {

    private UUID mId; // a great idea to find the crime in the list
    private String mTitle;
    private Date mDate;
    private boolean mSolved;


    private String mSuspect;

    // TODO -- Index add

    public Crime(){ // new one
        this(UUID.randomUUID());
    }

    public Crime(UUID id){ // old one
        mId   =id;
        mDate =new Date();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }
    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

}
