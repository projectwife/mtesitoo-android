package com.mtesitoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.R;
import com.mtesitoo.adapter.AddProductPicturesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eduardodiaz on 18/10/2017.
 */

public class AddProductPictureFragment extends Fragment {

    @BindView(R.id.pictures_recyclerview)
    RecyclerView picturesRecyclerView;

    public AddProductPictureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_pictures, container, false);

        ButterKnife.bind(this, rootView);

        populateScreen();

        return rootView;
    }

    private void populateScreen() {
        picturesRecyclerView.setAdapter(new AddProductPicturesAdapter());
        picturesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
}
