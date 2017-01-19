package com.mtesitoo.backend.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model object for a Mtesitoo farmer/seller.
 */
public class Seller implements Parcelable {
    public static final Seller DUMMY = new Seller(1, "User Name", "First Name", "Last Name",
            "Phone Number", "Email", "Company", "Address1", "Address2", "City", "State", "Postcode", "Country", "Description", "Uri");

    private final Integer mId;
    private final String mUsername;
    private Uri mThumbnail;
    private String mZoneId;
    private String mAgree;
    //Samuel added
    private  String mPassword;

    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;
    private String mEmail;
    private String mBusiness;
    private String mAddress1;
    private String mAddress2;
    private String mCity;
    private String mState;
    private String mPostcode;
    private String mDescription;
    private String mCountry;

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setmBusiness(String mBusiness) {
        this.mBusiness = mBusiness;
    }

    public void setmAddress1(String mAddress1) {
        this.mAddress1 = mAddress1;
    }

    public void setmAddress2(String mAddress2) {
        this.mAddress2 = mAddress2;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public void setmPostcode(String mPostcode) {
        this.mPostcode = mPostcode;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmUsername() {
        return mUsername;
    }

    public Integer getmId() {
        return mId;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmBusiness() {
        return mBusiness;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmState() {
        return mState;
    }

    public String getmAddress1() {
        return mAddress1;
    }

    public String getmAddress2() {
        return mAddress2;
    }

    public String getmPostcode() {
        return mPostcode;
    }

    public String getmCountry() {
        return mCountry;
    }

    public String getmDescription() {
        return mDescription;
    }

    public Uri getmThumbnail() {
        return mThumbnail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public String getmZoneId() {
        return mZoneId;
    }

    public String getmAgree() {
        return mAgree;
    }

    public String getmCoutry() {
        return mCountry;
    }

    private Seller(Parcel in) {
        this.mId = in.readInt();
        this.mUsername = in.readString();
        this.mFirstName = in.readString();
        this.mLastName = in.readString();
        this.mPhoneNumber = in.readString();
        this.mEmail = in.readString();
        this.mBusiness = in.readString();
        this.mAddress1 = in.readString();
        this.mAddress2 = in.readString();
        this.mCity = in.readString();
        this.mState = in.readString();
        this.mPostcode = in.readString();
        this.mCountry = in.readString();
        this.mDescription = in.readString();
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
                  String phoneNumber, String email, String company, String address1, String address2, String city, String state,
                  String postCode, String country, String description, String uri) {
        mId = id;
        mUsername = username;
        mFirstName = firstName;
        mLastName = lastName;
        mPhoneNumber = phoneNumber;
        mEmail = email;
        mBusiness = company;
        mAddress1 = address1;
        mAddress2 = address2;
        mCity = city;
        mState = state;
        mPostcode = postCode;
        mCountry = country;
        mDescription = description;
        mThumbnail = Uri.parse(uri);

        System.out.println("mId " + mId);
        System.out.println("mUsername " + mUsername);
        System.out.println("mFirstName " + mFirstName);
        System.out.println("mLastName "  + mLastName);
        System.out.println("mPhoneNumber " + mPhoneNumber);
        System.out.println("mEmail " + mEmail);
        System.out.println("mBusiness " + mBusiness);
        System.out.println("mAddress1 " + mAddress1);
        System.out.println("mAddress2 " + mAddress2);
        System.out.println("mCity " + mCity);
        System.out.println("mState " + mState);
        System.out.println("mPostcode " + mPostcode);
        System.out.println("mCountry " + mCountry);
        System.out.println("mDescription " + mDescription);
        System.out.println("mThumbnail " + uri);
    }
    //Seller for registration
    public Seller(Integer id, String username, String firstName, String lastName,
                  String phoneNumber, String email, String company, String address1, String address2, String city, String state,
                  String postCode, String uri,String password, String zoneId,String  agree, String country, String description) {
        mId = id;
        mUsername = username.trim();
        mFirstName = firstName.trim();
        mLastName = lastName.trim();
        mPhoneNumber = phoneNumber.trim();
        mEmail = email.trim();
        mBusiness = company.trim();
        mAddress1 = address1.trim();
        mAddress2 = address2.trim();
        mCity = city.trim();
        mState = state.trim();
        mPostcode = postCode.trim();
        mThumbnail = Uri.parse(uri);

        mPassword=password;
        mZoneId=zoneId;
        mAgree=agree;
        mCountry=country;
        mDescription = description;

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
        if (mBusiness != null ? !mBusiness.equals(seller.mBusiness) : seller.mBusiness != null)
            return false;
        if (mAddress1 != null ? !mAddress1.equals(seller.mAddress1) : seller.mAddress1 != null)
            return false;
        if (mAddress2 != null ? !mAddress2.equals(seller.mAddress2) : seller.mAddress2 != null)
            return false;
        if (mCity != null ? !mCity.equals(seller.mCity) : seller.mCity != null)
            return false;
        if (mState != null ? !mState.equals(seller.mCity) : seller.mState != null)
            return false;
        if (mPostcode != null ? !mPostcode.equals(seller.mPostcode) : seller.mPostcode != null)
            return false;
        if (mCountry != null ? !mCountry.equals(seller.mCountry) : seller.mCountry != null)
            return false;
        if (mDescription != null ? !mDescription.equals(seller.mDescription) : seller.mDescription != null)
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
        result = 31 * result + (mBusiness != null ? mBusiness.hashCode() : 0);
        result = 31 * result + (mAddress1 != null ? mAddress1.hashCode() : 0);
        result = 31 * result + (mAddress2 != null ? mAddress2.hashCode() : 0);
        result = 31 * result + (mCity != null ? mCity.hashCode() : 0);
        result = 31 * result + (mState != null ? mState.hashCode() : 0);
        result = 31 * result + (mPostcode != null ? mPostcode.hashCode() : 0);
        result = 31 * result + (mCountry != null ? mCountry.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
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
        dest.writeString(mBusiness);
        dest.writeString(mAddress1);
        dest.writeString(mAddress2);
        dest.writeString(mCity);
        dest.writeString(mState);
        dest.writeString(mPostcode);
        dest.writeString(mCountry);
        dest.writeString(mDescription);
        dest.writeString(mThumbnail.toString());
    }

    public int getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "mId=" + mId +
                ", mUsername='" + mUsername + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mBusiness='" + mBusiness + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mPostcode='" + mPostcode + '\'' +
                ", mThumbnail=" + mThumbnail +
                ", mPassword='" + mPassword + '\'' +
                ", mZoneId='" + mZoneId + '\'' +
                ", mAgree='" + mAgree + '\'' +
                ", mCountry='" + mCountry + '\'' +
                '}';
    }
}