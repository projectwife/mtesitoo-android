package com.mtesitoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.fragment.ContactFragment;
import com.mtesitoo.fragment.HelpFragment;
import com.mtesitoo.fragment.InfoFragment;
import com.mtesitoo.fragment.OrderFragment;
import com.mtesitoo.fragment.ProductFragment;
import com.mtesitoo.fragment.ProfileFragment;
import com.mtesitoo.helper.UriAdapter;
import com.squareup.picasso.Picasso;

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

        ProductFragment f = ProductFragment.newInstance(this, mSeller.getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();

        if (resetPassword) {
            String token = getIntent().getExtras().getString(getString(R.string.automatic_login_token));
            ProfileFragment profileFragment = ProfileFragment.newInstance(resetPassword, token);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();

        }
    }

    public void buildNavigationDrawer() {
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable drawable) {
                Picasso.with(imageView.getContext()).load(uri)
                        .placeholder(getDefaultDrawable())
                        .error(getDefaultDrawable())
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {

            }

            @Override
            public Drawable placeholder(Context context) {
                return getDefaultDrawable();
            }
        });

        final IProfile profile = new ProfileDrawerItem()
                .withIdentifier(mSeller.getId())
                .withName(mSeller.getUsername())
                .withEmail(getString(R.string.role_seller));

        if (!mSeller.getmThumbnail().getPath().equals("null")) {
            profile.withIcon(mSeller.getmThumbnail());
        } else {
            profile.withIcon(getDefaultDrawable());
        }


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
                                f = ProductFragment.newInstance(mContext, mSeller.getId());
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_profile_index))) {
                                f = ProfileFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_order_index))) {
                                f = OrderFragment.newInstance(mContext, mSeller.getId());
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_add_product_index))) {
                                Intent intent = new Intent(mContext, AddProductActivity.class);
                                mContext.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_info_index))) {
                                f = InfoFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_help_index))) {
                                f = HelpFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_contact_index))) {
                                f = ContactFragment.newInstance();
                            } else if (drawerItem.getIdentifier() == Integer.parseInt(mContext.getString(R.string.menu_item_logout_index))) {

                                showLogoutConfirmationDialog();
                            }

                            if (f != null) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).addToBackStack(null).commit();
                            }
                        }

                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        checkProfilePicture(profile);
                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

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

    private void checkProfilePicture(IProfile profile) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriAdapter())
                .create();

        Seller seller = gson.fromJson(sharedPreferences.getString(Constants.LOGGED_IN_USER_DATA, ""), Seller.class);

        if (profile.getIcon().getUri() != null && !profile.getIcon().getUri().equals(seller.getmThumbnail())
                || profile.getIcon().getIcon() != null && !seller.getmThumbnail().getPath().equals("null")) {
            if (!seller.getmThumbnail().getPath().equals("null")) {
                profile.withIcon(seller.getmThumbnail());
            } else {
                profile.withIcon(getDefaultDrawable());
            }
            headerResult.updateProfileByIdentifier(profile);
        }
    }

    private Drawable getDefaultDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getDrawable(R.drawable.ic_account_circle_black_24dp);
        }
        //noinspection deprecation
        return getResources().getDrawable(R.drawable.ic_account_circle_black_24dp);
    }

    private void showLogoutConfirmationDialog() {
        final AlertDialog.Builder logoutConfirmationDialog = new AlertDialog.Builder(this);
        logoutConfirmationDialog.setTitle(R.string.logout_action_title)
                .setMessage(R.string.logout_action_message)
                .setPositiveButton(R.string.logout_action_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.logout_action_cancel, null);
        logoutConfirmationDialog.show();
        result.setSelection(-1);
    }

    private void logout() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        boolean isUserLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGGED_IN_KEY, false);

        if (isUserLoggedIn) {

            //set logged_in to false and show LoginActivity
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

        }
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }
}