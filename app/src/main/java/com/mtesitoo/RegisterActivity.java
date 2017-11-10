package com.mtesitoo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.RegistrationRequest;
import com.mtesitoo.backend.service.VendorTermsRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IRegistrationRequest;
import com.mtesitoo.backend.service.logic.IVendorTerms;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AbstractLoginActivity {

    final String TAG = "RegisterActivity";

    String password;
    String confirmPassword;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;

    @BindView(R.id.registration_terms)
    CheckBox termsCheckbox;

    boolean termsAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.register_user_button)
    void registrationAttempt() {

        showLoginProgress(null);

        password = ((EditText) findViewById(R.id.registration_password)).getText().toString();
        confirmPassword = ((EditText) findViewById(R.id.registration_confirm_password)).getText().toString();
        email = ((EditText) findViewById(R.id.registration_email)).getText().toString();
        firstName = ((EditText) findViewById(R.id.registration_firstname)).getText().toString();
        lastName = ((EditText) findViewById(R.id.registration_lastname)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.registration_phone)).getText().toString();
        termsAccepted = termsCheckbox.isChecked();

        if (verifyInput()) {
            registerUser();
        } else {
            dismissLoginProgress();
        }
    }

    @OnClick(R.id.cancel_registration_button)
    void cancel() {
        finish();
    }

    @OnClick(R.id.registration_view_terms)
    void showToC() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        termsCheckbox.setChecked(true);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        final TextView textView = new TextView(this);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setPadding(50, 40, 50, 0);

        IVendorTerms vendorTerms = new VendorTermsRequest(this);
        vendorTerms.getVendorTerms(new ICallback<String>() {
            @Override
            public void onResult(String result) {
                //Show TOCs in dialog
                String title = "Terms and Conditions";
                String description = "";

                try {
                    JSONObject tocObj = new JSONObject(result);
                    title = tocObj.getString("title");
                    description = tocObj.getString("description");

                    textView.setText(Html.fromHtml(description));
                    builder.setView(textView);
                    builder.setTitle(title)
                            .setPositiveButton("Agree", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();
                } catch (JSONException e) {
                    description = "Error occurred. Try Again later.";
                    Log.e(TAG, description + " : " + e.getMessage());
                    builder.setTitle(title)
                            .setMessage(description)
                            .setNegativeButton("Cancel", dialogClickListener).show();
                }
            }

            @Override
            public void onError(Exception e) {
                String errMsg = "Failed to fetch Terms and Conditions. Please try again later.";
                Log.e(TAG, errMsg + e.getMessage());
                builder.setTitle("Terms and Conditions")
                        .setMessage(errMsg)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }
        });
    }

    private void registerUser() {

        final Seller seller = new Seller(0, email, firstName, lastName,
                phoneNumber, email, " ", "", "", "",
                "", "", "", password, "", "1", "", "");

        IRegistrationRequest registrationService = new RegistrationRequest(this);
        registrationService.submitSeller(seller, new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                Toast.makeText(TesitooApplication.getInstance().getContext(), R.string.register_successful, Toast.LENGTH_LONG).show();

                final Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                logInUser(intent, seller.getUsername(), seller.getmPassword(), false);
            }

            @Override
            public void onError(Exception e) {
                dismissLoginProgress();

                VolleyError err = (VolleyError) e;

                String errorMsg = "";
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
                        jErr.printStackTrace();
                    }
                }

                if (errorMsg.equals("")) {
                    errorMsg = "Error registering account";
                }

                displayToast(errorMsg);
            }
        });
    }

    private boolean verifyInput() {

        boolean passwordsMatch = password.equalsIgnoreCase(confirmPassword);

        if (!noEmptyFieds()) {
            displayToast("All fields are required");
            return false;
        }

        if (!termsAccepted) {
            displayToast(getString(R.string.registration_terms_error));
            return false;
        }

        if (!passwordValid()) {
            displayToast("Password must be greater than 5 characters long");
            return false;
        }

        if (!passwordsMatch) {
            displayToast("Passwords entered must match");
            return false;
        }

        if (!isValidEmailAddress()) {
            displayToast("Email address is not valid");
            return false;
        }

        return true;
    }

    private boolean isValidEmailAddress() {
        email = email.trim();
        return true;
    }

    private boolean passwordValid() {
        return password.length() > 5;
    }

    private boolean noEmptyFieds() {

        firstName = firstName.trim();
        lastName = lastName.trim();
        phoneNumber = phoneNumber.trim();

        return !firstName.equals("")
                && !lastName.equals("")
                && !phoneNumber.equals("");
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
