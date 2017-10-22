package com.mtesitoo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.helper.AddProductHelper;

import java.util.Map;

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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.toString().hashCode() == productNameEditText.getText().toString().hashCode()) {
                AddProductHelper.getInstance().setProductName(charSequence.toString());
                return;
            }
            if (charSequence.toString().hashCode() == productDescriptionEditText.getText().toString().hashCode()) {
                AddProductHelper.getInstance().setProductDescription(charSequence.toString());
                return;
            }
            if (charSequence.toString().hashCode() == productLocationEditText.getText().toString().hashCode()) {
                AddProductHelper.getInstance().setProductLocation(charSequence.toString());
                return;
            }
            AddProductHelper.getInstance().setProductExpirationDate(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public AddProductDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_details, container, false);

        ButterKnife.bind(this, rootView);

        populateScreen();

        return rootView;
    }

    private void populateScreen() {
        Map<String, String> h = AddProductHelper.getInstance().getProductDetailsData();

        productNameEditText.setText(h.get(Constants.PRODUCT_NAME_KEY));
        productDescriptionEditText.setText(h.get(Constants.PRODUCT_DESCRIPTION_KEY));
        productLocationEditText.setText(h.get(Constants.PRODUCT_LOCATION_KEY));
        productExpirationEditText.setText(h.get(Constants.PRODUCT_EXPIRATION_KEY));

        productNameEditText.addTextChangedListener(textWatcher);
        productDescriptionEditText.addTextChangedListener(textWatcher);
        productLocationEditText.addTextChangedListener(textWatcher);
        productExpirationEditText.addTextChangedListener(textWatcher);
    }
}
