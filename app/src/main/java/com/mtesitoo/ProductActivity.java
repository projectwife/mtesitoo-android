package com.mtesitoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
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

        IProductRequest productService = new ProductRequest(this);

        productService.getProduct(mProduct.getId(), new ICallback<Product>() {
            @Override
            public void onResult(Product result) {
                mProduct = result;
                ProductDetailFragment f = ProductDetailFragment.newInstance(ProductActivity.this, mProduct);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ProductActivity.this, "Error getting product images", Toast.LENGTH_LONG).show();
            }
        });
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