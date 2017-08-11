package com.mtesitoo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ptyagi on 8/4/17.
 */

public class ContactFragment extends Fragment {

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment_contact, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.adminEmail)
    public void sendEmailToAdmin(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getResources().getString(R.string.admin_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Tesitoo Admin");
        intent.putExtra(Intent.EXTRA_TEXT, "Please describe your concern below\n");
        startActivity(Intent.createChooser(intent, ""));
    }

    @OnClick(R.id.admin_phone_line1)
    public void dialSupportLine1(View view) {
        String phone = getResources().getString(R.string.admin_email);
        dialPhone(phone);
    }

    @OnClick(R.id.admin_phone_line2)
    public void dialSupportLine2(View view) {
        String phone = getResources().getString(R.string.admin_email);
        dialPhone(phone);
    }

    private void dialPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
