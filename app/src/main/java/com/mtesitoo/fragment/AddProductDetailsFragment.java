package com.mtesitoo.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.helper.AddProductHelper;
import com.mtesitoo.helper.DateHelper;

import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddProductDetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etProductName)
    @NonNull
    EditText productNameEditText;

    @BindView(R.id.etProductDescription)
    @NonNull
    EditText productDescriptionEditText;

    @BindView(R.id.etProductLocation)
    @NonNull
    EditText productLocationEditText;

    @BindView(R.id.tvProductExpiration)
    @NonNull
    TextView productExpirationTextView;

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
        productExpirationTextView.setText(h.get(Constants.PRODUCT_EXPIRATION_KEY));

        productNameEditText.addTextChangedListener(textWatcher);
        productDescriptionEditText.addTextChangedListener(textWatcher);
        productLocationEditText.addTextChangedListener(textWatcher);
        productExpirationTextView.addTextChangedListener(textWatcher);
    }

    @OnClick(R.id.tvProductExpiration)
    void clickOnProductExpiration() {
        String currentExpiration = AddProductHelper.getInstance().getProductDetailsData().get(Constants.PRODUCT_EXPIRATION_KEY);
        int year, month, day;
        if (currentExpiration != null && DateHelper.parseDate(currentExpiration) != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateHelper.parseDate(currentExpiration));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog d = new DatePickerDialog(getContext(), this, year, month, day);
        d.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        d.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        AddProductHelper.getInstance().setProductExpirationDate(DateHelper.dateToString(calendar.getTime()));
        productExpirationTextView.setText(AddProductHelper.getInstance().getProductDetailsData().get(Constants.PRODUCT_EXPIRATION_KEY));
    }
}
