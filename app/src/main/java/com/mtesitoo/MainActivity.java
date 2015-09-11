package com.mtesitoo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mtesitoo.adapter.ProductListAdapter;
import com.mtesitoo.backend.service.ProductService;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private ProductListAdapter mProductListAdapter;

    @Bind(R.id.product_list)
    ListView mProductList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        buildNavigationDrawer();
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
        final IProfile profile = new ProfileDrawerItem()
                .withName(getString(R.string.test_user))
                .withEmail(getString(R.string.role_seller));

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withProfileImagesClickable(false)
                .withSelectionListEnabled(false)
                .withCompactStyle(true)
                .addProfiles(profile)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)

                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_home).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header_account),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_profile).withIcon(GoogleMaterial.Icon.gmd_face).withIdentifier(3).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_orders).withIcon(GoogleMaterial.Icon.gmd_history).withIdentifier(4).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_new).withIcon(GoogleMaterial.Icon.gmd_add).withIdentifier(5),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header_app),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_info).withIcon(GoogleMaterial.Icon.gmd_info_outline).withIdentifier(9).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(GoogleMaterial.Icon.gmd_help_outline).withIdentifier(10).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_exit).withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(11)
                )
                .withShowDrawerOnFirstLaunch(true)
                .build();

        result.updateBadge(4, new StringHolder(10 + ""));
    }

    public void updateProductList() {
        ProductService productService = new ProductService(this);

        productService.getProducts(new IResponse<List<Product>>() {
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