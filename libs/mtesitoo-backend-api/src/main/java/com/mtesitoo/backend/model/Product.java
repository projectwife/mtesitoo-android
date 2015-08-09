package com.mtesitoo.backend.model;

import android.net.Uri;

import java.math.BigDecimal;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Model object for a Mtesitoo product.
 */
public class Product implements Parcelable {
    private final String mName;
    private final String mDescription;
    private final String mLocation;
    private final String mCategory;
    private final String mSIUnit;
    private final String mPricePerUnit;
    private final Integer mQuantity;
    private final Date mExpiration;
    private final Uri mThumbnail;

    /**
     * Only exists if this is a special offer.
     */
    @Nullable
    private final BigDecimal mSpecialPrice;

    private final Seller mSeller;

    private Product(Parcel in) {

        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mLocation = in.readString();
        this.mCategory = in.readString();
        this.mSIUnit = in.readString();
        this.mPricePerUnit = in.readString();
        this.mQuantity = in.readInt();
        this.mExpiration = new Date(in.readLong());

        this.mThumbnail = null;
        this.mSpecialPrice = null;
        this.mSeller = null;
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    /**
     * @param name         product name (e.g. apples)
     * @param description  product descriptions (e.g. freshly picked, sweet-sour)
     * @param location     product location (e.g. 123 fake street)
     * @param category     product category (e.g. fruits & nuts)
     * @param siUnit       product si unit measurement (e.g. liter, gram)
     * @param pricePerUnit product price per quantity (e.g. $2.5)
     * @param quantity     product quantity (e.g. 100)
     * @param expiration   product post expiration (e.g. YYYY-MM-DD)
     * @param thumbnail    url of the product's thumbnail
     * @param specialPrice special price, if this is a special offer, {@code null otherwise}
     * @param seller       product owner
     */
    public Product(String name, String description, String location, String category, String siUnit,
                   String pricePerUnit, Integer quantity, Date expiration, Uri thumbnail,
                   @Nullable BigDecimal specialPrice, Seller seller) {
        mName = name;
        mDescription = description;
        mLocation = location;
        mCategory = category;
        mSIUnit = siUnit;
        mPricePerUnit = pricePerUnit;
        mQuantity = quantity;
        mExpiration = expiration;

        mThumbnail = thumbnail;
        mSpecialPrice = specialPrice;
        mSeller = seller;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getSIUnit() {
        return mSIUnit;
    }

    public String getPricePerUnit() {
        return mPricePerUnit;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public Date getExpiration() {
        return mExpiration;
    }

    public Uri getThumbnail() {
        return mThumbnail;
    }

    @Nullable
    public BigDecimal getSpecialPrice() {
        return mSpecialPrice;
    }

    public Seller getSeller() {
        return mSeller;
    }

    @Override
    public String toString() {
        return "Product{" + "name='" + mName + '\'' + ", description='" + mDescription + '\''
                + ", price='" + mPricePerUnit + '\'' + ", thumbnail=" + mThumbnail + ", specialPrice="
                + mSpecialPrice + ", seller=" + mSeller + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Product product = (Product) o;

        if (!mName.equals(product.mName))
            return false;
        if (mDescription != null ? !mDescription.equals(product.mDescription)
                : product.mDescription != null)
            return false;
        return !(mSeller != null ? !mSeller.equals(product.mSeller) : product.mSeller != null);

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mSeller != null ? mSeller.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mLocation);
        dest.writeString(mCategory);
        dest.writeString(mSIUnit);
        dest.writeString(mPricePerUnit);
        dest.writeInt(mQuantity);
        dest.writeLong(mExpiration.getTime());
    }
}