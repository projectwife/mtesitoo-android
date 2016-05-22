package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Zone;

import java.util.List;

/**
 * Created by Samuel on 2016/5/11 0011.
 */
public interface IZoneRequest {
    void getZones(ICallback<List<Zone>> callback);
}
