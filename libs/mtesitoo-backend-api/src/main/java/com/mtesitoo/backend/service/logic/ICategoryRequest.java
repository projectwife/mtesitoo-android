package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Category;

import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public interface ICategoryRequest {
    void getCategories(ICallback<List<Category>> callback);
}
