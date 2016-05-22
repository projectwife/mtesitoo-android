package com.mtesitoo.backend.cache;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Zone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Samuel on 2016/5/12 0012.
 */
public class ZoneCache extends Cache implements IZonesCache {

    public ZoneCache(Context context) {
        super(context);
    }
    @Override
    public void storeZones(List<Zone> zones) {
        Set<String> set = new HashSet<>();

        for (Zone zone : zones) {
            set.add(Integer.toString(zone.getId()) + ":" + zone.getName());
        }

        mEditor.putStringSet(mContext.getString(R.string.shared_preference_key_zones), set);
        mEditor.apply();
    }

    @Override
    public List<Zone> GetZones() {
        Set<String> set = mPrefs.getStringSet(mContext.getString(R.string.shared_preference_key_zones), new HashSet<String>());
        List<Zone>  zones = new ArrayList<>();

        for (String s : set) {
            String[] parts = s.split(":", 2);
            zones.add(new Zone(Integer.parseInt(parts[0]), parts[1],"BU"));
        }

        return zones;
    }
}
