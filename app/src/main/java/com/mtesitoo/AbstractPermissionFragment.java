package com.mtesitoo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/* Base Activity to handle run-time permissions at one place.
 * Credits: @commonsware
 */

abstract public class AbstractPermissionFragment
        extends Fragment {
    abstract protected String[] getDesiredPermissions();
    abstract protected void onPermissionDenied();
    abstract protected void onReady(Bundle state);

    private static final int REQUEST_PERMISSION=61125;
    private static final String STATE_IN_PERMISSION="inPermission";
    private boolean isInPermission=false;
    private Bundle state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.state=savedInstanceState;

        if (state!=null) {
            isInPermission=state.getBoolean(STATE_IN_PERMISSION, false);
        }

        if (hasAllPermissions(getDesiredPermissions())) {
            onReady(state);
        }
        else if (!isInPermission) {
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        isInPermission=false;

        if (requestCode==REQUEST_PERMISSION) {
            if (hasAllPermissions(getDesiredPermissions())) {
                onReady(state);
            }
            else {
                onPermissionDenied();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_IN_PERMISSION, isInPermission);
    }

    /**
     * Shows dialog to request permissions.
     */
    public void requestPermissions() {
        isInPermission=true;

        ActivityCompat
                .requestPermissions(getActivity(),
                        netPermissions(getDesiredPermissions()),
                        REQUEST_PERMISSION);
    }

    /**
     * Checks if all required permissions are available
     * @return
     */
    public boolean isReady() {
        if (hasAllPermissions(getDesiredPermissions())) {
            return true;
        }

        return false;
    }

    private boolean hasAllPermissions(String[] perms) {
        for (String perm : perms) {
            if (!hasPermission(perm)) {
                return(false);
            }
        }

        return(true);
    }

    private boolean hasPermission(String perm) {
        return(ContextCompat.checkSelfPermission(getActivity(), perm)==
                PackageManager.PERMISSION_GRANTED);
    }

    private String[] netPermissions(String[] wanted) {
        ArrayList<String> result=new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return(result.toArray(new String[result.size()]));
    }
}