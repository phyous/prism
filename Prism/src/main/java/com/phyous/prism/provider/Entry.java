package com.phyous.prism.provider;

public class Entry {
    private long mId;
    private long mDate;
    private String[] mPositives;
    private String[] mNegatives;
    private String[] mNext;

    public Entry(long date, String[] negatives, String[] positives, String[] next) {
        mDate = date;
        mPositives = positives;
        mNegatives = negatives;
        mNext = next;
    }

    public Entry(long date, String[] negatives, String[] positives, String[] next, long id) {
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

    public String[] getPositives() {
        return mPositives;
    }

    public String[] getNegatives() {
        return mNegatives;
    }

    public String[] getNext() {
        return mNext;
    }
}
