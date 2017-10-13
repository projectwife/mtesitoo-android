package com.mtesitoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtesitoo.backend.cache.AuthorizationCache;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.CountriesCache;
import com.mtesitoo.backend.cache.UnitCache;
import com.mtesitoo.backend.cache.logic.IAuthorizationCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.cache.logic.ICountriesCache;
import com.mtesitoo.backend.cache.logic.IUnitCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.backend.service.CategoryRequest;
import com.mtesitoo.backend.service.CommonRequest;
import com.mtesitoo.backend.service.CountriesRequest;
import com.mtesitoo.backend.service.ForgotPasswordRequest;
import com.mtesitoo.backend.service.LoginRequest;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICommonRequest;
import com.mtesitoo.backend.service.logic.ICountriesRequest;
import com.mtesitoo.backend.service.logic.IForgotPasswordRequest;
import com.mtesitoo.backend.service.logic.ILoginRequest;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.helper.UriAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    protected SharedPreferences.Editor mEditor;
    protected SharedPreferences mPrefs;
    String[] zonesNames;

    @BindView(R.id.user_name)
    TextView mUsername;

    @BindView(R.id.password)
    TextView mPassword;

    @BindView(R.id.loading_progress_container)
    View mLoadingViewContainer;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login: {
                final Intent intent = new Intent(this, HomeActivity.class);
                logInUser(intent, null, null, false);
                break;
            }

            case R.id.newUser: {
                //Todo: allow user to pick between seller and buyer profiles

                final Intent intent = new Intent(mContext, RegisterActivity.class);
                mContext.startActivity(intent);

                break;
            }

            case R.id.forgotPassword: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter your registered email address");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //send request to forgot password API
                        String username = input.getText().toString();
                        if (!username.isEmpty()) {
                            forgotPassword(username);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            }
        }
    }

    private void forgotPassword(final String username) {
        final IForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(mContext);
        forgotPasswordRequest.forgotPassword(username, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Toast.makeText(mContext, getString(R.string.forgot_password_request), Toast.LENGTH_LONG).show();
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
                            errorMsg = getString(R.string.forgot_password_request_error);
                        }
                    }
                }

                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Send Firebase token to server after user login
     */
    private void sendFirebaseTokenToServer() {
        SharedPreferences mPrefs = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String refreshedToken = mPrefs.getString("firebase_token", "");

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

    private void logInUser(final Intent intent, final String user, final String pass, final boolean resetPassword) {
        final ILoginRequest loginService = new LoginRequest(this);
        String username = null;
        String password = null;

        if (user == null || pass == null) {
            username = mUsername.getText().toString().trim();
            password = mPassword.getText().toString();
        } else {
            username = user;
            password = pass;
        }

        final String userId = username;
        final String userPass = password;

        final String token = password;

        showLoginProgress("Logging in");
        loginService.authenticateUser(username, token, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Log.d("LOGIN - RESULT", result);
                sendFirebaseTokenToServer();

                ICategoryRequest categoryService = new CategoryRequest(mContext);
                ISellerRequest sellerService = new SellerRequest(mContext);
                ICommonRequest commonService = new CommonRequest(mContext);

                commonService.getLengthUnits(new ICallback<List<Unit>>() {
                    @Override
                    public void onResult(List<Unit> units) {
                        IUnitCache cache = new UnitCache(mContext);
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
                        IUnitCache cache = new UnitCache(mContext);
                        cache.storeWeightUnits(units);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("WeightUnits", e.toString());
                    }
                });

                categoryService.getCategories(new ICallback<List<Category>>() {
                    @Override
                    public void onResult(List<Category> categories) {
                        ICategoryCache cache = new CategoryCache(mContext);
                        cache.storeCategories(categories);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Categories", e.toString());
                    }
                });

                sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Log.d("Login - Seller Info", result.toString());

                        logUser(result);
                        logSuccessLogin(result);

                        //Cache login state in sharedprefs
                        mEditor.putBoolean(Constants.IS_USER_LOGGED_IN_KEY, true);
                        mEditor.putString(Constants.LOGGED_IN_USER_ID_KEY, userId);
                        mEditor.putString(Constants.LOGGED_IN_USER_PASS_KEY, userPass);

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Uri.class, new UriAdapter())
                                .create();
                        mEditor.putString(Constants.LOGGED_IN_USER_DATA, gson.toJson(result));

                        mEditor.commit();

                        intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
                        intent.putExtra(mContext.getString(R.string.automatic_login_key), resetPassword);
                        if (resetPassword) {
                            intent.putExtra(mContext.getString(R.string.automatic_login_token), token);
                        }
                        mContext.startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("getSellerInfo", e.toString());
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("AuthenticateUser", e.toString());
                logFailLogin(mUsername.getText().toString(),e);
                String errorMessage = "";

                if (e.getMessage() != null) {
                    errorMessage = "Failed to log in due to an error: " +
                            e.getMessage() + ". Try again later.";
                } else {
                    errorMessage = "Failed to log in user !";
                }
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logInByCode(final Intent intent, final String code, final boolean resetPassword) {
        final ILoginRequest loginService = new LoginRequest(this);

        loginService.authenticateUser(code, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Log.d("LOGIN - RESULT", result);
                ICategoryRequest categoryService = new CategoryRequest(mContext);
                ISellerRequest sellerService = new SellerRequest(mContext);
                ICommonRequest commonService = new CommonRequest(mContext);

                commonService.getLengthUnits(new ICallback<List<Unit>>() {
                    @Override
                    public void onResult(List<Unit> units) {
                        IUnitCache cache = new UnitCache(mContext);
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
                        IUnitCache cache = new UnitCache(mContext);
                        cache.storeWeightUnits(units);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("WeightUnits", e.toString());
                    }
                });

                categoryService.getCategories(new ICallback<List<Category>>() {
                    @Override
                    public void onResult(List<Category> categories) {
                        ICategoryCache cache = new CategoryCache(mContext);
                        cache.storeCategories(categories);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Categories", e.toString());
                    }
                });

                sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Log.d("Login - Seller Info", result.toString());

                        logUser(result);
                        logSuccessLogin(result);
                        intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
                        intent.putExtra(mContext.getString(R.string.automatic_login_key), resetPassword);
                        if (resetPassword) {
                            intent.putExtra(mContext.getString(R.string.automatic_login_token), code);
                        }
                        mContext.startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("getSellerInfo", e.toString());
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("AuthenticateUser", e.toString());
                logFailLogin(mUsername.getText().toString(),e);
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        Button newUser = (Button) findViewById(R.id.newUser);
        newUser.setOnClickListener(this);
        Button forgotPassword = (Button) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
        mContext = this;

        //Handling password reset request
        Intent passwordResetIntent = getIntent();
        String action = passwordResetIntent.getAction();
        String data = passwordResetIntent.getDataString();

        //https://tesitoo.com/index.php?route=common/reset&code=84f6c6874e7ffd31fe3d84132c790824fc8b1024
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            if (data.contains("code")) {
                String code = data.substring(data.indexOf("&code=")+ "&code=".length());
                logInByCode(new Intent(this, HomeActivity.class), code, true);
            } else {
                Toast.makeText(mContext, "Broken Password reset link.", Toast.LENGTH_LONG).show();
            }
        }
        //ends here

        mPrefs = this.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        ICountriesRequest countriesService = new CountriesRequest(mContext);

        countriesService.getCountries(new ICallback<List<Countries>>() {
            @Override
            public void onResult(List<Countries> countries) {
                ICountriesCache cache = new CountriesCache(mContext);
                cache.storeCountries(countries);
                String s1 = "";

                for (Countries country : countries) {
                    s1 = s1 + country.getName();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Countries", e.toString());
            }
        });


        final ILoginRequest loginService = new LoginRequest(this);
        loginService.getAuthToken(new ICallback<String>() {
            @Override
            public void onResult(String result) {
                IAuthorizationCache authorizationCache = new AuthorizationCache(mContext);
                if (authorizationCache.getAuthorization() == null) {
                    authorizationCache.storeAuthorization(result);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Login", e.toString());
            }
        });

        //Automatically log-in for already logged-in user
        boolean isUserLoggedIn = mPrefs.getBoolean(Constants.IS_USER_LOGGED_IN_KEY, false);
        if (isUserLoggedIn) {
            showLoginProgress("Logging in");

            String username = mPrefs.getString(Constants.LOGGED_IN_USER_ID_KEY, "");
            String password = mPrefs.getString(Constants.LOGGED_IN_USER_PASS_KEY, "");
            logInUser(new Intent(this, HomeActivity.class), username, password, false);
        }
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

    private void showLoginProgress(String message) {
        TextView loadingText = (TextView) findViewById(R.id.loading_text);
        if (message != null) {
            loadingText.setText(message);
        }
        else {
            loadingText.setText("Loading");
        }

        mLoadingViewContainer.setVisibility(View.VISIBLE);
//        mLoginProgress = new ProgressDialog(this);
//        mLoginProgress.setMessage("Logging in");
//        mLoginProgress.show();
    }

    private void dismissLoginProgress() {
//        if (mLoginProgress != null) {
//            mLoginProgress.dismiss();
//        }
        mLoadingViewContainer.setVisibility(View.INVISIBLE);

    }

}