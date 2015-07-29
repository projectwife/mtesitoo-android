package com.mtesitoo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mtesitoo.model.Product;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductDetailActivity extends ActionBarActivity {

    @Bind(R.id.product_name)
    TextView mProductName;
    @Bind(R.id.product_location)
    TextView mProductLocation;
    @Bind(R.id.product_price)
    TextView mProductPricePerUnit;
    @Bind(R.id.product_unit)
    TextView mProductSIUnit;
    @Bind(R.id.product_quantity)
    TextView mProductQuantity;
    @Bind(R.id.product_expiration)
    TextView mProductExpiration;
    @Bind(R.id.product_description)
    TextView mProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        Product product = getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));

        mProductName.setText(product.getName());
        mProductLocation.setText(product.getLocation());
        mProductPricePerUnit.setText(product.getPricePerUnit());
        mProductSIUnit.setText(product.getSIUnit());
        mProductQuantity.setText(product.getQuantity().toString());
        mProductExpiration.setText(product.getExpiration().toString());
        mProductDescription.setText(product.getDescription());
    }
}
