package com.mtesitoo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mtesitoo.fragment.AddProductDetailsFragment;
import com.mtesitoo.fragment.AddProductPictureFragment;
import com.mtesitoo.fragment.AddProductQuantityFragment;

/**
 * Created by eduardodiaz on 18/10/2017.
 */

public class AddProductPagerAdapter extends FragmentStatePagerAdapter {

    public AddProductPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddProductDetailsFragment();
            case 1:
                return new AddProductQuantityFragment();
            case 2:
                return new AddProductPictureFragment();
            default:
                return new AddProductDetailsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

