package com.mtesitoo.backend.model;

/**
 * Created by Nan on 9/19/2015.
 */
public class Category {
    private final Integer mId;
    private final String mName;

    public Category(Integer id, String name) {
        mId = id;
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (mId != category.mId) return false;
        return !(mName != null ? !mName.toString().equals(category.mName) : category.mName != null);
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);

        return result;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}