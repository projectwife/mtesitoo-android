package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Product;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nan on 9/7/2015.
 */
public interface IProductServiceResponse {
    List<Product> parseResponse(String response) throws JSONException;
}
