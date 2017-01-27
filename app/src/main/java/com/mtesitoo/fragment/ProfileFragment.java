package com.mtesitoo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mtesitoo.R;
import com.mtesitoo.backend.cache.CountriesCache;
import com.mtesitoo.backend.cache.ZoneCache;
import com.mtesitoo.backend.cache.logic.ICountriesCache;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.Zone;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.model.ImageFile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nan on 12/30/2015.
 */
public class ProfileFragment extends Fragment {
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static Seller mSeller;
    private static Context mContext;
    private ImageFile mProfileImageFile;

    @Bind(R.id.profileImage)
    ImageView mProfileImage;
//    @Bind(R.id.profile_name)
//    TextView mProfileName;
//    @Bind(R.id.profile_username)
//    TextView mProfileUsername;

    @Bind(R.id.etFirstName)
    EditText mFirstName;
    @Bind(R.id.etLastName)
    EditText mLastName;
    @Bind(R.id.etPhone)
    EditText mProfileTelephone;
    @Bind(R.id.etEmail)
    EditText mProfileEmail;
    @Bind(R.id.etBusiness)
    EditText mProfileCompanyName;
    @Bind(R.id.etDescription)
    EditText mProfileDescription;
    @Bind(R.id.etAddress1)
    EditText mProfileAddress1;
    @Bind(R.id.etAddress2)
    EditText mProfileAddress2;
    @Bind(R.id.etCity)
    EditText mProfileCity;
    @Bind(R.id.spinnerState)
    Spinner mProfileState;
    @Bind(R.id.spinnerCountry)
    Spinner mProfileCountry;
    @Bind(R.id.etPostCode)
    TextView mProfilePostcode;
    @Bind(R.id.etPassword)
    EditText mPassword;

    public static ProfileFragment newInstance(Context context, Seller seller) {
        mContext = context;
        mSeller = seller;
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_seller_key), seller);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = this.getArguments();
        mSeller = args.getParcelable(getString(R.string.bundle_seller_key));
        if (mSeller.getmThumbnail() != null && !mSeller.getmThumbnail().toString().equals("null")) {
            Picasso.with(getContext()).load(mSeller.getmThumbnail().toString()).into(mProfileImage);
        }

        if (mSeller.getmBusiness() != null && !mSeller.getmBusiness().isEmpty()) {
            mProfileCompanyName.setText(mSeller.getmBusiness());
        } else {
            mProfileCompanyName.setText(mSeller.getmFirstName() + " " + mSeller.getmLastName());
        }

        mProfileAddress1.setText(mSeller.getmAddress1());
        mProfileAddress2.setText(mSeller.getmAddress2());
        mProfileTelephone.setText(mSeller.getmPhoneNumber());
        mProfileEmail.setText(mSeller.getmEmail());
        mProfileDescription.setText(mSeller.getmDescription());
        mProfileCity.setText(mSeller.getmCity());
        mProfilePostcode.setText(mSeller.getmPostcode());

        //TODO Spinner is blocked on incorrect population of countries and its zones
        //mProfileState.setText(mSeller.getmState());
        //mProfileCountry.setText(mSeller.getmCountry());


        ICountriesCache countriesCache = new CountriesCache(mContext);
        ArrayList<Countries> countriesArrayList = (ArrayList<Countries>) countriesCache.getCountries();

        mProfileCountry.setAdapter(new ArrayAdapter<Countries>(mContext,
                android.R.layout.simple_spinner_item,
                countriesArrayList));
        mProfileCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProfileCountry.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        IZonesCache zoneCache = new ZoneCache(mContext);
        ArrayList<Zone> zoneArrayList = (ArrayList<Zone>) zoneCache.GetZones();

        mProfileState.setAdapter(new ArrayAdapter<Zone>(mContext,
                android.R.layout.simple_spinner_item,
                zoneArrayList));
        mProfileState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProfileState.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            String imagePath = getPath(selectedImageUri);
            //Uri.fromFile(new File(imagePath))
            ISellerRequest sellerRequest = new SellerRequest(this.getContext());
            try {
                sellerRequest.submitProfileImage(new ImageFile(imagePath).getUri(), new ICallback<String>() {
                    @Override
                    public void onResult(String result) {
                        mProfileImage.setImageURI(selectedImageUri);
                        Snackbar.make(getView(), "Profile Image Uploaded Successfully",
                                Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("UploadImage", e.toString());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

            ISellerRequest sellerRequest = new SellerRequest(this.getContext());
            sellerRequest.submitProfileImage(mProfileImageFile.getUri(), new ICallback<String>() {
                @Override
                public void onResult(String result) {
                    mProfileImage.setImageURI(mProfileImageFile.getUri());
                    Snackbar.make(getView(), "Profile Image Uploaded Successfully",
                            Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("UploadImage", e.toString());
                }
            });
        }
    }

    @OnClick(R.id.manageProfilePhoto)
    public void onUpdateProfileImage(View view) {
        CharSequence options[] = new CharSequence[] {"Pick from Gallery", "Add an Image"};
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.EditImages))
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0:
                                Snackbar.make(getView(), "Not Supported", Snackbar.LENGTH_LONG).show();
                                //TODO
                                //Picking photo from gallery doesn't work due to issues in
                                //reading file from external location other than app's data dir
//                                intent = new Intent(
//                                        Intent.ACTION_PICK,
//                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                intent.setType("image/*");
//                                intent.setAction(Intent.ACTION_GET_CONTENT);
//                                startActivityForResult(Intent.createChooser(intent,
//                                        "Select Picture"), SELECT_PICTURE);
                                break;
                            case 1:
                            default:
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                                Toast.makeText(getActivity(),"Deleted Image Successfully",Toast.LENGTH_SHORT).show();
                                mProfileImage.setImageURI(null);
                                mProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getActivity(),"Error Deleting Image",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .show();
    }

    @OnClick(R.id.updateProfile)
    public void onUpdateProfileClick(View view) {
        String businessName = mProfileCompanyName.getText().toString();
        String description = mProfileDescription.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String phoneNumber = mProfileTelephone.getText().toString();
        String email = mProfileEmail.getText().toString();

        String address1 = mProfileAddress1.getText().toString();
        String address2 = mProfileAddress2.getText().toString();
        String city = mProfileCity.getText().toString();
        //String state = mProfileState.getText().toString();
        String postCode = mProfilePostcode.getText().toString();
        //String country = mProfileCountry.getText().toString();

        mSeller.setmFirstName(firstName);
        mSeller.setmLastName(lastName);
        mSeller.setmPhoneNumber(phoneNumber);
        mSeller.setmEmail(email);
        mSeller.setmAddress1(address1);
        mSeller.setmAddress2(address2);
        mSeller.setmPostcode(postCode);
        mSeller.setmCity(city);
        //TODO: Disabling state and country unless its fixed
        mSeller.setmState(null);
        mSeller.setmCountry(null);

        mSeller.setmBusiness(businessName);
        mSeller.setmDescription(description);

        final ISellerRequest sellerService = new SellerRequest(mContext);
        sellerService.getSellerInfo(mSeller.getId(), new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                sellerService.updateSellerProfile(mSeller, new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Snackbar.make(getView(), getString(R.string.profile_updated),
                                Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        String errorMsg = "";

                        if (e instanceof JSONException) {
                            errorMsg = "Error updating profile: " + e.getMessage();
                        } else {
                            VolleyError err = (VolleyError) e;
                            if(err.networkResponse.data!=null) {
                                try {
                                    String body = new String(err.networkResponse.data,"UTF-8");
                                    Log.e("REG_ERR",body);
                                    JSONObject jsonErrors = new JSONObject(body);
                                    JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                                    errorMsg = error.getString("message");
                                } catch (UnsupportedEncodingException encErr) {
                                    encErr.printStackTrace();
                                } catch (JSONException jErr) {
                                    errorMsg = "Error updating profile: " +jErr.getMessage();
                                    jErr.printStackTrace();
                                } finally {
                                    if(errorMsg.equals("")){
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
                VolleyError err = (VolleyError)e;

                String errorMsg = "";
                if(err.networkResponse.data!=null) {
                    try {
                        String body = new String(err.networkResponse.data,"UTF-8");
                        Log.e("REG_ERR",body);
                        JSONObject jsonErrors = new JSONObject(body);
                        JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                        errorMsg = error.getString("message");
                    } catch (UnsupportedEncodingException encErr) {
                        encErr.printStackTrace();
                    } catch (JSONException jErr) {
                        jErr.printStackTrace();
                    } finally {
                        if(errorMsg.equals("")){
                            errorMsg = "Error updating profile";
                        }
                    }
                }

                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.etPassword)
    public void updatePassword() {
        final ISellerRequest sellerService = new SellerRequest(mContext);
        sellerService.getSellerInfo(mSeller.getId(), new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                sellerService.updatePassword("tesitoo1", "tesitoo", new ICallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Snackbar.make(getView(), getString(R.string.password_updated),
                                Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        String errorMsg = "";

                        if (e instanceof JSONException) {
                            errorMsg = "Error updating profile: " + e.getMessage();
                        } else {
                            VolleyError err = (VolleyError) e;
                            if(err.networkResponse.data!=null) {
                                try {
                                    String body = new String(err.networkResponse.data,"UTF-8");
                                    Log.e("REG_ERR",body);
                                    JSONObject jsonErrors = new JSONObject(body);
                                    JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                                    errorMsg = error.getString("message");
                                } catch (UnsupportedEncodingException encErr) {
                                    encErr.printStackTrace();
                                } catch (JSONException jErr) {
                                    errorMsg = "Error updating profile: " +jErr.getMessage();
                                    jErr.printStackTrace();
                                } finally {
                                    if(errorMsg.equals("")){
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
                VolleyError err = (VolleyError)e;

                String errorMsg = "";
                if(err.networkResponse.data!=null) {
                    try {
                        String body = new String(err.networkResponse.data,"UTF-8");
                        Log.e("REG_ERR",body);
                        JSONObject jsonErrors = new JSONObject(body);
                        JSONObject error = jsonErrors.getJSONArray("errors").getJSONObject(0);
                        errorMsg = error.getString("message");
                    } catch (UnsupportedEncodingException encErr) {
                        encErr.printStackTrace();
                    } catch (JSONException jErr) {
                        jErr.printStackTrace();
                    } finally {
                        if(errorMsg.equals("")){
                            errorMsg = "Error updating profile";
                        }
                    }
                }

                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
