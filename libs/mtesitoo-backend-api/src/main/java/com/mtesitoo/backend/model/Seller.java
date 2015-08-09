package com.mtesitoo.backend.model;

import android.net.Uri;

import java.math.BigDecimal;

/**
 * Model object for a Mtesitoo farmer/seeller. TODO(danieldanciu): for now I just added some random
 * fields, it's TBD what actually goes into this class.
 */
public class Seller {
    public static final Seller DUMMY = new Seller("John Doe", new BigDecimal("5"), Uri.parse(""),
            "Gambiastreet 11", "+43 344 334 333", "john@doe.com");
    private final String name;
    private final BigDecimal rating;
    private final Uri thumbnail;
    private final String address;
    private final String phoneNumber;
    private final String email;

    public Seller(String name, BigDecimal rating, Uri thumbnail, String address, String phoneNumber,
                  String email) {
        this.name = name;
        this.rating = rating;
        this.thumbnail = thumbnail;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seller seller = (Seller) o;

        if (name != null ? !name.equals(seller.name) : seller.name != null) return false;
        if (rating != null ? !rating.equals(seller.rating) : seller.rating != null) return false;
        if (thumbnail != null ? !thumbnail.equals(seller.thumbnail) : seller.thumbnail != null)
            return false;
        if (address != null ? !address.equals(seller.address) : seller.address != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(seller.phoneNumber) : seller.phoneNumber != null)
            return false;
        return !(email != null ? !email.equals(seller.email) : seller.email != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
