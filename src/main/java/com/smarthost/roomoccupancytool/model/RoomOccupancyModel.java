package com.smarthost.roomoccupancytool.model;

public record RoomOccupancyModel(int freePremiumRooms, int freeEconomyRooms, int[] potentialGuests) {
}
