package com.mtesitoo.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.helper.FormatHelper;
import com.mtesitoo.model.ImageFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nan on 12/31/2015.
 */
public class ProductDetailEditFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener{
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOAD_IMAGE = 2;
    static int REQUEST_IMAGE_TYPE = 0;

    private static final int IMAGE_SLIDER_DURATION = 8000;
    private static final int MAX_IMAGES = Product.MAX_AUX_IMAGES;

    private Product mProduct;
    //private ArrayList<ImageFile> mImages;
    private ArrayList<Uri> mImageUris;
    private ImageFile currentImage;
    private RadioGroup categoryButtonGroup;

    @BindView(R.id.product_image_slider_edit)
    SliderLayout mImageSlider;
    @BindView(R.id.product_detail_info_border)
    RelativeLayout mInfoBorder;
    @BindView(R.id.product_detail_price_border)
    RelativeLayout mPriceBorder;
//    @Bind(R.id.product_detail_date_border)
//    RelativeLayout mDateBorder;
    @BindView(R.id.product_detail_name_edit)
    EditText mProductName;
    @BindView(R.id.product_detail_description_edit)
    EditText mProductDescription;
    @BindView(R.id.product_detail_location_edit)
    EditText mProductLocation;
    @BindView(R.id.product_detail_category_container)
    LinearLayout mProductCategoryContainer;
    @BindView(R.id.product_detail_expiration_edit)
    EditText mProductExpiration;

    @BindView(R.id.product_detail_unit_edit)
    EditText mProductUnit;
    @BindView(R.id.product_detail_quantity_edit)
    EditText mProductQuantity;
    @BindView(R.id.product_detail_price_edit)
    EditText mProductPrice;

    private Activity mActivity;

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
        mActivity = getActivity();
        mImageUris = new ArrayList<>(MAX_IMAGES);
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
        mProductDescription.setText(FormatHelper.formatDescription(mProduct.getDescription()));
        mProductLocation.setText(mProduct.getLocation());
        mProductUnit.setText(mProduct.getSIUnit());
        mProductQuantity.setText(mProduct.getQuantity().toString());
        mProductPrice.setText(mProduct.getPricePerUnit());
        mProductExpiration.setText(mProduct.getExpirationFormattedForApp());

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

        Uri thumbnail = mProduct.getmThumbnail();

        if (thumbnail!= null && !thumbnail.toString().isEmpty() &&
                mProduct.getAuxImages().size() >= MAX_IMAGES) {
            Snackbar.make(getView(), "Can't upload more than 4 pictures. Pick your best pictures !", Snackbar.LENGTH_LONG).show();
            //Image limit reached to maximum. Only show option to delete images.
            displayOnlyDeletePhotoOption(slider);
            return;
        }

        if (mProduct.getmThumbnail().toString().isEmpty() && mProduct.getAuxImages().size() < 1) {
            //No Image available. Only show option to add image. No option to delete image
            displayOnlyAddPhotoOption();
            return;
        }

        displayDefaulPhotoOptions(slider);
    }

    private void displayDefaulPhotoOptions(final BaseSliderView slider) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        dispatchGalleryPictureIntent();
                        break;
                    case 2:
                        Log.d("PHOTO", "Cancel clicked...");
                        dialog.dismiss();
                        break;
                    case 3:
                        deleteImage(slider.getUrl());
                        break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Manage Photo");

        builder.setItems(new CharSequence[]{"Camera", "Gallery", "Cancel", "Delete"},
                dialogClickListener);
        builder.create().show();
    }

    private void displayOnlyDeletePhotoOption(final BaseSliderView slider) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case 0:
                        Log.d("PHOTO", "Cancel clicked...");
                        dialog.dismiss();
                        break;
                    case 1:
                        deleteImage(slider.getUrl());
                        break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Manage Photo");

        builder.setItems(new CharSequence[]{"Cancel", "Delete"},
                dialogClickListener);
        builder.create().show();
    }

    private void displayOnlyAddPhotoOption() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        dispatchGalleryPictureIntent();
                        break;
                    case 2:
                        Log.d("PHOTO", "Cancel clicked...");
                        dialog.dismiss();
                        break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Manage Photo");
//        builder.setMessage("Would you like to take a new product " +
//                "picture with the camera or pick from the photo gallery?");

        builder.setItems(new CharSequence[]{"Camera", "Gallery", "Cancel"},
                dialogClickListener);
        builder.create().show();
    }

    private void dispatchTakePictureIntent() {
        REQUEST_IMAGE_TYPE = REQUEST_IMAGE_CAPTURE;
        requestPhotoPermissions();
    }

    private void dispatchGalleryPictureIntent() {
        REQUEST_IMAGE_TYPE = REQUEST_LOAD_IMAGE;
        requestPhotoPermissions();
    }

    private void requestPhotoPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(mActivity, "Please grant permissions to change photo", Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else if (ContextCompat.checkSelfPermission(mActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            addImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    addImage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(mActivity, "Permissions Denied to access Photos", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void addImage() {
        if (REQUEST_IMAGE_TYPE == REQUEST_IMAGE_CAPTURE) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                ImageFile image = null;

                try {
                    image = new ImageFile(mActivity);
                } catch (Exception e) {
                    Log.d("IMAGE_CAPTURE","Issue creating image file");
                }

                if (image != null) {
                    Uri imgUri = FileProvider.getUriForFile(mActivity,
                            Constants.FILE_PROVIDER,
                            image);
                    currentImage = image;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else if (REQUEST_IMAGE_TYPE == REQUEST_LOAD_IMAGE) {
            Intent galleryPictureIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryPictureIntent.setType("image/*");
            galleryPictureIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryPictureIntent, "Select Picture"), REQUEST_LOAD_IMAGE);
        }
    }

    private void deleteImage(final String imageUrl) {
        final String imageFilename = imageUrl.substring(imageUrl.lastIndexOf('/')+1, imageUrl.length() );

        // Delete image request
        IProductRequest productService = new ProductRequest(ProductDetailEditFragment.this.getContext());
        productService.deleteProductImage(mProduct, imageFilename, new ICallback<Product>() {
            @Override
            public void onResult(Product result) {
                mImageSlider.removeSliderAt(mImageSlider.getCurrentPosition());
                mImageUris.remove(Uri.parse(imageUrl));
                updateImageSlider();
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
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            submitProductImage(currentImage.getUri());
        } else if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = mActivity.getContentResolver().query(selectedImageUri,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                currentImage = new ImageFile(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            submitProductImage(selectedImageUri);
        } else {
                Snackbar.make(getView(), "Can't upload more than " + MAX_IMAGES + " pictures. Pick your best pictures !", Snackbar.LENGTH_LONG).show();
        }
    }

    private void submitProductImage(Uri imageUri) {
        Uri thumbnail = mProduct.getmThumbnail();

        //If thumbnail is not assigned yet, then add first picture as thumbnail
        if (thumbnail == null || (thumbnail instanceof Uri && thumbnail.toString().isEmpty())) {
            submitProductThumbnail(imageUri);
        } else {
            //thumbnail already exists, carry on adding aux images
            submitAuxProductImage(imageUri);
        }
    }

    private void submitAuxProductImage(Uri imageUri) {
        boolean isImageAdded = mProduct.addImage(imageUri);
        if (isImageAdded) {
            mImageUris.add(0, imageUri);
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

    private void submitProductThumbnail(Uri imageUri) {
        boolean isImageAdded = mProduct.addImage(imageUri);
        if (isImageAdded) {
            mImageUris.add(0, imageUri);
            updateImageSlider();

            mProduct.setThumbnail(imageUri);

            IProductRequest productService = new ProductRequest(this.getContext());
            productService.submitProductThumbnail(mProduct.getId(), mProduct.getmThumbnail(), new ICallback<String>() {
                @Override
                public void onResult(String result) {
                    Log.d("image thumb upload","Success");
                    Toast.makeText(getContext(), "Product thumbnail uploaded.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("image thumb upload err",e.toString());
                    Toast.makeText(getContext(), "Error occurred while uploading Product thumbnail.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public ImageFile getCurrentImage(){
        return currentImage;
    }

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

    private void buildImageSlider() {
        Uri thumbnail = mProduct.getmThumbnail();
        if (thumbnail instanceof Uri && !thumbnail.toString().isEmpty()) {
            mImageUris.add(thumbnail);
        }

        for(Uri image : mProduct.getAuxImages()){
            mImageUris.add(image);
        }

        updateImageSlider();
    }

    private void updateImageSlider() {
        mImageSlider.removeAllSliders();

        for (Uri uri : mImageUris) {
            String url = uri.toString();
            url = url.trim();
            if(!url.isEmpty()){
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView
                        .image(url)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                mImageSlider.addSlider(sliderView);
            }
        }

        //If there's no picture available for product, then show a no_image picture
        if (mImageUris.size() < 1) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            sliderView
                    .image("http://tesitoo.com/image/cache/no_image-100x100.png")
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            mImageSlider.addSlider(sliderView);
        }

        mImageSlider.stopAutoCycle();
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