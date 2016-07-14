package com.mtesitoo.backend.service;

import android.content.Context;
import android.util.Log;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.url.ProductImageURL;
import com.mtesitoo.backend.model.url.VendorProductsURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IRegistrationRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationRequest extends Request implements IRegistrationRequest {
    public RegistrationRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void getSeller(int id, ICallback<List<Seller>> callback) {
    }

    @Override
    public void submitSeller(final Seller seller, final ICallback<Seller> callback) {
        System.out.println("seller--" + seller);
        URL url = new URL(mContext, R.string.path_vendor_register);
        Log.d("Registration", "Registering Seller: " + seller.toString());
        RegistrationResponse response = new RegistrationResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_register_name), seller.getUsername());
                Log.d("Registration","Username: " + seller.getUsername());

                if (seller.getmPassword() != null)
                    params.put(mContext.getString(R.string.params_register_password), seller.getmPassword());
                if (seller.getmFirstName() != null)
                    params.put(mContext.getString(R.string.params_register_firstname), seller.getmFirstName());
                if (seller.getmLastName() != null)
                    params.put(mContext.getString(R.string.params_register_lastname), seller.getmLastName());
                if (seller.getmEmail() != null)
                    params.put(mContext.getString(R.string.params_register_email), seller.getmEmail());
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
                if (seller.getmPhoneNumber() != null)
                    params.put(mContext.getString(R.string.params_register_telephone), seller.getmPhoneNumber());
                if (seller.getmCompany() != null)
                    params.put(mContext.getString(R.string.params_register_company), seller.getmCompany());
                if (seller.getmAgree() != null)
                    params.put(mContext.getString(R.string.params_register_agree), seller.getmAgree());

                return params;
            }

        };

        Log.d("mAuthorizationCache",mAuthorizationCache.getAuthorization());
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void submitSellerImage(Seller seller, ICallback<Seller> callback) {

    }

    @Override
    public void deleteSellerImage(Seller seller, String fileName, ICallback<Product> callback) {

    }
}