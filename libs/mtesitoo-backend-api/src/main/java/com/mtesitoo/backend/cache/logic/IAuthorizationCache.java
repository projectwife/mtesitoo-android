package com.mtesitoo.backend.cache.logic;

/**
 * Created by Nan on 10/11/2015.
 */
public interface IAuthorizationCache {
    void storeAuthorization(String authorization);

    String getAuthorization();
}
