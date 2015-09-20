package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Category;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public interface ICategoryServiceResponse {
    List<Category> parseResponse(String response) throws JSONException;
}
