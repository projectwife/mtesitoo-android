package com.mtesitoo.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mtesitoo.R;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.model.ImageFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nan on 12/31/2015.
 */
public class ProductDetailEditFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener{
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MAX_IMAGES = 3;
    private static final int IMAGE_SLIDER_DURATION = 8000;

    private Product mProduct;
    private ArrayList<ImageFile> mImages;
    private ImageFile currentImage;
    private RadioGroup categoryButtonGroup;

    @Bind(R.id.product_image_slider_edit)
    SliderLayout mImageSlider;
    @Bind(R.id.product_detail_info_border)
    RelativeLayout mInfoBorder;
    @Bind(R.id.product_detail_price_border)
    RelativeLayout mPriceBorder;
//    @Bind(R.id.product_detail_date_border)
//    RelativeLayout mDateBorder;
    @Bind(R.id.product_detail_name_edit)
    EditText mProductName;
    @Bind(R.id.product_detail_description_edit)
    EditText mProductDescription;
    @Bind(R.id.product_detail_location_edit)
    EditText mProductLocation;
    @Bind(R.id.product_detail_category_container)
    LinearLayout mProductCategoryContainer;
    @Bind(R.id.product_detail_expiration_edit)
    EditText mProductExpiration;

    @Bind(R.id.product_detail_unit_edit)
    EditText mProductUnit;
    @Bind(R.id.product_detail_quantity_edit)
    EditText mProductQuantity;
    @Bind(R.id.product_detail_price_edit)
    EditText mProductPrice;

    public static ProductDetailEditFragment newInstance(Context context, Product product) {
        ProductDetailEditFragment fragment = new ProductDetailEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_product_key), product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mImages = new ArrayList<>(MAX_IMAGES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_fragment_edit, container, false);
        ButterKnife.bind(this, view);
        Bundle args = this.getArguments();
        mProduct = args.getParcelable(getString(R.string.bundle_product_key));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buildProductCategories();
        //buildProductDatePicker();
        buildImageSlider();

        mProductName.setText(mProduct.getName());
        mProductDescription.setText(mProduct.getDescription());
        mProductLocation.setText(mProduct.getLocation());
        mProductUnit.setText(mProduct.getSIUnit());
        mProductQuantity.setText(mProduct.getQuantity().toString());
        mProductPrice.setText(mProduct.getPricePerUnit());
        mProductExpiration.setText(mProduct.getExpirationFormatted());

        updateEditTextLengths();
        updateBorderPaddings();
        mProductExpiration.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        mImageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //FileHelper.clean(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_product_detail_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Date expiryDate;

        if (id == R.id.action_done_edit_product){
            int catID = categoryButtonGroup.indexOfChild(getActivity().findViewById(categoryButtonGroup.getCheckedRadioButtonId()));
            RadioButton cButton = ((RadioButton)categoryButtonGroup.getChildAt(catID));
            String category = cButton != null ? cButton.getText().toString() : "Category";
            ICategoryCache cache = new CategoryCache(this.getActivity());
            List<Category> categories = cache.getCategories();

            for (Category c : categories) {
                if (c.getName().equals(category)) {
                    category = Integer.toString(c.getId());
                    break;
                }
            }

            try {
                expiryDate = getDateFromDateField();
            } catch (ParseException e) {
                expiryDate = mProduct.getExpiration();
            }

            // Save product here
            final Product updatedProduct = new Product(
                    mProduct.getId(),
                    mProductName.getText().toString(),
                    mProductDescription.getText().toString(),
                    mProduct.getLocation(),
                    category,
                    mProductUnit.getText().toString(),
                    mProductPrice.getText().toString(),
                    Integer.parseInt(mProductQuantity.getText().toString()),
                    expiryDate,
                    mProduct.getmThumbnail(),
                    mProduct.getAuxImages()
            );

            IProductRequest productService = new ProductRequest(ProductDetailEditFragment.this.getContext());
            productService.updateProduct(updatedProduct, new ICallback<String>() {
                @Override
                public void onResult(String result) {
                    Toast.makeText(getActivity(),"Product Updated Successfully",Toast.LENGTH_SHORT).show();
                    ProductDetailFragment f = ProductDetailFragment.newInstance(getActivity(), updatedProduct);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getActivity(),"Error Updating Product",Toast.LENGTH_SHORT).show();
                    ProductDetailFragment f = ProductDetailFragment.newInstance(getActivity(), mProduct);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                }
            });

            return true;
        }else if(id == R.id.action_cancel_edit_product) {
            ProductDetailFragment f = ProductDetailFragment.newInstance(getActivity(), mProduct);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSliderClick(final BaseSliderView slider) {

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.EditImages))
                .setPositiveButton("Add an image", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            ImageFile image = null;

                            try {
                                image = new ImageFile(getActivity());
                            } catch (Exception e) {
                                Log.d("IMAGE_CAPTURE","Issue creating image file");
                            }

                            if (image != null) {
                                Uri imgUri = FileProvider.getUriForFile(getActivity(),
                                        "com.mtesitoo.fileprovider",
                                        image);
                                currentImage = image;
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }
                })
                .setNegativeButton("Delete this image", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        final String imageFilename = slider.getUrl().substring( slider.getUrl().lastIndexOf('/')+1, slider.getUrl().length() );

                        // Delete image request
                        IProductRequest productService = new ProductRequest(ProductDetailEditFragment.this.getContext());
                        productService.deleteProductImage(mProduct, imageFilename, new ICallback<Product>() {
                            @Override
                            public void onResult(Product result) {
                                mImageSlider.removeSliderAt(mImageSlider.getCurrentPosition());
                                Toast.makeText(getActivity(),"Deleted Image Successfully",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getActivity(),"Error Deleting Image",Toast.LENGTH_SHORT).show();
                            }
                        });

                            // Remove image from list if successful

                        //Toast.makeText(getActivity(),"delete " + slider.getUrl(),Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            mImages.add(0,currentImage);
            mProduct.addImage(currentImage.getUri());
            updateImageSlider();

            IProductRequest productService = new ProductRequest(this.getContext());
            productService.submitProductImage(mProduct, new ICallback<String>() {
                @Override
                public void onResult(String result) {
                    Toast.makeText(getContext(), "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("UploadImage", e.toString());
                }
            });
        }
    }

    public ImageFile getCurrentImage(){
        return currentImage;
    }

//    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
//        public void onDateSet(DatePicker view, int selectedYear,
//                              int selectedMonth, int selectedDay) {
//            mProductExpirationDate.setText(new StringBuilder().append(selectedMonth + 1)
//                            .append("-").append(selectedDay).append("-").append(selectedYear)
//            );
//        }
//    };

    private void buildProductCategories() {
        ICategoryCache cache = new CategoryCache(getActivity());
        List<Category> categories = cache.getCategories();
        categoryButtonGroup = new RadioGroup(getActivity());

        for (int i = 0; i < categories.size(); i++) {
            String name = categories.get(i).getName();
            RadioButton categoryButton = new RadioButton(getActivity());
            categoryButton.setId(i);

            categoryButton.setText(name);
            categoryButtonGroup.addView(categoryButton);

            for(String catString : mProduct.getResolvedCategories(getContext())){
                if (catString.equalsIgnoreCase(name)) {
                    categoryButtonGroup.check(i);
                    break;
                }
            }
        }

        mProductCategoryContainer.addView(categoryButtonGroup);
    }

//    private void buildProductDatePicker() {
//        mProductExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus == true) {
//                    new DatePickerDialog(getActivity(), datePickerListener, 2015, 11, 1).show();
//                }
//            }
//        });
//    }

    private void buildImageSlider() {

        ArrayList<String> urls = new ArrayList<>();

        urls.add(mProduct.getmThumbnail().toString());

        for(Uri image : mProduct.getAuxImages()){
            urls.add(image.toString());
        }

        for (String url : urls) {
            if(!url.equals("") && !url.equals(" ")){
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView
                        .image(url)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                mImageSlider.addSlider(sliderView);
            }else{
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView
                        .image("http://tesitoo.com/image/cache/no_image-100x100.png")
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                mImageSlider.addSlider(sliderView);
            }
        }

        mImageSlider.setDuration(IMAGE_SLIDER_DURATION);

        if(urls.size() <= 1){
            mImageSlider.stopAutoCycle();
            mImageSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }
    }

    private void updateEditTextLengths() {
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

        params = mProductExpiration.getLayoutParams();
        params.width = (int) (metrics.widthPixels * 0.5);
    }

    private void updateBorderPaddings() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = mImageSlider.getLayoutParams();
        params.height = (int) (metrics.widthPixels * 0.65);

        int padding = Integer.parseInt(getString(R.string.padding_20));
        mInfoBorder.setPadding(padding, padding, padding, padding / 2);
        mPriceBorder.setPadding(padding, padding / 2, padding, padding / 2);
        //mDateBorder.setPadding(padding, padding / 2, padding, padding);
    }

    private void updateImageSlider() {
        mImageSlider.removeAllSliders();

        for (ImageFile image : mImages) {

            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            sliderView
                    .image(image)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            mImageSlider.addSlider(sliderView);
        }

        mImageSlider.setDuration(IMAGE_SLIDER_DURATION);

        if(mImages.size() <= 1){
            mImageSlider.stopAutoCycle();
            mImageSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }
    }

    public void onClick(View view){

        if (view == mProductExpiration) {
            DatePickerDialogFragment datePickerDialog = new DatePickerDialogFragment();

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(getDateFromDateField());
            } catch (ParseException e) {
                // Log
            }

            Bundle args = new Bundle();
            args.putInt("year", calendar.get(Calendar.YEAR));
            args.putInt("month", calendar.get(Calendar.MONTH));
            args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setArguments(args);

            DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int month,
                                      int day) {

                    mProductExpiration.setText(String.valueOf(year) + "-" + String.valueOf(month+1)
                            + "-" + String.valueOf(day));
                }
            };

            datePickerDialog.setCallBack(onDate);
            datePickerDialog.show(getFragmentManager(), "Date Picker");
        }
    }

    private Date getDateFromDateField() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return dateFormat.parse(mProductExpiration.getText().toString());
        } catch (ParseException e) {
            throw e;
        }
    }
}