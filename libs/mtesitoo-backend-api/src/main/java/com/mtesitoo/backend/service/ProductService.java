package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.ProductProductURL;
import com.mtesitoo.backend.model.url.ProductVendorProductsURL;
import com.mtesitoo.backend.service.logic.IProductServiceResponse;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.IProductService;
import com.mtesitoo.backend.model.Product;

import org.json.JSONException;

/**
 * Opencart API-based implementation of {@link IProductService}.
 *
 * @author danieldanciu
 */
public class ProductService extends Service implements IProductService {
    private static final String TAG = ProductService.class.getSimpleName();
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
        super(context);
        mILoginService = new LoginService(mContext);
    }

    @Override
    public void getProducts(final int sellerId, final IResponse<List<Product>> callback) {
        mCallback = callback;
        URL url = new ProductVendorProductsURL(mContext, R.string.path_product_vendor, sellerId);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, Request.Method.GET, url.toString(), listener, errorListener);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void submitProduct(final Product product, final IResponse<Product> callback) {
        URL url = new ProductProductURL(mContext, R.string.path_product_product);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, Request.Method.POST, url.toString(), listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_product_name), product.getName());
                params.put(mContext.getString(R.string.params_product_description), product.getDescription());
                params.put(mContext.getString(R.string.params_product_price), product.getPricePerUnit());
                params.put(mContext.getString(R.string.params_product_quantity), Integer.toString(product.getQuantity()));
                params.put(mContext.getString(R.string.params_product_category_ids), product.getCategory());

                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}