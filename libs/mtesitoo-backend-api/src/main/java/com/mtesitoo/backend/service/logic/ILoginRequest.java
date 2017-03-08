package com.mtesitoo.backend.service.logic;

/**
 * Service for getting an OAuth token for Accessing the Api.
 *
 * @author danieldanciu
 */
public interface ILoginRequest {
    void getAuthToken(ICallback<String> callback);

    void authenticateUser(String username, String password, ICallback<String> callback);
    void authenticateUser(final String code, ICallback<String> callback);
}