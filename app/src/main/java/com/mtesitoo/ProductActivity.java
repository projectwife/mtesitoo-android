package com.mtesitoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mtesitoo.backend.model.Product;
import com.mtesitoo.fragment.ProductDetailEditFragment;
import com.mtesitoo.fragment.ProductDetailFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductActivity extends AppCompatActivity {
    private Product mProduct;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mProduct = getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));
        ProductDetailFragment f = ProductDetailFragment.newInstance(this, mProduct);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_product) {
            ProductDetailEditFragment f = ProductDetailEditFragment.newInstance(this, mProduct);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}