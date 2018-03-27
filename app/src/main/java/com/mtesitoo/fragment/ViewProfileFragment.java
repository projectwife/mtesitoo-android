package com.mtesitoo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtesitoo.AbstractPermissionFragment;
import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.backend.cache.ZoneCache;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.Zone;
import com.mtesitoo.backend.service.ZoneRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IZoneRequest;
import com.mtesitoo.helper.ImageHelper;
import com.mtesitoo.helper.UriAdapter;
import com.mtesitoo.model.ImageFile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nan on 12/30/2015.
 */
public class ViewProfileFragment extends AbstractPermissionFragment {
    private static final String TAG = "ViewProfileFragment";
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static Seller mSeller;
    private Context mContext;
    private ImageFile mProfileImageFile;

    @BindView(R.id.profileImage)
    ImageView mProfileImage;
    @BindView(R.id.etFirstName)
    EditText mFirstName;
    @BindView(R.id.etLastName)
    EditText mLastName;
    @BindView(R.id.etPhone)
    EditText mProfileTelephone;
    @BindView(R.id.etEmail)
    EditText mProfileEmail;
    @BindView(R.id.etBusiness)
    EditText mProfileCompanyName;
    @BindView(R.id.etDescription)
    EditText mProfileDescription;
    @BindView(R.id.etAddress1)
    EditText mProfileAddress1;
    @BindView(R.id.etCity)
    EditText mProfileCity;
    @BindView(R.id.etState)
    EditText mProfileState;
    @BindView(R.id.etCountry)
    EditText mProfileCountry;

    private Uri newProfileImageUri = null;
    private int selectedStatePosition = -1;
    private int selectedCountryPosition = -1;

    //Password reset flag
    private static boolean resetPasswordFlag = false;
    private static String mTempPasswordToken = null;

    private SharedPreferences sharedPreferences;
    private Gson gson;

    private Callback profilePicassoCallback = new Callback() {
        @Override
        public void onSuccess() {
            Bitmap imageBitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
            RoundedBitmapDrawable imageDrawable = ImageHelper.createRoundedBitmapImageDrawableWithBorder(getContext(),
                    imageBitmap, ContextCompat.getColor(getContext(), R.color.primary_dark));
            mProfileImage.setImageDrawable(imageDrawable);
        }

        @Override
        public void onError() {
            mProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }
    };

    private OnFragmentInteractionListener mListener;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    public static ViewProfileFragment newInstance() {
        ViewProfileFragment fragment = new ViewProfileFragment();
        return fragment;
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

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getContext();
        sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriAdapter())
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadData();
    }

    private void loadData() {
        if (getSeller() instanceof Seller) {

            if (mSeller.getmThumbnail() != null && !mSeller.getmThumbnail().toString().equals("null")) {
                Picasso.with(getContext()).load(mSeller.getmThumbnail().toString()).into(mProfileImage, profilePicassoCallback);
            } else {
                mProfileImage.setImageURI(null);
                mProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
            }

            if (mSeller.getmBusiness() != null && !mSeller.getmBusiness().isEmpty()) {
                mProfileCompanyName.setText(mSeller.getmBusiness());
            }

            mFirstName.setText(mSeller.getmFirstName());
            mLastName.setText(mSeller.getmLastName());
            mProfileAddress1.setText(mSeller.getmAddress1());
            mProfileTelephone.setText(mSeller.getmPhoneNumber());
            mProfileEmail.setText(mSeller.getmEmail());
            mProfileDescription.setText(mSeller.getmDescription());
            mProfileCity.setText(mSeller.getmCity());

            final SharedPreferences.Editor mEditor = sharedPreferences.edit();

            //ICountriesCache countriesCache = new CountriesCache(mContext);
            ArrayList<Countries> countriesArrayList = new ArrayList<>();
            //Note: hard-coding to Gambia.
            countriesArrayList.add(new Countries(79, "Gambia"));
            mEditor.putString("SelectedCountries", "79");
            mEditor.apply();

            mProfileCountry.setText("Gambia");
            mProfileState.setText(mSeller.getmState());

        } else {
            Log.e(TAG, "Failed to get Seller object");
        }
    }

    private Seller getSeller() {
        if (sharedPreferences.contains(Constants.LOGGED_IN_USER_DATA)) {
            mSeller = gson.fromJson(sharedPreferences.getString(Constants.LOGGED_IN_USER_DATA, ""), Seller.class);
        }

        return mSeller;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                editProfile();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editProfile() {
        ProfileFragment editFragment = ProfileFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("ProfileView");
        transaction.replace(R.id.fragment_container, editFragment).commit();

    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
