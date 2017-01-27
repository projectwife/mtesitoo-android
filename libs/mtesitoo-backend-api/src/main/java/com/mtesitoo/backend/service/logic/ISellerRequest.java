package com.mtesitoo.backend.service.logic;

import android.net.Uri;

import com.mtesitoo.backend.model.Seller;

/**
 * Created by Nan on 9/13/2015.
 */
public interface ISellerRequest {
    void getSellerInfo(int id, final ICallback<Seller> callback);
    void updateSellerProfile(final Seller seller, final ICallback<Seller> callback);
    void submitProfileImage(final Uri imageUri, final ICallback<String> callback);
    void deleteProfileImage(final ICallback<String> callback);
    void updatePassword(final String oldPassword, final String newPassword, final ICallback<String> callback);
}