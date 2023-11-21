package com.smarthost.roomoccupancytool.service.impl;

import com.smarthost.roomoccupancytool.model.RoomOccupancyModel;
import com.smarthost.roomoccupancytool.model.RoomOccupancyResult;
import com.smarthost.roomoccupancytool.service.RoomOccupancyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RoomOccupancyServiceImpl implements RoomOccupancyService {

    private static final int PREMIUM_MONEY_SEPARATOR = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomOccupancyServiceImpl.class);
    @Override
    public RoomOccupancyResult calculate(final RoomOccupancyModel roomOccupancyModel) {
        LOGGER.info("entry calculate method with the following input data {}", roomOccupancyModel.toString());

        boolean premiumEligible = true;
        boolean economyEligible = false;
        Map<Boolean, List<Integer>> split = IntStream.of(roomOccupancyModel.potentialGuests())
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.partitioningBy(i -> i >= PREMIUM_MONEY_SEPARATOR));

        AtomicInteger usagePremium = new AtomicInteger();
        AtomicInteger revenuePremium = new AtomicInteger();

        split.get(premiumEligible).stream()
                .limit(roomOccupancyModel.freePremiumRooms())
                .forEach(i -> {
                    usagePremium.incrementAndGet(); revenuePremium.addAndGet(i);
                });

        if (upgradeCondition(usagePremium.get(), roomOccupancyModel.freePremiumRooms(),
                split.get(economyEligible).size(), roomOccupancyModel.freeEconomyRooms())) {
            LOGGER.trace("conditions are met successfully to allow one guest get premium room instead of economy");
            usagePremium.incrementAndGet();
            revenuePremium.addAndGet(split.get(economyEligible).getFirst());
            split.get(economyEligible).removeFirst();
        }

        AtomicInteger usageEconomy = new AtomicInteger();
        AtomicInteger revenueEconomy = new AtomicInteger();
        split.get(economyEligible).stream()
                .limit(roomOccupancyModel.freeEconomyRooms())
                .forEach(i -> {
                    usageEconomy.incrementAndGet(); revenueEconomy.addAndGet(i);
                });

        return new RoomOccupancyResult(usagePremium.get(), usageEconomy.get(),
                revenuePremium.get(), revenueEconomy.get());
    }

    private boolean upgradeCondition(final int usagePremium,
                                     final int freePremium, final int eligibleEconomy, final int freeEconomy) {
       return usagePremium < freePremium && eligibleEconomy > freeEconomy;
    }
}
