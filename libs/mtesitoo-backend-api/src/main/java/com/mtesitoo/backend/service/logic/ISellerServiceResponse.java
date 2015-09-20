package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Seller;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nan on 9/13/2015.
 */
public interface ISellerServiceResponse {
    Seller parseResponse(String response) throws JSONException;
}