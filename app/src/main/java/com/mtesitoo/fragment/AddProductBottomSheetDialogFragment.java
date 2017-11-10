package com.mtesitoo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.mtesitoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eduardodiaz on 21/10/2017.
 */

public class AddProductBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String BUNDLE_NEW_PICTURE = "bundle_new_picture";
    Callback callback;
    boolean isNewPicture;

    @BindView(R.id.fragment_add_product_bottom_sheet_remove)
    View removePicture;

    final static int ACTION_CAMERA = 0;
    final static int ACTION_GALLERY = 1;
    final static int ACTION_REMOVE = 2;

    public static AddProductBottomSheetDialogFragment newInstance(boolean isNewPicture) {

        Bundle args = new Bundle();

        args.putBoolean(BUNDLE_NEW_PICTURE, isNewPicture);

        AddProductBottomSheetDialogFragment fragment = new AddProductBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BUNDLE_NEW_PICTURE)) {
            this.isNewPicture = getArguments().getBoolean(BUNDLE_NEW_PICTURE);
        }
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        //noinspection RestrictedApi
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.add_product_bottom_sheet, null);
        dialog.setContentView(contentView);

        ButterKnife.bind(this, contentView);

        if (isNewPicture) removePicture.setVisibility(View.GONE);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @OnClick({R.id.fragment_add_product_bottom_sheet_camera, R.id.fragment_add_product_bottom_sheet_gallery,
            R.id.fragment_add_product_bottom_sheet_remove, R.id.fragment_add_product_bottom_sheet_cancel})
    public void onItemsClick(View view) {
        dismiss();
        int action = -1;
        switch (view.getId()) {
            case R.id.fragment_add_product_bottom_sheet_camera:
                action = 0;
                break;
            case R.id.fragment_add_product_bottom_sheet_gallery:
                action = 1;
                break;
            case R.id.fragment_add_product_bottom_sheet_remove:
                action = 2;
                break;
        }
        if (callback != null) {
            callback.optionSelected(action);
        }
    }

    interface Callback {
        void optionSelected(int action);
    }
}
