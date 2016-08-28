package com.mtesitoo.backend.service;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.ProductImageURL;
import com.mtesitoo.backend.model.url.VendorProductsURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.backend.model.Product;

import org.json.JSONException;

/**
 * Opencart API-based implementation of {@link IProductRequest}.
 *
 * @author danieldanciu
 */
public class ProductRequest extends Request implements IProductRequest {
    public ProductRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void getProducts(final int sellerId, final ICallback<List<Product>> callback) {
        Log.d("getProducts - SellerId",String.valueOf(sellerId));
        URL url = new VendorProductsURL(mContext, R.string.path_product_vendor, sellerId);
        Log.d("Vendor Products URL",url.toString());
        ProductResponse response = new ProductResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void submitProduct(final Product product, final ICallback<Product> callback) {
        Log.d("Product",product.toString());
        URL url = new URL(mContext, R.string.path_product_product);
        Log.d("Submit Product URL",url.toString());
        ProductResponse response = new ProductResponse(null);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_product_name), product.getName());
                params.put(mContext.getString(R.string.params_product_description), product.getDescription());
                params.put(mContext.getString(R.string.params_product_price), product.getPricePerUnit());
                params.put(mContext.getString(R.string.params_product_quantity), Integer.toString(product.getQuantity()));
                params.put(mContext.getString(R.string.params_product_category_ids), product.getCategory());
                params.put(mContext.getString(R.string.params_product_meta_title), "meta_title");
                params.put(mContext.getString(R.string.params_product_status), mContext.getString(R.string.params_product_status_enabled));

                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void submitProductImage(final Product product, ICallback<Product> callback) {
        URL url = new ProductImageURL(mContext, R.string.path_product_product, product.getId());
        final Uri image = product.getLastImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        // Todo: need to prep the image for upload - stringify
        ProductResponse response = new ProductResponse(null);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), null, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("file",encodedImage);
                params.put(mContext.getString(R.string.params_product_image_main), "false");
                params.put(mContext.getString(R.string.params_product_image_sort), "1");
                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void deleteProductImage(final Product product, final String fileName, ICallback<Product> callback) {
        URL url = new ProductImageURL(mContext, R.string.path_product_product, product.getId());
        ProductResponse response = new ProductResponse(null);
        url.append("?file=" + fileName);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.DELETE, url.toString(), null, response);
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}