package com.mtesitoo.backend.service;

import android.content.Context;
import android.util.Log;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.VendorURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nan on 9/13/2015.
 */
public class SellerRequest extends Request implements ISellerRequest {
    public SellerRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void getSellerInfo(final int sellerId, final ICallback<Seller> callback) {
        Log.d("getSellrInfo - SellerId",String.valueOf(sellerId));
        URL url = new VendorURL(mContext, R.string.path_product_vendor, sellerId);
        SellerResponse response = new SellerResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void updateSellerProfile(final Seller seller, final ICallback<Seller> callback) {
        System.out.println("seller--" + seller);
        URL url = new URL(mContext, R.string.path_vendor_profile);
        Log.d("SellerProfile", "Updating Seller Profile: " + seller.toString());
        RegistrationResponse response = new RegistrationResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //if (seller.getmPassword() != null)
                    //params.put(mContext.getString(R.string.params_register_password), seller.getmPassword());
                if (seller.getmBusiness() != null)
                    params.put(mContext.getString(R.string.params_register_company), seller.getmBusiness());
                if (seller.getmDescription() != null)
                    params.put(mContext.getString(R.string.params_business_description), seller.getmDescription());
                if (seller.getmFirstName() != null)
                    params.put(mContext.getString(R.string.params_register_firstname), seller.getmFirstName());
                if (seller.getmLastName() != null)
                    params.put(mContext.getString(R.string.params_register_lastname), seller.getmLastName());
                if (seller.getmEmail() != null)
                    params.put(mContext.getString(R.string.params_register_email), seller.getmEmail());
                if (seller.getmPhoneNumber() != null)
                    params.put(mContext.getString(R.string.params_register_telephone), seller.getmPhoneNumber());
                if (seller.getmAddress1() != null)
                    params.put(mContext.getString(R.string.params_register_address_1), seller.getmAddress1());
                if (seller.getmAddress2() != null)
                    params.put(mContext.getString(R.string.params_register_address_2), seller.getmAddress2());
                if (seller.getmCity() != null)
                    params.put(mContext.getString(R.string.params_register_city), seller.getmCity());
                if (seller.getmPostcode() != null)
                    params.put(mContext.getString(R.string.params_register_postcode), seller.getmPostcode());
                if (seller.getmCountry() != null)
                    params.put(mContext.getString(R.string.params_register_country_id), seller.getmCountry());
                if (seller.getmZoneId() != null)
                    params.put(mContext.getString(R.string.params_register_zone_id), seller.getmZoneId());


                //if (seller.getmAgree() != null)
                    //params.put(mContext.getString(R.string.params_register_agree), seller.getmAgree());

                return params;
            }

        };

        Log.d("mAuthorizationCache",mAuthorizationCache.getAuthorization());
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}