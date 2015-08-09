package com.mtesitoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mtesitoo.adapter.ProductListAdapter;
import com.mtesitoo.backend.api.ApiLoginService;
import com.mtesitoo.backend.api.ApiProductService;
import com.mtesitoo.backend.api.Callback;
import com.mtesitoo.backend.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity {

    private ProductListAdapter mProductListAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.product_list)
    ListView mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ApiLoginService apiLoginService = new ApiLoginService(this);
        ApiProductService apiProductService = new ApiProductService(this);

        apiLoginService.getAuthToken(new Callback<String>() {
            @Override
            public void onResult(String result) {
                Log.d(TAG, "Got auth token: " + result);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error getting token", e);
            }
        });

        apiProductService.getSpecials(new Callback<List<Product>>() {
            @Override
            public void onResult(List<Product> result) {
                Log.d(TAG, "Products are: " + result);
                mProductListAdapter.refresh((ArrayList<Product>) result);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error retrieving products: " + e);
            }
        });

        mProductListAdapter = new ProductListAdapter(this, new ArrayList<Product>());
        mProductList.setAdapter(mProductListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_item) {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}