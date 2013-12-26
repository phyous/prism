package com.phyous.prism.provider;

public class Entry {
    private long mId;
    private long mDate;
    private String[] mPositives;
    private String[] mNegatives;

    public Entry(long date, String[] negatives, String[] positives) {
        mDate = date;
        mPositives = positives;
        mNegatives = negatives;
    }

    public Entry(long date, String[] negatives, String[] positives, long id) {
        this(date, negatives, positives);
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
}
