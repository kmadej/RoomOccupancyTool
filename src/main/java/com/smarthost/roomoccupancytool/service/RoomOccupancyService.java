package com.smarthost.roomoccupancytool.service;

import com.smarthost.roomoccupancytool.model.RoomOccupancyModel;
import com.smarthost.roomoccupancytool.model.RoomOccupancyResult;

public interface RoomOccupancyService {
    /**
     * Calculates room occupancy according to data from model.
     *
     * @param roomOccupancyModel input data with potential guests and free rooms
     * @return value of occupied number of rooms with revenue
     */
    RoomOccupancyResult calculate(RoomOccupancyModel roomOccupancyModel);
}
