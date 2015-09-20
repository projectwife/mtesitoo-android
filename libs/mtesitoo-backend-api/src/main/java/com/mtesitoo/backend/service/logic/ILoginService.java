package com.mtesitoo.backend.service.logic;

/**
 * Service for getting an OAuth token for Accessing the Api.
 *
 * @author danieldanciu
 */
public interface ILoginService {
    void getAuthToken(final IResponse<String> callback);
}