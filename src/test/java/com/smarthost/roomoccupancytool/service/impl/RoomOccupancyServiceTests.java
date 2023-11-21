package com.smarthost.roomoccupancytool.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthost.roomoccupancytool.FileLoaderTestUtil;
import com.smarthost.roomoccupancytool.model.RoomOccupancyModel;
import com.smarthost.roomoccupancytool.model.RoomOccupancyResult;
import com.smarthost.roomoccupancytool.service.RoomOccupancyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomOccupancyServiceTests {

    private final RoomOccupancyService roomOccupancyService = new RoomOccupancyServiceImpl();
    private static final String POTENTIAL_GUESTS_JSON_FILE = "smarthost_hotel_guests.json";
    private static int[] potentialGuests;

    @BeforeAll
    public static void setUp() throws Exception {
        potentialGuests = new ObjectMapper().readValue(
                FileLoaderTestUtil.loadResourceFile(POTENTIAL_GUESTS_JSON_FILE), int[].class);
    }

    @Test
    public void shouldProperlyCalculateWhenSmallNumberOfRooms() {
        int freePremiumRooms = 3;
        int freeEconomyRooms = 3;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, potentialGuests));

        assertEquals(3, actual.usagePremium());
        assertEquals(3, actual.usageEconomy());
        assertEquals(738, actual.revenuePremium());
        assertEquals(167, actual.revenueEconomy());
    }

    @Test
    public void shouldProperlyCalculateWhenBigNumberOfRooms() {
        int freePremiumRooms = 7;
        int freeEconomyRooms = 5;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, potentialGuests));

        assertEquals(6, actual.usagePremium());
        assertEquals(4, actual.usageEconomy());
        assertEquals(1054, actual.revenuePremium());
        assertEquals(189, actual.revenueEconomy());
    }

    @Test
    public void shouldProperlyCalculateWhenSmallNumberPremiumRooms() {
        int freePremiumRooms = 2;
        int freeEconomyRooms = 7;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, potentialGuests));

        assertEquals(2, actual.usagePremium());
        assertEquals(4, actual.usageEconomy());
        assertEquals(583, actual.revenuePremium());
        assertEquals(189, actual.revenueEconomy());
    }

    @Test
    public void shouldProperlyCalculateWhenBigNumberPremiumRooms() {
        int freePremiumRooms = 10;
        int freeEconomyRooms = 1;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, potentialGuests));

        assertEquals(7, actual.usagePremium());
        assertEquals(1, actual.usageEconomy());
        assertEquals(1153, actual.revenuePremium());
        assertEquals(45, actual.revenueEconomy());
    }

    /**
     * Added this unit test, because I can imagine wrongly implement algorithm when economy guest is upgraded to
     * premium when premium room is available and all economy guests are assigned to economy rooms. Wrong algorithm
     * can return usagePremium 7 and usageEconomy 3.
     */
    @Test
    public void shouldProperlyCalculateWhenEconomyRoomsEqualToEconomyGuests() {
        int freePremiumRooms = 10;
        int freeEconomyRooms = 4;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, potentialGuests));

        assertEquals(6, actual.usagePremium());
        assertEquals(4, actual.usageEconomy());
        assertEquals(1054, actual.revenuePremium());
        assertEquals(189, actual.revenueEconomy());
    }

    @Test
    public void shouldNotThrownExceptionWhenNoPremiumGuestsIncluded() {
        int freePremiumRooms = 10;
        int freeEconomyRooms = 4;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, IntStream.of(10, 20, 30).toArray()));

        assertEquals(0, actual.usagePremium());
        assertEquals(3, actual.usageEconomy());
        assertEquals(0, actual.revenuePremium());
        assertEquals(60, actual.revenueEconomy());
    }

    @Test
    public void shouldNotThrownExceptionWhenNoEconomyGuestsIncluded() {
        int freePremiumRooms = 10;
        int freeEconomyRooms = 4;

        RoomOccupancyResult actual = roomOccupancyService.calculate(new RoomOccupancyModel(
                freePremiumRooms, freeEconomyRooms, IntStream.of(299, 102).toArray()));

        assertEquals(2, actual.usagePremium());
        assertEquals(0, actual.usageEconomy());
        assertEquals(401, actual.revenuePremium());
        assertEquals(0, actual.revenueEconomy());
    }
}
