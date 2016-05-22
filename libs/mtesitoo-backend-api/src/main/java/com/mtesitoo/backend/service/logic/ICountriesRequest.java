package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Countries;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public interface ICountriesRequest {
    void getCountries(ICallback<List<Countries>> callback);
}
