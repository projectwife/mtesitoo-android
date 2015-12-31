package com.mtesitoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mtesitoo.backend.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductDetailActivity extends ActionBarActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final double IMAGE_SLIDER_HEIGHT_FACTOR = 0.45;
    private Product mProduct;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.product_image_slider)
    SliderLayout mImageSlider;
    @Bind(R.id.product_detail_info_border)
    RelativeLayout mInfoBorder;
    @Bind(R.id.product_detail_price_border)
    RelativeLayout mPriceBorder;
    @Bind(R.id.product_detail_date_border)
    RelativeLayout mDateBorder;

    @Bind(R.id.product_detail_name)
    TextView mProductName;
    @Bind(R.id.product_detail_description)
    TextView mProductDescription;
    @Bind(R.id.product_detail_location)
    TextView mProductLocation;
    @Bind(R.id.product_detail_category)
    TextView mProductCategory;

    @Bind(R.id.product_detail_unit)
    TextView mProductUnit;
    @Bind(R.id.product_detail_quantity)
    TextView mProductQuantity;
    @Bind(R.id.product_detail_price)
    TextView mProductPrice;

    @Bind(R.id.product_detail_posting_date)
    TextView mProductPostingDate;
    @Bind(R.id.product_detail_expiration_date)
    TextView mProductExpirationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mProduct = getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));
        mProductName.setText(mProduct.getName());
        mProductDescription.setText(mProduct.getDescription());
        mProductLocation.setText(mProduct.getLocation());
        mProductCategory.setText(mProduct.getCategory());
        mProductUnit.setText(mProduct.getSIUnit());
        mProductQuantity.setText(mProduct.getQuantity().toString());
        mProductPrice.setText(mProduct.getPricePerUnit());
        mProductExpirationDate.setText(mProduct.getExpiration().toString());

        updateBorderPaddings();
        updateImageSlider();
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
            Intent intent = new Intent(this, ProductDetailEditActivity.class);
            intent.putExtra(getString(R.string.bundle_product_key), mProduct);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        mImageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void updateBorderPaddings() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = mImageSlider.getLayoutParams();
        params.height = (int) (metrics.widthPixels * 0.65);

        int padding = Integer.parseInt(getString(R.string.padding));
        mInfoBorder.setPadding(padding, padding, padding, padding / 2);
        mPriceBorder.setPadding(padding, padding / 2, padding, padding / 2);
        mDateBorder.setPadding(padding, padding / 2, padding, padding);
    }

    public void updateImageSlider() {
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        urls.add("http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        urls.add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
        urls.add("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for (String url : urls) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView
                    .image(url)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            mImageSlider.addSlider(sliderView);
        }

        mImageSlider.setDuration(8000);
        mImageSlider.addOnPageChangeListener(this);
    }
}