package com.mtesitoo.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtesitoo.AbstractPermissionFragment;
import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.backend.cache.ZoneCache;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.ProfilePicture;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.Zone;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.ZoneRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.backend.service.logic.IZoneRequest;
import com.mtesitoo.helper.ImageHelper;
import com.mtesitoo.helper.UriAdapter;
import com.mtesitoo.model.ImageFile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Nan on 12/30/2015.
 */
public class ProfileFragment extends AbstractPermissionFragment {
    private static final String TAG = "ProfileFragment";
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
    @BindView(R.id.spinnerState)
    Spinner mProfileState;
    @BindView(R.id.spinnerCountry)
    Spinner mProfileCountry;

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

    public static ProfileFragment newInstance() {
        return newInstance(false, null);
    }

    public static ProfileFragment newInstance(boolean resetPassword, String token) {
        resetPasswordFlag = resetPassword;
        mTempPasswordToken = token;

        return new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        if (resetPasswordFlag && mTempPasswordToken != null) {
            showPasswordPrompt();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

            mProfileCountry.setAdapter(new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item,
                    countriesArrayList));
            /*mProfileCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mProfileCountry.setSelection(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });*/
            selectedCountryPosition = getSpinnerIndex(mProfileCountry, mSeller.getmCountry());
            mProfileCountry.setSelection(selectedCountryPosition);
            mProfileCountry.setEnabled(false);

            ArrayList<Zone> zoneArrayList;
            final ArrayAdapter[] zoneAdapter = {null};
            IZonesCache zoneCache = new ZoneCache(mContext);
            zoneArrayList = (ArrayList<Zone>) zoneCache.GetZones();
            if (zoneArrayList.isEmpty()) {
                IZoneRequest zoneService = new ZoneRequest(mContext);
                final String[][] zonesNames = {null};
                final ArrayList<Zone> finalZoneArrayList = zoneArrayList;
                zoneService.getZones(new ICallback<List<Zone>>() {
                    @Override
                    public void onResult(List<Zone> zones) {
                        IZonesCache zonesCache = new ZoneCache(mContext);
                        zonesCache.storeZones(zones);

                        zonesNames[0] = new String[zones.size()];

                        for (int i = 0; i < zones.size(); i++) {
                            zonesNames[0][i] = zones.get(i).getName();
                            Zone zone = zones.get(i);
                            finalZoneArrayList.add(new Zone(zone.getId(), zone.getName(), zone.getName()));
                        }

                        zoneAdapter[0] = new ArrayAdapter<>(mContext,
                                android.R.layout.simple_spinner_item,
                                finalZoneArrayList);
                        zoneAdapter[0].setDropDownViewResource(R.layout.item_spinner_profile);
                        mProfileState.setAdapter(zoneAdapter[0]);
                        selectedStatePosition = getSpinnerIndex(mProfileState, mSeller.getmState());
                        mProfileState.setSelection(selectedStatePosition);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Zones", e.toString());
                    }
                });

            } else {
                zoneAdapter[0] = new ArrayAdapter<>(mContext,
                        android.R.layout.simple_spinner_item,
                        zoneArrayList);
                zoneAdapter[0].setDropDownViewResource(R.layout.item_spinner_profile);

                mProfileState.setAdapter(zoneAdapter[0]);
                selectedStatePosition = getSpinnerIndex(mProfileState, mSeller.getmState());
                mProfileState.setSelection(selectedStatePosition);
            }

            mProfileState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mProfileState.setSelection(i);
                    selectedStatePosition = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } else {
            Log.e(TAG, "Failed to get Seller object");
        }

    }

    private Seller getSeller() {
        if (mSeller == null &&
                sharedPreferences.contains(Constants.LOGGED_IN_USER_DATA)) {
            mSeller = gson.fromJson(sharedPreferences.getString(Constants.LOGGED_IN_USER_DATA, ""), Seller.class);
        }

        return mSeller;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_profile:
                updateProfile();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            newProfileImageUri = data.getData();
            Picasso.with(getContext()).load(newProfileImageUri).into(mProfileImage, profilePicassoCallback);

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

            newProfileImageUri = mProfileImageFile.getUri();
            Picasso.with(getContext()).load(newProfileImageUri).into(mProfileImage, profilePicassoCallback);
        }
    }

    //// Run time Permissions Handling ////

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

    @OnClick({R.id.change_picture_button, R.id.profileImage})
    public void onUpdateProfileImage() {
        if (super.isReady()) {
            photoOps();
        } else {
            if (super.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                photoOps();
                return;
            }

            //Don't have permissions at this point, so go ahead and request permissions
            Toast.makeText(getActivity(), R.string.msg_permission_sorry, Toast.LENGTH_LONG)
                    .show();
            super.requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photoOps();
            } else {
                onPermissionDenied();
            }
        }
    }

    //If user has given needed permissions, then go ahead with accessing photos
    private void photoOps() {
        CharSequence options[] = new CharSequence[]{"Pick from Gallery", "Add an Image"};
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.EditImages))
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0:
                                intent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                                break;
                            case 1:
                            default:
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                                        mProfileImageFile = image;
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }

                        }

                    }
                })
                .setNegativeButton("Delete this image", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete image request
                        ISellerRequest sellerRequest = new SellerRequest(ProfileFragment.this.getContext());
                        sellerRequest.deleteProfileImage(new ICallback<String>() {
                            @Override
                            public void onResult(String result) {
                                Toast.makeText(getActivity(), "Deleted Image Successfully", Toast.LENGTH_SHORT).show();

                                mSeller.setmThumbnail("null");
                                updateProfileInPreferences();

                                mProfileImage.setImageURI(null);
                                mProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getActivity(), "Error Deleting Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void updateProfile() {

        if (checkProfileChanges() || newProfileImageUri != null) {

            if (checkProfileChanges()) {
                onUpdateProfileClick();
            }
            if (newProfileImageUri != null) {
                onUpdatePicture(newProfileImageUri);
            }
            return;
        }

        Snackbar.make(getView(), getString(R.string.profile_no_changes_message),
                Snackbar.LENGTH_LONG).show();
    }

    private void updateProfileInPreferences() {
        sharedPreferences.edit().putString(Constants.LOGGED_IN_USER_DATA, gson.toJson(mSeller)).apply();
    }

    private boolean checkProfileChanges() {

        return (mSeller.getmBusiness() != null && !mSeller.getmBusiness().equals(mProfileCompanyName.getText().toString()))
                || !mSeller.getmFirstName().equals(mFirstName.getText().toString())
                || !mSeller.getmLastName().equals(mLastName.getText().toString())
                || !mSeller.getmAddress1().equals(mProfileAddress1.getText().toString())
                || !mSeller.getmPhoneNumber().equals(mProfileTelephone.getText().toString())
                || !mSeller.getmEmail().equals(mProfileEmail.getText().toString())
                || !mSeller.getmDescription().equals(mProfileDescription.getText().toString())
                || !mSeller.getmCity().equals(mProfileCity.getText().toString())
                || getSpinnerIndex(mProfileState, mSeller.getmState()) != selectedStatePosition;

    }

    private void onUpdatePicture(final Uri selectedImageURI) {
        ISellerRequest sellerRequest = new SellerRequest(this.getContext());
        sellerRequest.submitProfileImage(selectedImageURI, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                if (result != null && !result.isEmpty()) {
                    ProfilePicture profilePicture = gson.fromJson(result, ProfilePicture.class);
                    mSeller.setmThumbnail(profilePicture.getThumbnailPath());
                    updateProfileInPreferences();
                }
                newProfileImageUri = null;
                Snackbar.make(getView(), "Profile Image Uploaded Successfully",
                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Snackbar.make(getView(), "Failed to upload profile Image",
                        Snackbar.LENGTH_SHORT).show();
                Log.e("UploadImage", e.toString());
            }
        });
    }

    private void onUpdateProfileClick() {
        String businessName = mProfileCompanyName.getText().toString();
        String description = mProfileDescription.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String phoneNumber = mProfileTelephone.getText().toString();
        String email = mProfileEmail.getText().toString();

        String address1 = mProfileAddress1.getText().toString();
        String city = mProfileCity.getText().toString();
        Countries country = (Countries) mProfileCountry.getSelectedItem();
        Zone zone = (Zone) mProfileState.getSelectedItem();

        mSeller.setmFirstName(firstName);
        mSeller.setmLastName(lastName);
        mSeller.setmPhoneNumber(phoneNumber);
        mSeller.setmEmail(email);
        mSeller.setmAddress1(address1);
        mSeller.setmCity(city);
        mSeller.setmZoneId(String.valueOf(zone.getId()));
        mSeller.setmCountry(String.valueOf(country.getId()));

        mSeller.setmBusiness(businessName);
        mSeller.setmDescription(description.replace("\n", "<br>"));

        final ISellerRequest sellerService = new SellerRequest(mContext);
        sellerService.getSellerInfo(mSeller.getId(), new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                sellerService.updateSellerProfile(mSeller, new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        if (result == null) {
                            Snackbar.make(getView(), getString(R.string.profile_updated),
                                    Snackbar.LENGTH_LONG).show();
                            updateProfileInPreferences();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        String errorMsg = "";

                        if (e instanceof JSONException) {
                            errorMsg = "Error updating profile: " + e.getMessage();
                        } else {
                            VolleyError err = (VolleyError) e;
                            if (err.networkResponse.data != null) {
                                try {
                                    String body = new String(err.networkResponse.data, "UTF-8");
                                    Log.e("REG_ERR", body);
                                    JSONObject jsonErrors = new JSONObject(body);
                                    JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                                    errorMsg = error.getString("message");
                                } catch (UnsupportedEncodingException encErr) {
                                    encErr.printStackTrace();
                                } catch (JSONException jErr) {
                                    errorMsg = "Error updating profile: " + jErr.getMessage();
                                    jErr.printStackTrace();
                                } finally {
                                    if (errorMsg.equals("")) {
                                        errorMsg = "Error updating profile";
                                    }
                                }
                            }
                        }

                        Snackbar.make(getView(), errorMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                String errorMsg = "";
                if (e != null && e instanceof VolleyError) {

                    VolleyError err = (VolleyError) e;

                    if (err.networkResponse != null) {
                        if (err.networkResponse.data != null) {
                            try {
                                String body = new String(err.networkResponse.data, "UTF-8");
                                Log.e("REG_ERR", body);
                                JSONObject jsonErrors = new JSONObject(body);
                                JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                                errorMsg = error.getString("message");
                            } catch (UnsupportedEncodingException | JSONException encErr) {
                                encErr.printStackTrace();
                            }
                        }
                    }
                }
                if (errorMsg.equals("")) {
                    errorMsg = "Error updating profile";
                }

                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePassword(final String oldPassword, final String newPassword) {
        mSeller = getSeller();

        if (!(mSeller instanceof Seller)) {
            Toast.makeText(mContext, "Password can't be updated at this time. Try again later!"
                    , Toast.LENGTH_LONG).show();
            Log.e(TAG, "Seller object is null");
            return;
        }
        final ISellerRequest sellerService = new SellerRequest(mContext);
        sellerService.getSellerInfo(mSeller.getId(), new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                sellerService.updatePassword(oldPassword, newPassword, new ICallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Snackbar.make(getView(), getString(R.string.password_updated),
                                Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        String errorMsg = "";

                        if (e instanceof JSONException) {
                            errorMsg = "Error updating password: " + e.getMessage();
                        } else {
                            VolleyError err = (VolleyError) e;
                            if (err.networkResponse.data != null) {
                                try {
                                    String body = new String(err.networkResponse.data, "UTF-8");
                                    Log.e("REG_ERR", body);
                                    JSONObject jsonErrors = new JSONObject(body);
                                    JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                                    errorMsg = error.getString("message");
                                } catch (UnsupportedEncodingException encErr) {
                                    encErr.printStackTrace();
                                } catch (JSONException jErr) {
                                    errorMsg = "Error updating password: " + jErr.getMessage();
                                    jErr.printStackTrace();
                                } finally {
                                    if (errorMsg.equals("")) {
                                        errorMsg = "Error updating password";
                                    }
                                }
                            }
                        }

                        Snackbar.make(getView(), errorMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                VolleyError err = (VolleyError) e;

                String errorMsg = "";
                if (err.networkResponse.data != null) {
                    try {
                        String body = new String(err.networkResponse.data, "UTF-8");
                        Log.e("REG_ERR", body);
                        JSONObject jsonErrors = new JSONObject(body);
                        JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                        errorMsg = error.getString("message");
                    } catch (UnsupportedEncodingException | JSONException encErr) {
                        encErr.printStackTrace();
                    } finally {
                        if (errorMsg.equals("")) {
                            errorMsg = "Error updating password";
                        }
                    }
                }

                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.change_password_button)
    public void showPasswordPrompt() {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptsView = layoutInflater.inflate(R.layout.profile_password_prompt, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);

        final EditText oldPassword = (EditText) promptsView.findViewById(R.id.editTextOldPassword);
        final EditText newPassword1 = (EditText) promptsView.findViewById(R.id.editTextNewPassword1);
        final EditText newPassword2 = (EditText) promptsView.findViewById(R.id.editTextNewPassword2);

        if (mTempPasswordToken != null) {
            oldPassword.setText(mTempPasswordToken);
        }

        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String oldPass = (oldPassword != null) ? oldPassword.getText().toString() : "";
                                String newPass1 = (newPassword1 != null) ? newPassword1.getText().toString() : "";
                                String newPass2 = (newPassword2 != null) ? newPassword2.getText().toString() : "";

                                //verify new password
                                if (!oldPass.isEmpty() && !newPass1.isEmpty() && !newPass2.isEmpty()
                                        && newPass1.equals(newPass2)) {
                                    updatePassword(oldPass, newPass1);
                                } else {
                                    String message;
                                    if (oldPass.isEmpty()) {
                                        message = "Don't forget to enter Old Password." + " \n \n" + "Please try again!";
                                    } else if (newPass1.isEmpty()) {
                                        message = "New Password can't be empty." + " \n \n" + "Please try again!";
                                    } else if (newPass2.isEmpty()) {
                                        message = "You need to re-enter new password." + " \n \n" + "Please try again!";
                                    } else if (!newPass1.equals(newPass2)) {
                                        message = "The new passwords don't match." + " \n \n" + "Please try again!";
                                    } else {
                                        message = "Something went wrong !" + " \n \n" + "Please try again!";
                                    }

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showPasswordPrompt();
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @OnClick(R.id.profile_relative_layout)
    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        View currentFocusView = getActivity().getCurrentFocus();
        if (currentFocusView != null)
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
    }
}