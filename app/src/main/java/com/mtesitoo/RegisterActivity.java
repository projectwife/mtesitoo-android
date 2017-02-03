package com.mtesitoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
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
import com.mtesitoo.backend.service.RegistrationRequest;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICommonRequest;
import com.mtesitoo.backend.service.logic.ILoginRequest;
import com.mtesitoo.backend.service.logic.IRegistrationRequest;
import com.mtesitoo.backend.service.logic.ISellerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    String password;
    String confirmPassword;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AppCompatButton registerButton = (AppCompatButton) findViewById(R.id.register_user_button);
        AppCompatButton cancelButton = (AppCompatButton) findViewById(R.id.cancel_registration_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = ((EditText)findViewById(R.id.registration_password)).getText().toString();
                confirmPassword = ((EditText)findViewById(R.id.registration_confirm_password)).getText().toString();
                email = ((EditText)findViewById(R.id.registration_email)).getText().toString();
                firstName = ((EditText)findViewById(R.id.registration_firstname)).getText().toString();
                lastName = ((EditText)findViewById(R.id.registration_lastname)).getText().toString();
                phoneNumber = ((EditText)findViewById(R.id.registration_phone)).getText().toString();

                if(verifyInput()){

                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choice) {
                            switch (choice) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    registerUser();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Terms and Conditions")
                            .setMessage("<< Placeholder >>")
                            .setPositiveButton("Agree", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void registerUser(){

        final Seller seller = new Seller(0, email, firstName, lastName,
                phoneNumber, email, firstName, "", "", "",
                "", "", "", password, "", "1", "", "");

        IRegistrationRequest registrationService = new RegistrationRequest(this);
        registrationService.submitSeller(seller, new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                Toast.makeText(TesitooApplication.getInstance().getContext(), R.string.register_successful, Toast.LENGTH_LONG).show();
                startNewLogin(seller,RegisterActivity.this);
                finish();
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
                    }
                }
                if(errorMsg.equals("")){
                    errorMsg = "Error registering account";
                    displayToast(errorMsg);
                }

            }
        });
    }

    private boolean verifyInput(){

        boolean passwordsMatch = password.equalsIgnoreCase(confirmPassword);

        if(!noEmptyFieds()){
            displayToast("All fields are required");
            return false;
        }

        if(!passwordValid()){
            displayToast("Password must be greater than 5 characters long");
            return false;
        }

        if(!passwordsMatch){
            displayToast("Passwords entered must match");
            return false;
        }

        if(!isValidEmailAddress()){
            displayToast("Email address is not valid");
            return false;
        }

        return true;
    }

    private boolean isValidEmailAddress(){
        email = email.trim();
        return true;
    }

    private boolean passwordValid(){
        return password.length() > 5;
    }

    private boolean noEmptyFieds(){

        firstName = firstName.trim();
        lastName = lastName.trim();
        phoneNumber = phoneNumber.trim();

        return  !firstName.equals("")
                && !lastName.equals("")
                && !phoneNumber.equals("");
    }

    private void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public void startNewLogin(Seller seller, final Context mContext) {
        final Intent intent = new Intent(this, HomeActivity.class);
        final ILoginRequest loginService = new LoginRequest(this);

        loginService.authenticateUser(seller.getUsername(), seller.getmPassword(), new ICallback<String>() {
            @Override
            public void onResult(String result) {
                Log.d("LOGIN RESULT", result);
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
                    }
                });

                sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {
                        Log.d("getSellerInfo", result.toString());
                        logUser(result);
                        intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
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
                Log.e("authenticateUser", e.toString());
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void logUser(Seller seller) {
        Crashlytics.setUserIdentifier(String.valueOf(seller.getId()));
        Crashlytics.setUserEmail(seller.getmEmail());
        Crashlytics.setUserName(seller.getUsername());
    }
}
