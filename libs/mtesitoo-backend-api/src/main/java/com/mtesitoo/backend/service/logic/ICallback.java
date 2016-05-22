package com.mtesitoo.backend.service.logic;

/**
 * Callback for network requests
 */
public interface ICallback<T> {
    void onResult(T result);


    void onError(Exception e);
}