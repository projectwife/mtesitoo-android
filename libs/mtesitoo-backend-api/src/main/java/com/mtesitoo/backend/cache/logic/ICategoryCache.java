package com.mtesitoo.backend.cache.logic;

import com.mtesitoo.backend.model.Category;

import java.util.List;

/**
 * Created by Nan on 10/3/2015.
 */
public interface ICategoryCache {
    void storeCategories(List<Category> categories);

    List<Category> getCategories();
}
