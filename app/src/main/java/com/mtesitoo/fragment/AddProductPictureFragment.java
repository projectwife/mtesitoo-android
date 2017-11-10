package com.mtesitoo.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mtesitoo.AbstractPermissionFragment;
import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.adapter.AddProductPicturesAdapter;
import com.mtesitoo.model.ImageFile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eduardodiaz on 18/10/2017.
 */

public class AddProductPictureFragment extends AbstractPermissionFragment {

    @BindView(R.id.pictures_recyclerview)
    RecyclerView picturesRecyclerView;

    AddProductPicturesAdapter addProductPicturesAdapter;

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private int actionSelected = -1;
    private ImageFile imageFile;

    private boolean isNewImage = true;
    private int selectedPosition = -1;

    public AddProductPictureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_pictures, container, false);

        ButterKnife.bind(this, rootView);

        populateScreen();

        return rootView;
    }

    private void populateScreen() {
        addProductPicturesAdapter = new AddProductPicturesAdapter(getContext(), new AddProductPicturesAdapter.Callback() {
            @Override
            public void onItemClick(final String picture, final int position) {

                AddProductBottomSheetDialogFragment bottomSheetDialogFragment = AddProductBottomSheetDialogFragment.newInstance(picture.isEmpty());
                bottomSheetDialogFragment.show(getFragmentManager(), bottomSheetDialogFragment.getTag());

                isNewImage = picture.isEmpty();
                selectedPosition = position;

                bottomSheetDialogFragment.setCallback(new AddProductBottomSheetDialogFragment.Callback() {
                    @Override
                    public void optionSelected(int action) {
                        switch (action) {
                            case AddProductBottomSheetDialogFragment.ACTION_CAMERA:
                                if (checkStoragePermission()) {
                                    takePicture();
                                } else {
                                    actionSelected = REQUEST_IMAGE_CAPTURE;
                                    //Don't have permissions at this point, so go ahead and request permissions
                                    Toast.makeText(getActivity(), R.string.msg_permission_sorry, Toast.LENGTH_LONG)
                                            .show();
                                    requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                                }
                                break;
                            case AddProductBottomSheetDialogFragment.ACTION_GALLERY:
                                if (checkStoragePermission()) {
                                    pickFromGallery();
                                } else {
                                    actionSelected = SELECT_PICTURE;
                                    //Don't have permissions at this point, so go ahead and request permissions
                                    Toast.makeText(getActivity(), R.string.msg_permission_sorry, Toast.LENGTH_LONG)
                                            .show();
                                    requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                                }
                                break;

                            case AddProductBottomSheetDialogFragment.ACTION_REMOVE:
                                addProductPicturesAdapter.removePicture(position);
                                break;
                        }
                    }
                });
            }
        });
        picturesRecyclerView.setAdapter(addProductPicturesAdapter);

        picturesRecyclerView.setHasFixedSize(true);
        picturesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private boolean checkStoragePermission() {
        return super.isReady() || super.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected String[] getDesiredPermissions() {
        //Note: Add permissions here if permissions must be granted at the load of screen.
        // Otherwise go for granular approach to grant permissions as needed.

        //return(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE});
        return null;
    }

    @Override
    protected void onPermissionDenied() {
        Toast.makeText(getActivity(), R.string.msg_permission_sorry, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onReady(Bundle state) {
        if (actionSelected == REQUEST_IMAGE_CAPTURE) {
            takePicture();
        } else if (actionSelected == SELECT_PICTURE) {
            pickFromGallery();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            includeNewPicture(data.getData());

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

            includeNewPicture(imageFile.getUri());
        }
    }

    private void includeNewPicture(Uri pictureUri) {
        if (isNewImage) {
            addProductPicturesAdapter.insertPicture(selectedPosition, pictureUri);
        } else {
            addProductPicturesAdapter.updatePicture(selectedPosition, pictureUri);
        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            ImageFile image = null;

            try {
                image = new ImageFile(getActivity());
            } catch (Exception e) {
                Log.d("IMAGE_CAPTURE", "Issue creating image file");
            }

            if (image != null) {
                Uri imgUri = FileProvider.getUriForFile(getActivity(),
                        Constants.FILE_PROVIDER,
                        image);

                imageFile = image;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}
