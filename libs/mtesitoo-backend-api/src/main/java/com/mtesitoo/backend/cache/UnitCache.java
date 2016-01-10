package com.mtesitoo.backend.cache;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.IUnitCache;
import com.mtesitoo.backend.model.Unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Nan on 1/9/2016.
 */
public class UnitCache extends Cache implements IUnitCache {
    public UnitCache(Context context) {
        super(context);
    }

    public void storeLengthUnits(List<Unit> units) {
        storeUnits(units, R.string.shared_preference_key_length_units);
    }

    public void storeWeightUnits(List<Unit> units) {
        storeUnits(units, R.string.shared_preference_key_weight_units);
    }

    private void storeUnits(List<Unit> units, int stringId) {
        Set<String> set = new HashSet<>();

        for (Unit unit : units) {
            set.add(Integer.toString(unit.getId()) + ":" + unit.getName() + ":" + unit.getUnit());
        }

        mEditor.putStringSet(mContext.getString(stringId), set);
        mEditor.apply();
    }

    public List<Unit> getLengthUnits() {
        return getUnits(R.string.shared_preference_key_length_units);
    }

    public List<Unit> getWeightUnits() {
        return getUnits(R.string.shared_preference_key_weight_units);
    }

    private List<Unit> getUnits(int stringId) {
        Set<String> set = mPrefs.getStringSet(mContext.getString(stringId), new HashSet<String>());
        List<Unit> units = new ArrayList<>();

        for (String s : set) {
            String[] parts = s.split(":", 3);
            units.add(new Unit(Integer.parseInt(parts[0]), parts[1], parts[2]));
        }

        return units;
    }
}