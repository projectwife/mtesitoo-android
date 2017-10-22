package com.mtesitoo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.UnitCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.cache.logic.IUnitCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.helper.AddProductHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eduardodiaz on 18/10/2017.
 */

public class AddProductQuantityFragment extends Fragment {

    @BindView(R.id.etProductPrice)
    @NonNull
    EditText productPriceEditText;

    @BindView(R.id.etProductQuantity)
    @NonNull
    EditText productQuantityEditText;

    @BindView(R.id.spinnerProductCategory)
    @NonNull
    Spinner productCategorySpinner;

    @BindView(R.id.spinnerProductUnits)
    @NonNull
    Spinner productUnitsSpinner;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.toString().hashCode() == productPriceEditText.getText().toString().hashCode()) {
                AddProductHelper.getInstance().setProductPricePerUnit(charSequence.toString());
                return;
            }
            AddProductHelper.getInstance().setProductQuantity(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public AddProductQuantityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_quantity, container, false);

        ButterKnife.bind(this, rootView);

        populateScreen();

        return rootView;
    }

    private void populateScreen() {

        ICategoryCache cache = new CategoryCache(getContext());
        final List<Category> categories = cache.getCategories();
        List<String> categoryNames = new ArrayList<>(categories.size());

        for (int i = 0; i < categories.size(); i++) {
            categoryNames.add(categories.get(i).getName());
        }
        productCategorySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames));

        IUnitCache unitCache = new UnitCache(getContext());
        final List<Unit> units = unitCache.getWeightUnits();
        List<String> unitNames = new ArrayList<>(units.size());
        for (int i = 0; i < units.size(); i++) {
            unitNames.add(units.get(i).getName());
        }
        productUnitsSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, unitNames));

        Map<String, String> h = AddProductHelper.getInstance().getProductDetailsData();

        productPriceEditText.setText(h.get(Constants.PRODUCT_PRICE_KEY));
        productQuantityEditText.setText(h.get(Constants.PRODUCT_QUANTITY_KEY));

        productPriceEditText.addTextChangedListener(textWatcher);
        productQuantityEditText.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.button_price_minus, R.id.button_price_plus})
    void handlePrice(Button button) {
        if (productPriceEditText.getText().toString().isEmpty()) {
            productPriceEditText.setText("0");
            return;
        }

        int currentValue = Integer.parseInt(productPriceEditText.getText().toString());
        if (button.getId() == R.id.button_price_minus) {
            if (currentValue > 0) productPriceEditText.setText(String.valueOf(currentValue - 1));
        } else {
            productPriceEditText.setText(String.valueOf(currentValue + 1));
        }
    }

    @OnClick({R.id.button_quantity_minus, R.id.button_quantity_plus})
    void handleQuantity(Button button) {
        if (productQuantityEditText.getText().toString().isEmpty()) {
            productQuantityEditText.setText("0");
            return;
        }

        int currentValue = Integer.parseInt(productQuantityEditText.getText().toString());
        if (button.getId() == R.id.button_quantity_minus) {
            if (currentValue > 0) productQuantityEditText.setText(String.valueOf(currentValue - 1));
        } else {
            productQuantityEditText.setText(String.valueOf(currentValue + 1));
        }
    }
}
