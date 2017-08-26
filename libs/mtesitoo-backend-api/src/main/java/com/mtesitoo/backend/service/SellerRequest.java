package com.mtesitoo.backend.service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.mtesitoo.backend.MultipartRequest;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.url.VendorURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

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
        Log.d("getSellrInfo - SellerId",String.valueOf(sellerId));
        URL url = new VendorURL(mContext, R.string.path_product_vendor, sellerId);
        SellerResponse response = new SellerResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void updateSellerProfile(final Seller seller, final ICallback<Seller> callback) {
        System.out.println("seller--" + seller);
        URL url = new URL(mContext, R.string.path_vendor_profile);
        Log.d("SellerProfile", "Updating Seller Profile: " + seller.toString());
        SellerResponse response = new SellerResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //if (seller.getmPassword() != null)
                    //params.put(mContext.getString(R.string.params_register_password), seller.getmPassword());
                if (seller.getmBusiness() != null)
                    params.put(mContext.getString(R.string.params_register_company), seller.getmBusiness());
                if (seller.getmDescription() != null)
                    params.put(mContext.getString(R.string.params_business_description), seller.getmDescription());
                if (seller.getmFirstName() != null)
                    params.put(mContext.getString(R.string.params_register_firstname), seller.getmFirstName());
                if (seller.getmLastName() != null)
                    params.put(mContext.getString(R.string.params_register_lastname), seller.getmLastName());
                if (seller.getmEmail() != null)
                    params.put(mContext.getString(R.string.params_register_email), seller.getmEmail());
                if (seller.getmPhoneNumber() != null)
                    params.put(mContext.getString(R.string.params_register_telephone), seller.getmPhoneNumber());
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


                //if (seller.getmAgree() != null)
                    //params.put(mContext.getString(R.string.params_register_agree), seller.getmAgree());

                return params;
            }

        };

        Log.d("mAuthorizationCache",mAuthorizationCache.getAuthorization());
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
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
    public void submitProfileImage(final Uri imageUri, final ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_vendor_profile_image);

        String imageFilePath = getRealPathFromURI(imageUri);
        final String imageFileName = imageFilePath.substring(imageFilePath.lastIndexOf("/")+1);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);

        int DESIREDWIDTH = 500;
        int DESIREDHEIGHT = 500;

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, DESIREDWIDTH, DESIREDHEIGHT, true);
        bitmap.recycle();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        final byte[] imageBytes = baos.toByteArray();

        ProfileThumbnailResponse response = new ProfileThumbnailResponse(callback);

        MultipartRequest multipartRequest =
                new MultipartRequest(mContext, url.toString(), response, response){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return new HashMap<>();
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                // file name could find file base or direct access from real path
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
    public void deleteProfileImage(ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_vendor_profile_image);
        ProfileThumbnailResponse response = new ProfileThumbnailResponse(callback);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.DELETE, url.toString(), response, response);
        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void updatePassword(final String oldPassword, final String newPassword, final ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_admin_password);
        UpdatePasswordResponse response = new UpdatePasswordResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_admin_old_password), oldPassword);
                params.put(mContext.getString(R.string.params_admin_new_password), newPassword);
                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void forgotPassword(final String username, final ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_admin_password);
        ForgotPasswordResponse response = new ForgotPasswordResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_admin_username), username);
                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());

        mRequestQueue.add(stringRequest);
    }
}