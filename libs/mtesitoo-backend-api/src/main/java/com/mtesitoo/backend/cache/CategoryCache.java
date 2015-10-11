package com.mtesitoo.backend.cache;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Nan on 10/3/2015.
 */
public class CategoryCache extends Cache implements ICategoryCache {

    public CategoryCache(Context context) {
        super(context);
    }

    public void storeCategories(List<Category> categories) {
        Set<String> set = new HashSet<>();

        for (Category category : categories) {
            set.add(Integer.toString(category.getId()) + ":" + category.getName());
        }

        mEditor.putStringSet(mContext.getString(R.string.shared_preference_key_categories), set);
        mEditor.apply();
    }

    public List<Category> getCategories() {
        Set<String> set = mPrefs.getStringSet(mContext.getString(R.string.shared_preference_key_categories), new HashSet<String>());
        List<Category> categories = new ArrayList<>();

        for (String s : set) {
            String[] parts = s.split(":", 2);
            categories.add(new Category(Integer.parseInt(parts[0]), parts[1]));
        }

        return categories;
    }
}