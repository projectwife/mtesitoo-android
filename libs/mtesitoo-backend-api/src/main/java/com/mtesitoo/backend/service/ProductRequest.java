package com.mtesitoo.backend.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.ExifUtil;
import com.mtesitoo.backend.MultipartRequest;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.ScalingUtilities;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.url.ProductImageURL;
import com.mtesitoo.backend.model.url.ProductURL;
import com.mtesitoo.backend.model.url.VendorProductsURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.backend.model.Product;

import org.json.JSONException;

import static android.R.attr.path;

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
    public void getProduct(int id, ICallback<Product> callback) {
        Log.d("getProducts - ProductId",String.valueOf(id));
        URL url = new ProductURL(mContext, R.string.path_product_product, id);
        Log.d("Product URL",url.toString());
        ProductDetailResponse response = new ProductDetailResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void submitProduct(final Product product, final ICallback<String> callback) {
        Log.d("Product",product.toString());
        URL url = new URL(mContext, R.string.path_product_product);
        Log.d("Submit Product URL",url.toString());
        ProductThumbnailResponse response = new ProductThumbnailResponse(callback);
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
    public void submitProductThumbnail(int productId, Uri thumbnail, ICallback<String> callback){
        URL url = new ProductImageURL(mContext, R.string.path_product_product, productId);
        final Uri image = thumbnail;

        InputStream inputStream = null;
        try {
            inputStream = mContext.getContentResolver().openInputStream(image);

            Bitmap bm = BitmapFactory.decodeStream(inputStream);

            int DESIREDWIDTH = 500;
            int DESIREDHEIGHT = 500;

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, DESIREDWIDTH, DESIREDHEIGHT, true);
            bm.recycle();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            final byte[] imageBytes = baos.toByteArray();

            ProductThumbnailResponse response = new ProductThumbnailResponse(callback);

            MultipartRequest multipartRequest = new MultipartRequest(mContext, url.toString(), response, response){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put(mContext.getString(R.string.params_product_image_main), "true");
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() throws AuthFailureError {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("file", new DataPart("mtesitoo_thumb_" + image.getLastPathSegment() + ".jpg", imageBytes, "image/jpeg"));

                    return params;
                }
            };

            multipartRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
            mRequestQueue.add(multipartRequest);

            resizedBitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void submitProductImage(final Product product, ICallback<Product> callback) {
        URL url = new ProductImageURL(mContext, R.string.path_product_product, product.getId());
        final Uri image = product.getLastImage();

        Bitmap bm = BitmapFactory.decodeFile(image.getPath());

        int DESIREDWIDTH = 500;
        int DESIREDHEIGHT = 500;

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, DESIREDWIDTH, DESIREDHEIGHT, true);
        bm.recycle();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        final byte[] imageBytes = baos.toByteArray();

        ProductUpdateResponse response = new ProductUpdateResponse(callback);

        MultipartRequest multipartRequest = new MultipartRequest(mContext, url.toString(), response, response){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_product_image_main), "false");
                params.put(mContext.getString(R.string.params_product_image_sort), "1");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("file", new DataPart(image.getLastPathSegment(), imageBytes, "image/jpeg"));

                return params;
            }
        };

        multipartRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(multipartRequest);

        resizedBitmap.recycle();
    }

    @Override
    public void deleteProductImage(final Product product, final String fileName, ICallback<Product> callback) {
        URL url = new ProductImageURL(mContext, R.string.path_product_product, product.getId());
        ProductImageDeleteResponse response = new ProductImageDeleteResponse(callback);
        url.append("?files=" + fileName);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.DELETE, url.toString(), response, response);
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}