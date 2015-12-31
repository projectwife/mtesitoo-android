package com.mtesitoo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nan on 12/28/2015.
 */
public class ProductDetailEditActivity extends ActionBarActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final int DATE_DIALOG_ID = 999;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String IMAGE_SUFFIX = ".jpg";
    private Product mProduct;
    private File mImage;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.product_image_slider_edit)
    SliderLayout mImageSlider;
    @Bind(R.id.product_detail_info_border)
    RelativeLayout mInfoBorder;
    @Bind(R.id.product_detail_price_border)
    RelativeLayout mPriceBorder;
    @Bind(R.id.product_detail_date_border)
    RelativeLayout mDateBorder;
    @Bind(R.id.product_detail_name_edit)
    EditText mProductName;
    @Bind(R.id.product_detail_description_edit)
    EditText mProductDescription;
    @Bind(R.id.product_detail_location_edit)
    EditText mProductLocation;
    @Bind(R.id.product_detail_category_container)
    LinearLayout mProductCategoryContainer;

    @Bind(R.id.product_detail_unit_edit)
    EditText mProductUnit;
    @Bind(R.id.product_detail_quantity_edit)
    EditText mProductQuantity;
    @Bind(R.id.product_detail_price_edit)
    EditText mProductPrice;

    @Bind(R.id.product_detail_expiration_date_edit)
    EditText mProductExpirationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_edit);
        ButterKnife.bind(this);
        mProduct = getIntent().getExtras().getParcelable(getString(R.string.bundle_product_key));

        setSupportActionBar(toolbar);
        buildProductCategories();
        buildProductDatePicker();

        mProductName.setText(mProduct.getName());
        mProductDescription.setText(mProduct.getDescription());
        mProductLocation.setText(mProduct.getLocation());
        mProductUnit.setText(mProduct.getSIUnit());
        mProductQuantity.setText(mProduct.getQuantity().toString());
        mProductPrice.setText(mProduct.getPricePerUnit());
        mProductExpirationDate.setText(mProduct.getExpiration().toString());

        updateEditTextLengths();
        updateBorderPaddings();
        updateImageSlider();
    }

    @Override
    protected void onStop() {
        mImageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanCachedImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_detail_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel_edit_product) {
            finish();
            return true;
        }

        if (id == R.id.action_done_edit_product) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, 2015, 11, 1);
        }
        return null;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                buildImageFile();
            } catch (IOException ex) {
                return;
            }

            if (mImage != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImage));
                setResult(RESULT_OK, intent);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mImageSlider.getCurrentSlider();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
            updateImageSlider2();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            mProductExpirationDate.setText(new StringBuilder().append(selectedMonth + 1)
                            .append("-").append(selectedDay).append("-").append(selectedYear)
            );
        }
    };

    public void buildProductCategories() {
        ICategoryCache cache = new CategoryCache(this);
        List<Category> categories = cache.getCategories();
        RadioGroup categoryButtonGroup = new RadioGroup(this);

        for (int i = 0; i < categories.size(); i++) {
            String name = categories.get(i).getName();
            RadioButton categoryButton = new RadioButton(this);

            if (mProduct.getCategory() != null && mProduct.getCategory().compareTo(name) == 0) {
                categoryButton.setSelected(true);
            }

            categoryButton.setText(name);
            categoryButtonGroup.addView(categoryButton);
        }

        mProductCategoryContainer.addView(categoryButtonGroup);
    }

    public void buildProductDatePicker() {
        mProductExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    showDialog(DATE_DIALOG_ID);
                }
            }
        });
    }

    public void buildImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + IMAGE_SUFFIX;
        FileOutputStream fos = openFileOutput(imageFileName, Context.MODE_WORLD_WRITEABLE);
        fos.close();
        mImage = new File(getFilesDir(), imageFileName);
    }

    public void cleanCachedImages() {
        File[] files = getCacheDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().matches(".*?" + IMAGE_SUFFIX)) {
                    file.delete();
                }
            }
        }
    }

    public void updateEditTextLengths() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = mProductName.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductDescription.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductLocation.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductCategoryContainer.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductUnit.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductQuantity.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductPrice.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);

        params = mProductExpirationDate.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);
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

    public void updateImageSlider2() {
        mImageSlider.removeAllSliders();
        DefaultSliderView sliderView = new DefaultSliderView(this);
        sliderView
                .image(mImage)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(this);
        mImageSlider.addSlider(sliderView);

        mImageSlider.setDuration(8000);
        mImageSlider.addOnPageChangeListener(this);
    }
}