package com.mtesitoo.backend.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.CountriesCache;
import com.mtesitoo.backend.cache.logic.ICountriesCache;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.Zone;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IZoneRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class ZoneRequest extends Request implements IZoneRequest {


    //Samuel 18/05/2016
    //protected Context mContext;
    protected SharedPreferences.Editor mEditor;
    protected SharedPreferences mPrefs;

    String selectedCountryId;

    public ZoneRequest(Context context) {
        super(context);
        mContext=context;
        mILoginRequest = new LoginRequest(mContext);

        //Samuel 18/05/2016
        mPrefs = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

    }

    @Override
    public void getZones(ICallback<List<Zone>> callback) {

        //get the countries id

       selectedCountryId=mPrefs.getString("SelectedCountries","195");;
       //System.out.println( "Selected countried in ZoneRequest "+ mPrefs.getString("SelectedCountries",""));

        ICountriesCache cache = new CountriesCache(mContext);
        List<Countries> countries = cache.getCountries();

        for (Countries c : countries) {
            if (c.getName().equals(selectedCountryId)) {
                selectedCountryId = Integer.toString(c.getId());

                break;
            }
        }


        URL url = new URL(mContext, R.string.path_common_country);
       // System.out.println("I am testing Zone id here1 countries id"+url.toString()+"/"+selectedCountryId);
        ZoneResponse response = new ZoneResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString()+"/"+selectedCountryId, response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }


}
