package com.mtesitoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.R;

import butterknife.ButterKnife;

/**
 * Created by eduardodiaz on 18/10/2017.
 */

public class AddProductPictureFragment extends Fragment {

    public AddProductPictureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_pictures, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
