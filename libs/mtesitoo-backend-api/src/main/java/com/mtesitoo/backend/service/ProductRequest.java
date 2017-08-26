package com.mtesitoo.backend.service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.mtesitoo.backend.MultipartRequest;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.url.ProductImageURL;
import com.mtesitoo.backend.model.url.ProductURL;
import com.mtesitoo.backend.model.url.VendorProductsURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        URL url = new VendorProductsURL(mContext, R.string.path_vendor_products);
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
                params.put(mContext.getString(R.string.params_product_category_ids), product.getCategoriesIDStringList());
                params.put(mContext.getString(R.string.params_product_meta_title), "meta_title");
                params.put(mContext.getString(R.string.params_product_status), mContext.getString(R.string.params_product_status_enabled));

                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void updateProduct(final Product product, ICallback<String> callback) {
        Log.d("Product",product.toString());
        URL url = new URL(mContext, R.string.path_product_product);
        Log.d("Update Product URL",url.toString());
        ProductUpdateResponse response = new ProductUpdateResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString()+ "/" +product.getId(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_product_name), product.getName());
                params.put(mContext.getString(R.string.params_product_description), product.getDescription());
                params.put(mContext.getString(R.string.params_product_price), product.getPricePerUnit());
                params.put(mContext.getString(R.string.params_product_quantity), Integer.toString(product.getQuantity()));
                params.put(mContext.getString(R.string.params_product_category_ids), product.getCategoriesIDStringList());
                params.put(mContext.getString(R.string.params_product_expiry), product.getExpirationFormattedForAPI());
                //params.put(mContext.getString(R.string.params_product_location), product.getLocation());
                //params.put(mContext.getString(R.string.params_product_meta_title), "meta_title");
                //params.put(mContext.getString(R.string.params_product_status), mContext.getString(R.string.params_product_status_enabled));

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

        if (image == null) {
            Toast.makeText(mContext, "No Product photo selected yet.", Toast.LENGTH_LONG).show();
            return;
        }

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
            Toast.makeText(mContext, "Failed to upload Product photo.", Toast.LENGTH_LONG).show();
        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void submitProductImage(final Product product, ICallback<String> callback) {
        URL url = new ProductImageURL(mContext, R.string.path_product_product, product.getId());
        final Uri imageUri = product.getLastImage();

        String imageFilePath = getRealPathFromURI(imageUri);
        final String imageFileName = imageFilePath.substring(imageFilePath.lastIndexOf("/")+1);

        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        //Bitmap bm = BitmapFactory.decodeFile(image.getPath());

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
                params.put("file", new DataPart(imageFileName, imageBytes, "image/jpeg"));

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