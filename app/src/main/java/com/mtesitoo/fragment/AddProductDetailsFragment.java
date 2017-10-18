package com.mtesitoo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mtesitoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddProductDetailsFragment extends Fragment {

    @BindView(R.id.etProductName)
    @NonNull
    EditText productNameEditText;

    @BindView(R.id.etProductDescription)
    @NonNull
    EditText productDescriptionEditText;

    @BindView(R.id.etProductLocation)
    @NonNull
    EditText productLocationEditText;

    @BindView(R.id.etProductExpiration)
    @NonNull
    EditText productExpirationEditText;

    public AddProductDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_details, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
