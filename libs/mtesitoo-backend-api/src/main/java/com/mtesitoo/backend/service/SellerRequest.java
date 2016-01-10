package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.VendorURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.backend.service.logic.ISellerServiceResponse;

import org.json.JSONException;

/**
 * Created by Nan on 9/13/2015.
 */
public class SellerRequest extends Request implements ISellerRequest {
    private ICallback<Seller> mCallback;
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mCallback.onError(error);
        }
    };

    private Response.Listener listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ISellerServiceResponse sellerServiceResponse = new SellerResponse();
                Seller seller = sellerServiceResponse.parseResponse(response);

                if (mCallback != null)
                    mCallback.onResult(seller);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public SellerRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void getSellerInfo(final int sellerId, final ICallback<Seller> callback) {
        mCallback = callback;
        URL url = new VendorURL(mContext, R.string.path_product_vendor, sellerId);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), listener, errorListener);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}