package com.mtesitoo.backend.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model object for a Mtesitoo farmer/seller.
 */
public class Seller implements Parcelable {
    public static final Seller DUMMY = new Seller(1, "User Name", "First Name", "Last Name",
            "Phone Number", "Email", "Company", "Street", "City", "Postcode", "Country", "Uri");

    private final Integer mId;
    private final String mUsername;
    private final String mFirstName;
    private final String mLastName;
    private final String mPhoneNumber;
    private final String mEmail;
    private final String mCompany;
    private final String mStreet;
    private final String mCity;
    private final String mPostcode;
    private final String mCountry;
    private final Uri mThumbnail;

    private Seller(Parcel in) {
        this.mId = in.readInt();
        this.mUsername = in.readString();
        this.mFirstName = in.readString();
        this.mLastName = in.readString();
        this.mPhoneNumber = in.readString();
        this.mEmail = in.readString();
        this.mCompany = in.readString();
        this.mStreet = in.readString();
        this.mCity = in.readString();
        this.mPostcode = in.readString();
        this.mCountry = in.readString();
        this.mThumbnail = Uri.parse(in.readString());
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


    public Seller(Integer id, String username, String firstName, String lastName,
                  String phoneNumber, String email, String company, String street, String city,
                  String postCode, String country, String uri) {
        mId = id;
        mUsername = username;
        mFirstName = firstName;
        mLastName = lastName;
        mPhoneNumber = phoneNumber;
        mEmail = email;
        mCompany = company;
        mStreet = street;
        mCity = city;
        mPostcode = postCode;
        mCountry = country;
        mThumbnail = Uri.parse(uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seller seller = (Seller) o;

        if (mId != seller.mId) return false;
        if (mUsername != null ? !mUsername.equals(seller.mUsername) : seller.mUsername != null)
            return false;
        if (mFirstName != null ? !mFirstName.equals(seller.mFirstName) : seller.mFirstName != null)
            return false;
        if (mLastName != null ? !mLastName.equals(seller.mLastName) : seller.mLastName != null)
            return false;
        if (mPhoneNumber != null ? !mPhoneNumber.equals(seller.mPhoneNumber) : seller.mPhoneNumber != null)
            return false;
        if (mEmail != null ? !mEmail.equals(seller.mEmail) : seller.mEmail != null)
            return false;
        if (mCompany != null ? !mCompany.equals(seller.mCompany) : seller.mCompany != null)
            return false;
        if (mStreet != null ? !mStreet.equals(seller.mStreet) : seller.mStreet != null)
            return false;
        if (mCity != null ? !mCity.equals(seller.mCity) : seller.mCity != null)
            return false;
        if (mPostcode != null ? !mPostcode.equals(seller.mPostcode) : seller.mPostcode != null)
            return false;
        if (mCountry != null ? !mCountry.equals(seller.mCountry) : seller.mCountry != null)
            return false;
        return !(mThumbnail != null ? !mThumbnail.toString().equals(seller.mThumbnail.toString()) : seller.mThumbnail != null);
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mUsername != null ? mUsername.hashCode() : 0);
        result = 31 * result + (mFirstName != null ? mFirstName.hashCode() : 0);
        result = 31 * result + (mLastName != null ? mLastName.hashCode() : 0);
        result = 31 * result + (mPhoneNumber != null ? mPhoneNumber.hashCode() : 0);
        result = 31 * result + (mEmail != null ? mEmail.hashCode() : 0);
        result = 31 * result + (mCompany != null ? mCompany.hashCode() : 0);
        result = 31 * result + (mStreet != null ? mStreet.hashCode() : 0);
        result = 31 * result + (mCity != null ? mCity.hashCode() : 0);
        result = 31 * result + (mPostcode != null ? mPostcode.hashCode() : 0);
        result = 31 * result + (mCountry != null ? mCountry.hashCode() : 0);
        result = 31 * result + (mThumbnail != null ? mThumbnail.hashCode() : 0);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mUsername);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mPhoneNumber);
        dest.writeString(mEmail);
        dest.writeString(mCompany);
        dest.writeString(mStreet);
        dest.writeString(mCity);
        dest.writeString(mPostcode);
        dest.writeString(mCountry);
        dest.writeString(mThumbnail.toString());
    }

    public int getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }
}