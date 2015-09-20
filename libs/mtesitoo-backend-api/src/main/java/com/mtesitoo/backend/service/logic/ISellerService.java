package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Seller;

/**
 * Created by Nan on 9/13/2015.
 */
public interface ISellerService {
    void getSellerInfo(int id, final IResponse<Seller> callback);
}