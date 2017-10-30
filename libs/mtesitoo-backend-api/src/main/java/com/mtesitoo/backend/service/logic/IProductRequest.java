package com.mtesitoo.backend.service.logic;

import android.net.Uri;

import com.mtesitoo.backend.model.Product;

import java.util.List;

/**
 * Request for retrieving products (e.g. specials, search, etc.)
 * The callback results are returned on the main thread.
 */
public interface IProductRequest {
    void getProducts(int id, ICallback<List<Product>> callback);

    void getProduct(int id, ICallback<Product> callback);

    void submitProduct(Product product, ICallback<String> callback);

    void updateProduct(Product product, ICallback<String> callback);

    void submitProductPicture(int productId, Uri thumbnail, boolean isMainPicture, ICallback<String> callback);

    void deleteProductImage(Product product, String fileName, ICallback<Product> callback);
}