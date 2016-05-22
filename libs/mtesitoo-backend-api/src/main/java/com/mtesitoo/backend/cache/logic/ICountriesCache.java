package com.mtesitoo.backend.cache.logic;


import com.mtesitoo.backend.model.Countries;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public interface ICountriesCache {

    void storeCountries(List<Countries> countries);

    List<Countries> getCountries();
}
