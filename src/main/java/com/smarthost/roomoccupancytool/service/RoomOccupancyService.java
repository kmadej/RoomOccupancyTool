package com.smarthost.roomoccupancytool.service;

import com.smarthost.roomoccupancytool.model.RoomOccupancyModel;
import com.smarthost.roomoccupancytool.model.RoomOccupancyResult;

public interface RoomOccupancyService {
    RoomOccupancyResult calculate(RoomOccupancyModel roomOccupancyModel);
}
