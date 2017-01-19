package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nan on 12/30/2015.
 */
public class ProfileFragment extends Fragment {

    private static Seller mSeller;
    private static Context mContext;

    @Bind(R.id.logo)
    ImageView mProfileImage;
    @Bind(R.id.profile_name)
    TextView mProfileName;
    @Bind(R.id.profile_username)
    TextView mProfileUsername;
    @Bind(R.id.profile_telephone)
    TextView mProfileTelephone;
    @Bind(R.id.profile_email)
    TextView mProfileEmail;
    @Bind(R.id.profile_companyname)
    TextView mProfileCompanyName;
    @Bind(R.id.profile_description)
    TextView mProfileDescription;
    @Bind(R.id.profile_address1)
    TextView mProfileAddress1;
    @Bind(R.id.profile_address2)
    TextView mProfileAddress2;
    @Bind(R.id.profile_city)
    TextView mProfileCity;
    @Bind(R.id.profile_state)
    TextView mProfileState;
    @Bind(R.id.profile_country)
    TextView mProfileCountry;
    @Bind(R.id.profile_postcode)
    TextView mProfilePostcode;

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
        View view = inflater.inflate(R.layout.activity_my_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = this.getArguments();
        mSeller = args.getParcelable(getString(R.string.bundle_seller_key));
        Picasso.with(getContext()).load(mSeller.getmThumbnail().toString()).into(mProfileImage);
        mProfileName.setText(mSeller.getmFirstName() + " " + mSeller.getmLastName());
        mProfileUsername.setText(mSeller.getmUsername());
        mProfileAddress1.setText(mSeller.getmAddress1());
        mProfileAddress2.setText(mSeller.getmAddress2());
        mProfileTelephone.setText(mSeller.getmPhoneNumber());
        mProfileEmail.setText(mSeller.getmEmail());
        mProfileCompanyName.setText(mSeller.getmBusiness());
        mProfileDescription.setText(mSeller.getmDescription());
        mProfileCity.setText(mSeller.getmCity());
        mProfileState.setText(mSeller.getmState());
        mProfileCountry.setText(mSeller.getmCountry());
        mProfilePostcode.setText(mSeller.getmPostcode());
    }

    @OnClick(R.id.update_profile)
    public void onUpdateProfileClick(View view) {
        String userName = mProfileUsername.getText().toString();
        String firstName = mProfileName.getText().toString();
        String lastName = mProfileName.getText().toString();
        String phoneNumber = mProfileTelephone.getText().toString();
        String email = mProfileEmail.getText().toString();
        String address1 = mProfileAddress1.getText().toString();
        String address2 = mProfileAddress2.getText().toString();
        String postCode = mProfilePostcode.getText().toString();
        String city = mProfileCity.getText().toString();
        String state = mProfileState.getText().toString();
        String country = mProfileCountry.getText().toString();
        String businessName = mProfileCompanyName.getText().toString();
        String description = mProfileDescription.getText().toString();

        mSeller.setmFirstName(firstName);
        mSeller.setmLastName(lastName);
        mSeller.setmPhoneNumber(phoneNumber);
        mSeller.setmEmail(email);
        mSeller.setmAddress1(address1);
        mSeller.setmAddress2(address2);
        mSeller.setmPostcode(postCode);
        mSeller.setmCity(city);
        mSeller.setmState(state);
        mSeller.setmCountry(country);
        mSeller.setmBusiness(businessName);
        mSeller.setmDescription(description);

        final ISellerRequest sellerService = new SellerRequest(mContext);
        sellerService.getSellerInfo(mSeller.getId(), new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                sellerService.updateSellerProfile(mSeller, new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Toast.makeText(mContext, R.string.profile_updated, Toast.LENGTH_LONG).show();
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
