package com.mtesitoo.backend.service;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IVendorTerms;

/**
 * Created by ptyagi on 6/4/17.
 */

public class VendorTermsRequest extends Request implements IVendorTerms {
    public VendorTermsRequest(Context context) {
        super(context);
    }

    @Override
    public void getVendorTerms(ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_common_vendor_terms);
        VendorTermsResponse response = new VendorTermsResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}
