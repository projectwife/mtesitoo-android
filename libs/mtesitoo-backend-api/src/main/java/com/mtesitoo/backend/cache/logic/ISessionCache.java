package com.mtesitoo.backend.cache.logic;

/**
 * Created by Nan on 12/19/2015.
 */
public interface ISessionCache {
    void storeSession(String sessionKey);

    String getSession();
}
