
package com.mtesitoo.backend.cache.logic;

import com.mtesitoo.backend.model.Zone;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public interface IZonesCache {

    void storeZones(List<Zone> zones);

    List<Zone> GetZones();
}
