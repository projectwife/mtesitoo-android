package com.mtesitoo.backend.cache;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Zone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        mEditor.putString(mContext.getString(R.string.shared_preference_key_zones_timestamp),
                SimpleDateFormat.getInstance().format(Calendar.getInstance().getTime()));

        mEditor.apply();
    }

    @Override
    public List<Zone> GetZones() {
        List<Zone> zones = new ArrayList<>();

        String stringTimestamp = mPrefs.getString(mContext.getString(R.string.shared_preference_key_zones_timestamp), "");
        if (!stringTimestamp.isEmpty()) {
            try {
                Date storedDate = SimpleDateFormat.getInstance().parse(stringTimestamp);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(storedDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                if (calendar.getTime().after(Calendar.getInstance().getTime())) return getZones();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return zones;
    }

    private List<Zone> getZones() {
        List<Zone> zones = new ArrayList<>();
        Set<String> set = mPrefs.getStringSet(mContext.getString(R.string.shared_preference_key_zones), new HashSet<String>());

        for (String s : set) {
            String[] parts = s.split(":", 2);
            zones.add(new Zone(Integer.parseInt(parts[0]), parts[1], "BU"));
        }

        return zones;
    }
}
