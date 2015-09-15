package com.bawanj.criminalIntent.model;

import java.util.Date;
import java.util.UUID;


public class Crime {

    private UUID mId; // a great idea to find the crime in the list
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    // TODO -- Index add

    public Crime(){ // purpose is to initialize variables
        // Generate unique id
        mId= UUID.randomUUID();
        mDate= new Date();
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
}
