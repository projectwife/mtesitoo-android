package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Seller;

/**
 * Created by Nan on 9/13/2015.
 */
public interface ISellerRequest {
    void getSellerInfo(int id, final ICallback<Seller> callback);
    void updateSellerProfile(final Seller seller, final ICallback<Seller> callback);
}