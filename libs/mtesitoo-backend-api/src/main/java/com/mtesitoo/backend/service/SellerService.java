package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.ProductVendorURL;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.ISellerService;
import com.mtesitoo.backend.service.logic.ISellerServiceResponse;

import org.json.JSONException;

/**
 * Created by Nan on 9/13/2015.
 */
public class SellerService extends Service implements ISellerService {
    private IResponse<Seller> mCallback;
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
                ISellerServiceResponse sellerServiceResponse = new SellerServiceResponse();
                Seller seller = sellerServiceResponse.parseResponse(response);

                if (mCallback != null)
                    mCallback.onResult(seller);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public SellerService(Context context) {
        super(context);
        mILoginService = new LoginService(mContext);
    }

    @Override
    public void getSellerInfo(final int sellerId, final IResponse<Seller> callback) {
        mCallback = callback;
        mILoginService.getAuthToken(new IResponse<String>() {
            @Override
            public void onResult(final String result) {
                URL url = new ProductVendorURL(mContext, R.string.path_product_vendor, sellerId);
                AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, Request.Method.GET, url.toString(), listener, errorListener);
                stringRequest.setAuthorization(new Authorization(mContext, result).toString());
                mRequestQueue.add(stringRequest);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}