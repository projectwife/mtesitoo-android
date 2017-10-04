package com.mtesitoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.helper.ProductPriceHelper;
import com.mtesitoo.model.ImageFile;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddProductActivity2 extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOAD_IMAGE = 2;
    static int REQUEST_IMAGE_TYPE = 0;

    RelativeLayout addPhotoBox;
    TextView addPhotoText;
    ImageView photoDisplay;
    Drawable productPhotoDrawable;

    @BindView(R.id.add_product_name)
    @NonNull
    EditText productName;

    @BindView(R.id.add_product_description)
    @NonNull
    EditText productDescription;

    @BindView(R.id.add_new_product_ppu)
    @NonNull
    EditText pricePerUnit;

    @BindView(R.id.add_new_product_qty)
    @NonNull
    EditText productQty;

    String mSelectedCategoryName;
    private Uri imageUri;
    private ImageFile mProductImageFile;
    private Context mContext;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product2);
        unbinder = ButterKnife.bind(this);

        mContext =  this;

        final AppCompatButton addButton = (AppCompatButton) findViewById(R.id.add_new_product_button);
        AppCompatButton cancelButton = (AppCompatButton) findViewById(R.id.cancel_new_product_button);

        addPhotoBox = (RelativeLayout) findViewById(R.id.il_add_product_photo);
        addPhotoText = (TextView) findViewById(R.id.add_photo_text);
        photoDisplay = (ImageView) findViewById(R.id.photo_display);

        addCategories(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = productName.getText().toString();
                String desc = productDescription.getText().toString();
                String qty = productQty.getText().toString();
                String ppu = pricePerUnit.getText().toString();

                if (name.isEmpty() || desc.isEmpty() || qty.isEmpty() || ppu.isEmpty()) {
                    Toast.makeText(mContext, "Please provide missing product details", Toast.LENGTH_LONG).show();
                    return;
                }

                addProduct(name, desc, qty, ppu, mSelectedCategoryName);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Todo: ask user is sure they want to cancel if any fields contain data
                // Todo: improve by allowing user to save draft products for future adding?

                finish();
            }
        });

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dispatchTakePictureIntent();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dispatchGalleryPictureIntent();
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        Log.d("PHOTO", "Cancel clicked...");
                        dialog.dismiss();
                        break;
                }
            }
        };

        final AlertDialog.Builder addPhotoPopup = new AlertDialog.Builder(AddProductActivity2.this);
        addPhotoPopup.setTitle("Add Photo")
                .setMessage("Would you like to take a new product " +
                        "picture with the camera or pick from the photo gallery?")
                .setPositiveButton("Camera", dialogClickListener)
                .setNegativeButton("Gallery", dialogClickListener)
                .setNeutralButton("Cancel", dialogClickListener);

        final AlertDialog.Builder addPhotoPopupAgainBuilder = new AlertDialog.Builder(AddProductActivity2.this);
        addPhotoPopupAgainBuilder.setTitle("Change Photo")
                .setMessage("Would you like to replace the current " +
                        "picture with a new one from the camera or the photo gallery?")
                .setView(getLayoutInflater().inflate(R.layout.dialog_add_product_photo, null))
                .setPositiveButton("Camera", dialogClickListener)
                .setNegativeButton("Gallery", dialogClickListener)
                .setNeutralButton("Cancel", dialogClickListener);

        final AlertDialog addPhotoPopupAgain = addPhotoPopupAgainBuilder.create();

        addPhotoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (photoDisplay.getVisibility() == View.INVISIBLE) {
                    addPhotoPopup.show();
                } else {
                    addPhotoPopupAgain.show();
                    ImageView imagePreview = (ImageView) addPhotoPopupAgain.findViewById(R.id.dialog_add_photo_preview);
                    imagePreview.setImageDrawable(productPhotoDrawable);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            productPhotoDrawable = new BitmapDrawable(getResources(), imageBitmap);
            addPhotoText.setVisibility(View.INVISIBLE);
            photoDisplay.setVisibility(View.VISIBLE);
            //photoDisplay.setImageDrawable(productPhotoDrawable);

            imageUri = mProductImageFile.getUri();
            photoDisplay.setImageURI(imageUri);
        }

        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageUri = selectedImage;
            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);

            productPhotoDrawable = new BitmapDrawable(getResources(), imageBitmap);
            addPhotoText.setVisibility(View.INVISIBLE);
            photoDisplay.setVisibility(View.VISIBLE);
            photoDisplay.setImageDrawable(productPhotoDrawable);
        }
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
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please grant permissions to change profile photo", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            photoOps();
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
                    photoOps();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to access Photos", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void photoOps() {

        if (REQUEST_IMAGE_TYPE == REQUEST_IMAGE_CAPTURE) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                imageUri = this
//                        .getContentResolver()
//                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                new ContentValues());
//                Intent photoFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                photoFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                photoFromCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                ImageFile image = null;

                try {
                    image = new ImageFile(this);
                } catch (Exception e) {
                    Log.d("IMAGE_CAPTURE","Issue creating image file");
                }

                if (image != null) {
                    Uri imgUri = FileProvider.getUriForFile(this,
                            Constants.FILE_PROVIDER,
                            image);
                    mProductImageFile = image;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else if (REQUEST_IMAGE_TYPE == REQUEST_LOAD_IMAGE) {
            Intent galleryPictureIntent =
                    new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryPictureIntent, REQUEST_LOAD_IMAGE);
        }
    }
    private void addCategories(Context context) {
        GridLayout categoryGridView = (GridLayout) findViewById(R.id.category_grid);

        final RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioGroup.clearCheck();

        ICategoryCache cache = new CategoryCache(context);
        final List<Category> categories = cache.getCategories();
        String[] categoryNames = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            android.support.v7.widget.AppCompatRadioButton compatRadioButton = new AppCompatRadioButton(context);
            categoryNames[i] = categories.get(i).getName();
            compatRadioButton.setText(categoryNames[i]);
            compatRadioButton.setId(i);
            radioGroup.addView(compatRadioButton, i);
        }

        categoryGridView.addView(radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < categories.size(); i++) {
                    if (checkedId == i) {
                        mSelectedCategoryName = categories.get(i).getName();
                        break;
                    }
                }
            }
        });
    }

    private void addProduct(String name, String description, String quantity,
                            String pricePerUnit, String category) {
        ICategoryCache cache = new CategoryCache(this);
        List<Category> categories = cache.getCategories();

        for (Category c : categories) {
            if (c.getName().equals(category)) {
                category = Integer.toString(c.getId());
                break;
            }
        }

        final Product product = new Product(0, name, description, "Location", category, "SI Unit",
                pricePerUnit,
                ProductPriceHelper.getDisplayPrice(ProductPriceHelper.getDefaultCurrencyCode(), pricePerUnit),
                ProductPriceHelper.getDefaultCurrencyCode(),
                Integer.parseInt(quantity), new Date(), imageUri, null, 5, 0, 0);//Uri.parse(thumbnail)

        IProductRequest productService = new ProductRequest(this);
        productService.submitProduct(product, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                IProductRequest productService = new ProductRequest(AddProductActivity2.this);
                productService.submitProductThumbnail(Integer.parseInt(result), product.getmThumbnail(), new ICallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Log.d("image thumb upload","Success");
                        Toast.makeText(mContext, "Product thumbnail uploaded.", Toast.LENGTH_LONG).show();
//                        finish(); // finish will be called in the onResult for submitProduct. Shouldn't be called multiple times.

                        Intent intent = new Intent("submit_product_thumbnail");
                        intent.putExtra("result", result);
                        LocalBroadcastManager.getInstance(AddProductActivity2.this).sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("image thumb upload err",e.toString());
                        Toast.makeText(mContext, "Error occurred while uploading Product thumbnail.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

                Toast.makeText(mContext, "New Product Added.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(Exception e) {
                Log.e("product add error",e.toString());
                finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
