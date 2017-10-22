package com.mtesitoo.helper;

import android.net.Uri;

import com.mtesitoo.Constants;
import com.mtesitoo.backend.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eduardodiaz on 19/10/2017.
 */

public class AddProductHelper {


    private static AddProductHelper instance;
    private String productName = "";
    private String productDescription = "";
    private String productLocation = "";
    private String productExpirationDate = "";

    private String productCategory = "";
    private String productQuantity = "";
    private String productPricePerUnit = "";
    private String productUnits = "";


    private List<Uri> productPictures;

    public final static int MAX_PICTURES = 4;

    public static AddProductHelper getInstance() {
        if (instance == null) instance = new AddProductHelper();
        return instance;
    }

    private AddProductHelper() {
        productPictures = new ArrayList<>(4);
    }

    public Map<String, String> getProductDetailsData() {
        HashMap<String, String> h = new HashMap<>();

        h.put(Constants.PRODUCT_NAME_KEY, productName);
        h.put(Constants.PRODUCT_DESCRIPTION_KEY, productDescription);
        h.put(Constants.PRODUCT_LOCATION_KEY, productLocation);
        h.put(Constants.PRODUCT_EXPIRATION_KEY, productExpirationDate);

        return h;
    }

    public HashMap<String, String> getProductQuantityData() {

        HashMap<String, String> h = new HashMap<>();

        h.put(Constants.PRODUCT_CATEGORY_KEY, productCategory);
        h.put(Constants.PRODUCT_PRICE_KEY, productPricePerUnit);
        h.put(Constants.PRODUCT_QUANTITY_KEY, productQuantity);
        h.put(Constants.PRODUCT_UNITS_KEY, productUnits);

        return h;
    }

    public List<Uri> getProductPictureListData() {

        if (productPictures == null) {
            productPictures = new ArrayList<>(MAX_PICTURES);
        }
        return productPictures;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public void setProductExpirationDate(String productExpirationDate) {
        this.productExpirationDate = productExpirationDate;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductPricePerUnit(String productPricePerUnit) {
        this.productPricePerUnit = productPricePerUnit;
    }

    public void setProductUnits(String productUnits) {
        this.productUnits = productUnits;
    }

    public void setProductPictures(List<Uri> productPictures) {
        this.productPictures = productPictures;
    }

    public void clearFields() {

        this.productName = null;
        this.productDescription = null;
        this.productCategory = null;
        this.productQuantity = null;
        this.productPricePerUnit = null;
        this.productExpirationDate = null;
        this.productUnits = null;
        this.productLocation = null;
        this.productPictures = null;
    }

    public Product getProduct() {
        return null;
    }
}
