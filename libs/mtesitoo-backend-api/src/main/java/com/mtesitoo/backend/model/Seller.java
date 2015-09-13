package com.mtesitoo.backend.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Model object for a Mtesitoo farmer/seller.
 */
public class Seller implements Parcelable {
    public static final Seller DUMMY = new Seller("John Doe", new BigDecimal("5"), Uri.parse(""),
            "Gambiastreet 11", "+43 344 334 333", "john@doe.com");

    private final String mName;
    private final BigDecimal mRating;
    private final Uri mThumbnail;
    private final String mAddress;
    private final String mPhoneNumber;
    private final String mEmail;

    private Seller(Parcel in) {
        this.mName = in.readString();
        this.mRating = new BigDecimal(in.readInt());
        this.mThumbnail = Uri.parse(in.readString());
        this.mAddress = in.readString();
        this.mPhoneNumber = in.readString();
        this.mEmail = in.readString();
    }

    public static final Parcelable.Creator<Seller> CREATOR = new Parcelable.Creator<Seller>() {
        @Override
        public Seller createFromParcel(Parcel source) {
            return new Seller(source);
        }

        @Override
        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };


    public Seller(String name, BigDecimal rating, Uri thumbnail, String address, String phoneNumber,
                  String email) {
        this.mName = name;
        this.mRating = rating;
        this.mThumbnail = thumbnail;
        this.mAddress = address;
        this.mPhoneNumber = phoneNumber;
        this.mEmail = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seller seller = (Seller) o;

        if (mName != null ? !mName.equals(seller.mName) : seller.mName != null) return false;
        if (mRating != null ? !mRating.equals(seller.mRating) : seller.mRating != null) return false;
        if (mThumbnail != null ? !mThumbnail.equals(seller.mThumbnail) : seller.mThumbnail != null)
            return false;
        if (mAddress != null ? !mAddress.equals(seller.mAddress) : seller.mAddress != null)
            return false;
        if (mPhoneNumber != null ? !mPhoneNumber.equals(seller.mPhoneNumber) : seller.mPhoneNumber != null)
            return false;
        return !(mEmail != null ? !mEmail.equals(seller.mEmail) : seller.mEmail != null);
    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mRating != null ? mRating.hashCode() : 0);
        result = 31 * result + (mThumbnail != null ? mThumbnail.hashCode() : 0);
        result = 31 * result + (mAddress != null ? mAddress.hashCode() : 0);
        result = 31 * result + (mPhoneNumber != null ? mPhoneNumber.hashCode() : 0);
        result = 31 * result + (mEmail != null ? mEmail.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}