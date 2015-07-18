package com.mtesitoo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mtesitoo.model.Item;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemDetailActivity extends ActionBarActivity {

    private Item mItem;

    @Bind(R.id.item_name)
    TextView mItemName;
    @Bind(R.id.item_price)
    TextView mItemPrice;
    @Bind(R.id.item_transportation)
    TextView mItemTransportation;
    @Bind(R.id.item_expiration)
    TextView mItemExpiration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        mItem = getIntent().getExtras().getParcelable(getString(R.string.bundle_item_key));
        mItemName.setText(mItem.getName());
        mItemPrice.setText(getString(R.string.currency_symbol) + mItem.getPrice().toString());
        mItemTransportation.setText(mItem.getTransportation());
        mItemExpiration.setText(mItem.getExpiration().toString());
    }
}
