package com.smarthost.roomoccupancytool.controller;

import com.smarthost.roomoccupancytool.model.RoomOccupancyModel;
import com.smarthost.roomoccupancytool.model.RoomOccupancyResult;
import com.smarthost.roomoccupancytool.service.RoomOccupancyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoomOccupancyController {

    private final RoomOccupancyService roomOccupancyService;

    @GetMapping("/roomOccupancy")
    public RoomOccupancyResult getRoomOccupancy(@RequestParam int freePremiumRooms,
                                                @RequestParam int freeEconomyRooms,
                                                @RequestParam int[] potentialGuests) {
        return roomOccupancyService.calculate(
                new RoomOccupancyModel(freePremiumRooms, freeEconomyRooms, potentialGuests));
    }
}
