package com.mtesitoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.SellerService;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.ISellerService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private Context mContext;

    @OnClick(R.id.login)
    public void onClick(View view) {
        //TODO: Authenticate user

        final Intent intent = new Intent(this, HomeActivity.class);
        ISellerService sellerService = new SellerService(this);

        sellerService.getSellerInfo(4, new IResponse<Seller>() {
            @Override
            public void onResult(Seller result) {
                intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
                mContext.startActivity(intent);
            }

            @Override
            public void onError(Exception e) {}
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mContext = this;
    }
}