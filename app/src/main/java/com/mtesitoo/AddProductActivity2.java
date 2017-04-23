package com.mtesitoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;

import java.util.List;

public class AddProductActivity2 extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOAD_IMAGE = 2;

    RelativeLayout addPhotoBox;
    TextView addPhotoText;
    ImageView photoDisplay;
    Drawable productPhotoDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product2);

        final AppCompatButton addButton = (AppCompatButton) findViewById(R.id.add_new_product_button);
        AppCompatButton cancelButton = (AppCompatButton) findViewById(R.id.cancel_new_product_button);

        addPhotoBox = (RelativeLayout) findViewById(R.id.il_add_product_photo);
        addPhotoText = (TextView) findViewById(R.id.add_photo_text);
        photoDisplay = (ImageView) findViewById(R.id.photo_display);

        addCategories(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: add product
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
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            productPhotoDrawable = new BitmapDrawable(getResources(), imageBitmap);
            addPhotoText.setVisibility(View.INVISIBLE);
            photoDisplay.setVisibility(View.VISIBLE);
            photoDisplay.setImageDrawable(productPhotoDrawable);
        }

        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);

            productPhotoDrawable = new BitmapDrawable(getResources(), imageBitmap);
            addPhotoText.setVisibility(View.INVISIBLE);
            photoDisplay.setVisibility(View.VISIBLE);
            photoDisplay.setImageDrawable(productPhotoDrawable);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchGalleryPictureIntent() {
        Intent galleryPictureIntent =
                new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryPictureIntent, REQUEST_LOAD_IMAGE);
    }

    private void addCategories(Context context) {
        GridLayout categoriesGrid = (GridLayout) findViewById(R.id.category_grid);

        ICategoryCache cache = new CategoryCache(context);
        List<Category> categories = cache.getCategories();
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
            android.support.v7.widget.AppCompatCheckBox compatCheckBox = new AppCompatCheckBox(context);
            compatCheckBox.setText(categoryNames[i]);
            categoriesGrid.addView(compatCheckBox, i);
        }
    }
}
