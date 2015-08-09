package com.mtesitoo.backend.logic;

import com.mtesitoo.backend.api.Callback;

/**
 * Service for getting an OAuth token for Accessing the Api.
 *
 * @author danieldanciu
 */
public interface LoginService {
    void getAuthToken(final Callback<String> callback);
}
