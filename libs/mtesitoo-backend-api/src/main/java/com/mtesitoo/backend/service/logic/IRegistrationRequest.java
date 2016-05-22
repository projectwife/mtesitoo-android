package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;

import java.util.List;

/**
 * Created by Samuel on 2016/5/6 0006.
 * /**
 * Request for retrieving products (e.g. specials, search, etc.)
 * The callback results are returned on the main thread.
 */

public interface IRegistrationRequest {

    void getSeller(int id, ICallback<List<Seller>> callback);

    void submitSeller(Seller seller, ICallback<Seller> callback);

    void submitSellerImage(Seller seller, ICallback<Seller> callback);

    void deleteSellerImage(Seller seller, String fileName, ICallback<Product> callback);
}
