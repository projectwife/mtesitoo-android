package com.mtesitoo.backend.cache;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.cache.logic.ICountriesCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Countries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class CountriesCache extends Cache implements ICountriesCache {

    public CountriesCache(Context context) {
        super(context);
    }
    @Override
    public void storeCountries(List<Countries> countries) {
        Set<String> set = new HashSet<>();

        for (Countries country : countries) {
            set.add(Integer.toString(country.getId()) + ":" + country.getName());
        }

        mEditor.putStringSet(mContext.getString(R.string.shared_preference_key_countries), set);
        mEditor.apply();
    }

    @Override
    public List<Countries> getCountries() {
        Set<String> set = mPrefs.getStringSet(mContext.getString(R.string.shared_preference_key_countries), new HashSet<String>());
        List<Countries> countries = new ArrayList<>();

        for (String s : set) {
            String[] parts = s.split(":", 2);
            countries.add(new Countries(Integer.parseInt(parts[0]), parts[1]));
        }

        return countries;
    }
}
