package com.mtesitoo.backend.service;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.VendorURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;

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
        URL url = new VendorURL(mContext, R.string.path_product_vendor, sellerId);
        SellerResponse response = new SellerResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}