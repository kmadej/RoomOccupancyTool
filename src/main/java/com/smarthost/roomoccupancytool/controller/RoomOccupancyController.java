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

    /**
     * Gets optimise result how to split free rooms into potential guests and count revenue.
     *
     * @param freePremiumRooms number of free premium rooms in hotel
     * @param freeEconomyRooms number of free economy rooms in hotel
     * @param potentialGuests array of potential guests represented by money they can pay for room
     * @return result containing economy and premium usage rooms with revenue
     */
    @GetMapping("/roomOccupancy")
    public RoomOccupancyResult getRoomOccupancy(@RequestParam final int freePremiumRooms,
                                                @RequestParam final int freeEconomyRooms,
                                                @RequestParam final int[] potentialGuests) {
        return roomOccupancyService.calculate(
                new RoomOccupancyModel(freePremiumRooms, freeEconomyRooms, potentialGuests));
    }
}
