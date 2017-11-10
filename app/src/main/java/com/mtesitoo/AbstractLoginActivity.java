package com.mtesitoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.UnitCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.cache.logic.IUnitCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.backend.service.CategoryRequest;
import com.mtesitoo.backend.service.CommonRequest;
import com.mtesitoo.backend.service.LoginRequest;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICommonRequest;
import com.mtesitoo.backend.service.logic.ILoginRequest;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.helper.UriAdapter;

import java.util.List;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * Created by eduardodiaz on 10/11/2017.
 */

abstract class AbstractLoginActivity extends AppCompatActivity {

    protected Context context;
    protected SharedPreferences sharedPreferences;

    @BindView(R.id.loading_progress_container)
    View loadingViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    protected void showLoginProgress(String message) {
        TextView loadingText = (TextView) findViewById(R.id.loading_text);
        if (message != null) {
            loadingText.setText(message);
        } else {
            loadingText.setText("Loading");
        }

        loadingViewContainer.setVisibility(View.VISIBLE);
    }

    protected void dismissLoginProgress() {
        loadingViewContainer.setVisibility(View.GONE);
    }

    protected void logInUser(final Intent intent, final String user, final String pass, final boolean resetPassword) {

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final ILoginRequest loginService = new LoginRequest(this);

        showLoginProgress("Logging in");
        loginService.authenticateUser(user, pass, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Log.d("LOGIN - RESULT", result);
                sendFirebaseTokenToServer();

                ISellerRequest sellerService = new SellerRequest(context);

                getUnits();
                getCategories();

                sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Log.d("Login - Seller Info", result.toString());

                        logUser(result);
                        logSuccessLogin(result);

                        //Cache login state in sharedprefs
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constants.IS_USER_LOGGED_IN_KEY, true);
                        editor.putString(Constants.LOGGED_IN_USER_ID_KEY, user);
                        editor.putString(Constants.LOGGED_IN_USER_PASS_KEY, pass);
                        editor.apply();
                        cacheLoggedInUserData(result);

                        intent.putExtra(context.getString(R.string.bundle_seller_key), result);
                        intent.putExtra(context.getString(R.string.automatic_login_key), resetPassword);
                        if (resetPassword) {
                            intent.putExtra(context.getString(R.string.automatic_login_token), pass);
                        }
                        context.startActivity(intent);
                        dismissLoginProgress();
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("getSellerInfo", e.toString());
                        dismissLoginProgress();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("AuthenticateUser", e.toString());
                logFailLogin(user, e);
                String errorMessage;

                if (e.getMessage() != null) {
                    errorMessage = "Failed to log in due to an error: " +
                            e.getMessage() + ". Try again later.";
                } else {
                    errorMessage = "Failed to log in user !";
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                dismissLoginProgress();
            }
        });
    }

    protected void logInByCode(final Intent intent, final String code, final boolean resetPassword) {
        final ILoginRequest loginService = new LoginRequest(this);

        loginService.authenticateUser(code, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Log.d("LOGIN - RESULT", result);
                ISellerRequest sellerService = new SellerRequest(context);

                getUnits();
                getCategories();

                sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Log.d("Login - Seller Info", result.toString());

                        logUser(result);
                        logSuccessLogin(result);
                        cacheLoggedInUserData(result);

                        intent.putExtra(context.getString(R.string.bundle_seller_key), result);
                        intent.putExtra(context.getString(R.string.automatic_login_key), resetPassword);
                        if (resetPassword) {
                            intent.putExtra(context.getString(R.string.automatic_login_token), code);
                        }
                        context.startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("getSellerInfo", e.toString());
                        dismissLoginProgress();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("AuthenticateUser", e.toString());
                logFailLogin(code, e);
                String errorMessage = e.getMessage();
                if (errorMessage == null) {
                    errorMessage = "Authentication Failed.";
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                dismissLoginProgress();
            }
        });
    }

    private void getCategories() {
        ICategoryRequest categoryService = new CategoryRequest(context);
        categoryService.getCategories(new ICallback<List<Category>>() {
            @Override
            public void onResult(List<Category> categories) {
                ICategoryCache cache = new CategoryCache(context);
                cache.storeCategories(categories);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Categories", e.toString());
            }
        });
    }

    private void getUnits() {
        ICommonRequest commonService = new CommonRequest(context);
        commonService.getLengthUnits(new ICallback<List<Unit>>() {
            @Override
            public void onResult(List<Unit> units) {
                IUnitCache cache = new UnitCache(context);
                cache.storeLengthUnits(units);
            }

            @Override
            public void onError(Exception e) {
                Log.e("LengthUnits", e.toString());
            }
        });

        commonService.getWeightUnits(new ICallback<List<Unit>>() {
            @Override
            public void onResult(List<Unit> units) {
                IUnitCache cache = new UnitCache(context);
                cache.storeWeightUnits(units);
            }

            @Override
            public void onError(Exception e) {
                Log.e("WeightUnits", e.toString());
            }
        });
    }

    private void getSeller() {
        ISellerRequest sellerService = new SellerRequest(context);
    }

    private void logUser(Seller seller) {
        Crashlytics.setUserIdentifier(String.valueOf(seller.getId()));
        Crashlytics.setUserEmail(seller.getmEmail());
        Crashlytics.setUserName(seller.getUsername());
    }

    private void logSuccessLogin(Seller seller) {
        Answers.getInstance().logLogin(new LoginEvent()
                .putSuccess(true)
                .putCustomAttribute("Username", seller.getUsername()));
    }

    private void logFailLogin(String username, Exception e) {
        Answers.getInstance().logLogin(new LoginEvent()
                .putSuccess(false)
                .putCustomAttribute("Username", username)
                .putCustomAttribute("Exception", e.getMessage()));
    }

    private void cacheLoggedInUserData(Seller seller) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriAdapter())
                .create();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOGGED_IN_USER_DATA, gson.toJson(seller));
        editor.apply();
    }

    /**
     * Send Firebase token to server after user login
     */
    private void sendFirebaseTokenToServer() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String refreshedToken = sharedPreferences.getString("firebase_token", "");

        final ILoginRequest loginRequest = new LoginRequest(this);

        loginRequest.sendRegistrationTokenToServer(refreshedToken, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Log.d(TAG, "Success: " + result);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Failed to send token to server: " + e.getMessage());

            }
        });
    }
}
