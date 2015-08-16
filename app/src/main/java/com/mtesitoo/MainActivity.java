package com.mtesitoo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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

        buildNavigationDrawer();

        //TODO: Build tabbed view for items being sold by seller, and orders requested for seller
        updateProductList();
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

    public void buildNavigationDrawer() {
        //TODO: The navigation drawer style needs to be updated
        //TODO: Add backend api service component for user authentication

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(Color.BLACK)
                .withProfileImagesClickable(false)
                .withSelectionListEnabled(false)
                .addProfiles(new ProfileDrawerItem()
                        .withName("Nan Wu")
                        .withEmail("Seller")
                        .withNameShown(true))
                .build();

        String[] items = getResources().getStringArray(R.array.drawer_items);
        PrimaryDrawerItem[] drawerItems = new PrimaryDrawerItem[items.length];

        for (int i = 0; i < items.length; i++) {
            drawerItems[i] = new PrimaryDrawerItem().withName(items[i]);
        }

        new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(headerResult)
                .withDrawerWidthDp(250)
                .addDrawerItems(drawerItems)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        return true;
                    }
                })
                .build();
    }

    public void updateProductList() {
        ApiLoginService apiLoginService = new ApiLoginService(this);
        ApiProductService apiProductService = new ApiProductService(this);

        apiLoginService.getAuthToken(null);
        apiProductService.getSpecials(new Callback<List<Product>>() {
            @Override
            public void onResult(List<Product> result) {
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
}