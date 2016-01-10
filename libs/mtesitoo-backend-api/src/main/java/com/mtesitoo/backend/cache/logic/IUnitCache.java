package com.mtesitoo.backend.cache.logic;

import com.mtesitoo.backend.model.Unit;

import java.util.List;

/**
 * Created by Nan on 1/9/2016.
 */
public interface IUnitCache {
    void storeLengthUnits(List<Unit> units);

    void storeWeightUnits(List<Unit> units);

    List<Unit> getLengthUnits();

    List<Unit> getWeightUnits();
}
