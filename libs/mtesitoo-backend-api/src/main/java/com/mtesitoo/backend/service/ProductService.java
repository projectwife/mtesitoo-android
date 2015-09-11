package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.service.logic.IProductServiceResponse;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.ILoginService;
import com.mtesitoo.backend.service.logic.IProductService;
import com.mtesitoo.backend.model.Product;

import org.json.JSONException;

/**
 * Opencart API-based implementation of {@link IProductService}.
 *
 * @author danieldanciu
 */
public class ProductService implements IProductService {
    private static final String TAG = ProductService.class.getSimpleName();
    private final RequestQueue mRequestQueue;
    private final ILoginService mILoginService;
    private Context mContext;
    private IResponse<List<Product>> mCallback;

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
                IProductServiceResponse productServiceResponse = new ProductServiceResponse();
                List<Product> products = productServiceResponse.parseResponse(response);

                if (mCallback != null)
                    mCallback.onResult(products);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public ProductService(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mILoginService = new LoginService(mContext);
    }

    @Override
    public void getProducts(final IResponse<List<Product>> callback) {
        final String url = mContext.getString(R.string.server) + mContext.getString(R.string.path_module_latest);
        mCallback = callback;

        mILoginService.getAuthToken(new IResponse<String>() {
            @Override
            public void onResult(final String result) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
                    @Override
                    public HashMap<String, String> getHeaders() {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put(mContext.getString(R.string.header_authorization), mContext.getString(R.string.bearer) + " " + result);
                        params.put(mContext.getString(R.string.header_accept), mContext.getString(R.string.application_json));
                        return params;
                    }
                };

                mRequestQueue.add(stringRequest);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void submitProduct(final IResponse<Product> callback) {
        mILoginService.getAuthToken(new IResponse<String>() {
            @Override
            public void onResult(final String result) {
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}