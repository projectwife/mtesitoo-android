package com.mtesitoo.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by ptyagi on 4/3/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public final String FIREBASE_TOKEN_KEY = "firebase_token";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Saves token in SharePreferences. Once login is successful, it will be sent over to server.
     * @param refreshedToken
     */
    private void sendRegistrationToServer(String refreshedToken) {
        SharedPreferences mPrefs = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPrefs.edit();

        mEditor.putString(FIREBASE_TOKEN_KEY, refreshedToken);
        mEditor.commit();
    }
}
