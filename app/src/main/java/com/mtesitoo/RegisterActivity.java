package com.mtesitoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.RegistrationRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IRegistrationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

    String password;
    String confirmPassword;
    String email;

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

                if(verifyInput()){
                    registerUser();
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

        final Seller seller = new Seller(0, email, "", "",
                "", email, email, "", "", "",
                "", "", "", password, "", "1", "", "");

        IRegistrationRequest registrationService = new RegistrationRequest(this);
        registrationService.submitSeller(seller, new ICallback<Seller>() {
            @Override
            public void onResult(Seller result) {
                Toast.makeText(RegisterActivity.this, R.string.register_successful, Toast.LENGTH_LONG).show();

                System.out.println("SUCCESS");
                //finish();
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
                            errorMsg = "Error registering account";
                        }
                    }
                }

            }
        });
    }

    // Todo: make this better
    private boolean verifyInput(){

        boolean passwordsMatch = password.equalsIgnoreCase(confirmPassword);

        if(!passwordsMatch){
            displayToast("Passwords entered must match");
            return false;
        }

        boolean isValidEmailAddress = true;

        if(!isValidEmailAddress){
            displayToast("Email address is not valid");
            return false;
        }

        return true;
    }

    private void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
