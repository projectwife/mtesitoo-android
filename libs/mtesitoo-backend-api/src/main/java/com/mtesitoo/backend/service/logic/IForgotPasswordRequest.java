package com.mtesitoo.backend.service.logic;

/**
 * Created by pritya on 2/21/2017
 */
public interface IForgotPasswordRequest {
    void forgotPassword(final String username, final ICallback<String> callback);
}