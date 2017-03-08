package com.tech.freak.wizardpager.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tech.freak.wizardpager.R;
import com.tech.freak.wizardpager.model.Page;

public class ImageFragment extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final String NEW_IMAGE_URI = "new_image_uri";
    private static final int CAMERA_REQUEST_CODE = 1;

    protected static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private Page mPage;

    private ImageView imageView;

    private Uri mNewImageUri;

    public static ImageFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        ImageFragment f = new ImageFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

        if (savedInstanceState != null) {
            String uriString = savedInstanceState.getString(NEW_IMAGE_URI);
            if (!TextUtils.isEmpty(uriString)) {
                mNewImageUri = Uri.parse(uriString);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNewImageUri != null) {
            outState.putString(NEW_IMAGE_URI, mNewImageUri.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_image,
                container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage
                .getTitle());

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        String imageData = mPage.getData().getString(Page.SIMPLE_DATA_KEY);

        if (!TextUtils.isEmpty(imageData)) {
            Uri imageUri = Uri.parse(imageData);
            imageView.setImageURI(imageUri);
        } else {
            imageView.setImageResource(R.drawable.ic_person);
        }

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for run-time permissions here
                requestPhotoPermissions();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException(
                    "Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    imageView.setImageURI(mNewImageUri);
                    writeResult();
                }
                break;
        }
    }

    private void requestPhotoPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(getView(), "Please grant permissions to change profile photo", Snackbar.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    Snackbar.make(getView(), "Permissions Denied to access Photos", Snackbar.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void photoOps() {
        mNewImageUri = getActivity()
                .getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
        Intent photoFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, mNewImageUri);
        photoFromCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        getActivity().startActivityForResult(photoFromCamera, CAMERA_REQUEST_CODE);
    }

    private void writeResult() {
        mPage.getData().putString(Page.SIMPLE_DATA_KEY,
                (mNewImageUri != null) ? mNewImageUri.toString() : null);
        mPage.notifyDataChanged();
    }
}