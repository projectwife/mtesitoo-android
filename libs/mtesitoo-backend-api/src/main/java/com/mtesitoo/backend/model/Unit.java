package com.mtesitoo.backend.model;

/**
 * Created by Nan on 1/9/2016.
 */
public class Unit {
    private Integer mId;
    private String mName;
    private String mUnit;

    private String mAbbr;
    private Integer mSortOrder;

    public Unit(Integer id, String name, String unit) {
        mId = id;
        mName = name;
        mUnit = unit;
    }

    public Unit(Integer id, String name, String abbr, Integer sortOrder) {
        mId = id;
        mName = name;
        mAbbr = abbr;
        mSortOrder = sortOrder;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUnit() {
        return mUnit;
    }

    public String getAbbr() {
        return mAbbr;
    }

    public int getSortOrder() {
        return mSortOrder;
    }
}