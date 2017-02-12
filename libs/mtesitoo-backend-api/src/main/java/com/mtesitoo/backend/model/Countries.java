package com.mtesitoo.backend.model;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class Countries {
    private final Integer mId;
    private final String mName;
    public Countries(Integer id, String name) {
        mId = id;
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Countries countries = (Countries) o;

        if (mId != countries.mId) return false;
        return !(mName != null ? !mName.toString().equals(countries.mName) : countries.mName != null);
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);

        return result;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
