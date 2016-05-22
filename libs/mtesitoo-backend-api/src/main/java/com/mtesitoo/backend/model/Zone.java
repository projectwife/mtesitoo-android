package com.mtesitoo.backend.model;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class Zone {
    private final Integer mZone_id;
    private final String mName;
    private final String mCode;

    public Zone(Integer id, String name, String code) {
        mZone_id = id;
        mName = name;
        mCode=code;



    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Zone zone = (Zone) o;

        if (mZone_id != zone.mZone_id) return false;
        return !(mName != null ? !mName.toString().equals(zone.mName) : zone.mName != null);
    }

    @Override
    public int hashCode() {
        int result = mZone_id != null ? mZone_id.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);

        return result;
    }

    public int getId() {
        return mZone_id;
    }

    public String getName() {
        return mName;
    }
}
