package com.mtesitoo.backend.service.logic;

import android.net.Uri;

import java.util.List;

import com.mtesitoo.backend.model.Product;

/**
 * Request for retrieving products (e.g. specials, search, etc.)
 * The callback results are returned on the main thread.
 */
public interface IProductRequest {
    void getProducts(int id, ICallback<List<Product>> callback);

    void getProduct(int id, ICallback<Product> callback);

    void submitProduct(Product product, ICallback<String> callback);

    void submitProductThumbnail(int productId, Uri thumbnail, ICallback<String> callback);

    void submitProductImage(Product product, ICallback<Product> callback);

    void deleteProductImage(Product product, String fileName, ICallback<Product> callback);
}