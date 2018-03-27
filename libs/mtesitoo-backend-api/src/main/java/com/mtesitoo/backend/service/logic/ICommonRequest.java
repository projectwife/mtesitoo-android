package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Unit;

import java.util.List;

/**
 * Created by Nan on 1/9/2016.
 */
public interface ICommonRequest {
    void getLengthUnits(ICallback<List<Unit>> callback);

    void getWeightUnits(ICallback<List<Unit>> callback);

    void getUnits(ICallback<List<Unit>> callback);
}
