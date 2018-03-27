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
    private String productName;
    private String productDescription;
    private String productLocation;
    private String productExpirationDate;

    private String productCategory;
    private String productQuantity;
    private String productPricePerUnit;
    private String productUnits;
    private String productCustomUnits;


    private List<Uri> productPictures;

    public final static int MAX_PICTURES = 4;

    public static AddProductHelper getInstance() {
        if (instance == null) {
            instance = new AddProductHelper();
            instance.clearFields();
        }
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
        h.put(Constants.PRODUCT_CUSTOM_UNITS_KEY, productCustomUnits);

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

    public void setProductCustomUnits(String customUnits) {
        this.productCustomUnits = customUnits;
    }

    public void setProductPictures(List<Uri> productPictures) {
        this.productPictures = productPictures;
    }

    public void clearFields() {

        this.productName = "";
        this.productDescription = "";
        this.productLocation = "";
        this.productExpirationDate = "";

        this.productCategory = "";
        this.productQuantity = "";
        this.productPricePerUnit = "";
        this.productUnits = "";
        this.productCustomUnits = "";

        this.productPictures = null;
    }

    public Product getProduct() {
        List<Uri> auxImages = null;
        Uri mainPicture = null;
        if (productPictures.size() > 0) {
            mainPicture = productPictures.get(0);
            if (productPictures.size() > 1) {
                auxImages = productPictures.subList(1, productPictures.size());
            }
        }

        return new Product(0, productName, productDescription, productLocation, productCategory,
                productUnits, productCustomUnits, productPricePerUnit,
                ProductPriceHelper.getDisplayPrice(ProductPriceHelper.getDefaultCurrencyCode(), productPricePerUnit),
                ProductPriceHelper.getDefaultCurrencyCode(),
                Integer.parseInt(productQuantity), DateHelper.parseDate(productExpirationDate), mainPicture, auxImages, 5, 0, 0);
    }
}
