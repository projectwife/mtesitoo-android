package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Seller;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nan on 12/30/2015.
 */
public class ProfileFragment extends Fragment {

    private Seller mSeller;

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
        mProfileCompanyName.setText(mSeller.getmCompany());
        mProfileDescription.setText(mSeller.getmDescription());
        mProfileCity.setText(mSeller.getmCity());
        mProfileState.setText(mSeller.getmState());
        mProfileCountry.setText(mSeller.getmCountry());
        mProfilePostcode.setText(mSeller.getmPostcode());

    }

}
