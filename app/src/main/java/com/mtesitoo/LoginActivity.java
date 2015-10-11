package com.mtesitoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.CategoryService;
import com.mtesitoo.backend.service.LoginService;
import com.mtesitoo.backend.service.SellerService;
import com.mtesitoo.backend.service.logic.ICategoryService;
import com.mtesitoo.backend.service.logic.ILoginService;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.ISellerService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private Context mContext;

    @Bind(R.id.user_name)
    TextView mUsername;
    @Bind(R.id.password)
    TextView mPassword;

    @OnClick(R.id.login)
    public void onClick(View view) {
        final Intent intent = new Intent(this, HomeActivity.class);
        final ISellerService sellerService = new SellerService(this);
        final ILoginService loginService = new LoginService(this);

        loginService.authenticateUser(mUsername.getText().toString(), mPassword.getText().toString(), new IResponse<String>() {
            @Override
            public void onResult(String result) {
                sellerService.getSellerInfo(Integer.parseInt(result), new IResponse<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mContext = this;
        ICategoryService categoryService = new CategoryService(this);

        categoryService.getCategories(new IResponse<List<Category>>() {
            @Override
            public void onResult(List<Category> categories) {
                ICategoryCache cache = new CategoryCache(mContext);
                cache.storeCategories(categories);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}