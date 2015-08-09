package com.mtesitoo.backend.logic;

import java.util.List;

import com.mtesitoo.backend.api.Callback;
import com.mtesitoo.backend.model.Product;

/**
 * Service for retrieving products (e.g. specials, search, etc.)
 * The callback results are returned on the main thread.
 */
public interface ProductService {
    void getSpecials(Callback<List<Product>> callback);
}
