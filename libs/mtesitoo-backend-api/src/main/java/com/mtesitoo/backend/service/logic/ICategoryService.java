package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Category;

import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public interface ICategoryService {
    void getCategories(IResponse<List<Category>> callback);
}
