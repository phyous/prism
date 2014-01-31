package com.phyous.prism.provider;

import java.util.ArrayList;

public class Entry {
    private long mId;
    private long mDate;
    private ArrayList<String> mPositives;
    private ArrayList<String> mNegatives;
    private ArrayList<String> mNext;

    public Entry(long date,
            ArrayList<String> negatives,
            ArrayList<String> positives,
            ArrayList<String> next) {
        mDate = date;
        mPositives = positives;
        mNegatives = negatives;
        mNext = next;
    }

    public Entry(long date,
            ArrayList<String> negatives,
            ArrayList<String> positives,
            ArrayList<String> next,
            long id) {
        this(date, negatives, positives, next);
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getDate() {
        return mDate;
    }

    public ArrayList<String> getPositives() {
        return mPositives;
    }

    public ArrayList<String> getNegatives() {
        return mNegatives;
    }

    public ArrayList<String> getNext() {
        return mNext;
    }
}
