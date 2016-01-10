package com.mtesitoo.backend.model;

/**
 * Created by Nan on 1/9/2016.
 */
public class Unit {
    private Integer mId;
    private String mName;
    private String mUnit;

    public Unit(Integer id, String name, String unit) {
        mId = id;
        mName = name;
        mUnit = unit;
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
}