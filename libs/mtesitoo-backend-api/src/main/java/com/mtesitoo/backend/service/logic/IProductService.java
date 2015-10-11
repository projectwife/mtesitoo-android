package com.mtesitoo.backend.service.logic;

import java.util.List;

import com.mtesitoo.backend.model.Product;

/**
 * Service for retrieving products (e.g. specials, search, etc.)
 * The callback results are returned on the main thread.
 */
public interface IProductService {
    void getProducts(int id, IResponse<List<Product>> callback);

    void submitProduct(Product product, IResponse<Product> callback);
}