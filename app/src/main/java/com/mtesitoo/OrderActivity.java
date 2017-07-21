package com.mtesitoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mtesitoo.backend.model.Order;
import com.mtesitoo.fragment.OrderDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Order mOrder= getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));
        OrderDetailFragment f= OrderDetailFragment.newInstance(this,mOrder);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
    }
}
