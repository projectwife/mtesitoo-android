package com.mtesitoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.fragment.ContactFragment;
import com.mtesitoo.fragment.HelpFragment;
import com.mtesitoo.fragment.InfoFragment;
import com.mtesitoo.fragment.OrderFragment;
import com.mtesitoo.fragment.ProductFragment;
import com.mtesitoo.fragment.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    private Context mContext;
    private Seller mSeller;
    private boolean resetPassword = false;

    private AccountHeader headerResult = null;
    private Drawer result = null;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mContext = this;

        mSeller = getIntent().getExtras().getParcelable(getString(R.string.bundle_seller_key));
        resetPassword = getIntent().getBooleanExtra(getString(R.string.automatic_login_key), false);

        setSupportActionBar(toolbar);
        buildNavigationDrawer();

        ProductFragment f = ProductFragment.newInstance(this, mSeller);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();

        if (resetPassword) {
            String token = getIntent().getExtras().getString(getString(R.string.automatic_login_token));
            ProfileFragment profileFragment = ProfileFragment.newInstance(mContext, mSeller, resetPassword, token);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();

            return;
        }
    }

    public void buildNavigationDrawer() {
        final IProfile profile = new ProfileDrawerItem()
                .withName(mSeller.getUsername())
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
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_seller)
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_product_listing)
                                .withIcon(GoogleMaterial.Icon.gmd_list)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_list_products)))
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_new)
                                .withIcon(GoogleMaterial.Icon.gmd_add)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_add_product_index)))
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_orders)
                                .withIcon(GoogleMaterial.Icon.gmd_history)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_order_index)))
                                .withSelectable(false),
                        new SectionDrawerItem()
                                .withName(R.string.drawer_item_section_header_account),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_profile)
                                .withIcon(GoogleMaterial.Icon.gmd_face)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_profile_index)))
                                .withSelectable(false),

                                // Todo orders menu - displays number of pending orders.
                                // Uncomment the lines below to display the number of orders next to the menu item
                                /*.withBadgeStyle(new BadgeStyle()
                                        .withTextColor(Color.WHITE)
                                        .withColorRes(R.color.md_red_700)),*/

                        new SectionDrawerItem()
                                .withName(R.string.drawer_item_section_header_app),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_info)
                                .withIcon(GoogleMaterial.Icon.gmd_info_outline)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_info_index)))
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_help)
                                .withIcon(GoogleMaterial.Icon.gmd_help_outline)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_help_index)))
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_contact)
                                .withIcon(GoogleMaterial.Icon.gmd_contact_phone)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_contact_index)))
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_logout)
                                .withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                                .withIdentifier(Integer.parseInt(mContext.getString(R.string.menu_item_logout_index)))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Fragment f = null;

                            if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_list_products))) {
                                f = ProductFragment.newInstance(mContext, mSeller);
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_profile_index))) {
                                f = ProfileFragment.newInstance(mContext, mSeller);
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_order_index))) {
                                f = OrderFragment.newInstance(mContext, mSeller);
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_add_product_index))) {
                                Intent intent = new Intent(mContext, AddProductActivity2.class);
                                mContext.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_info_index))) {
                                f = InfoFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_help_index))) {
                                f = HelpFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_contact_index))) {
                                f = ContactFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_logout_index))) {
                                //set logged_in to false and show LoginActivity
                                SharedPreferences mPrefs = mContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mPrefs.edit();

                                boolean isUserLoggedIn = mPrefs.getBoolean(Constants.IS_USER_LOGGED_IN_KEY, false);

                                if (isUserLoggedIn) {
                                    editor.putBoolean(Constants.IS_USER_LOGGED_IN_KEY, false);
                                    editor.putString(Constants.LOGGED_IN_USER_ID_KEY, "");
                                    editor.putString(Constants.LOGGED_IN_USER_PASS_KEY, "");
                                    editor.commit();

                                }
                                startActivity(new Intent(mContext, LoginActivity.class));
                                //finish();
                            }

                            if (f != null) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).addToBackStack(null).commit();
                            }
                        }

                        return false;
                    }
                })
                .withShowDrawerOnFirstLaunch(!resetPassword)
                .withSelectedItem(-1)
                .build();

        // Todo orders menu - displays number of pending orders.
        // functionality not yet implemented.
        // Badge has been commented out above.
        // result.updateBadge(3, new StringHolder(10 + ""));
    }
}