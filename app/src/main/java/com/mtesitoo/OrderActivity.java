package com.mtesitoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mtesitoo.backend.model.Order;
import com.mtesitoo.fragment.OrderDetailFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {


    private Order mOrder;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mOrder= getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));
        OrderDetailFragment f= OrderDetailFragment.newInstance(this,mOrder);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
        /* ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mProduct = getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));
        ProductDetailFragment f = ProductDetailFragment.newInstance(this, mProduct);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
