package com.smarthost.roomoccupancytool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomOccupancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Value("classpath:smarthost_hotel_guests.json")
    private Resource resourceFile;

    @Test
    public void shouldReturn200WhenProvideAllParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/roomOccupancy")
                        .param("freePremiumRooms", "3")
                        .param("freeEconomyRooms", "3")
                        .param("potentialGuests", "1,2,3")
                .with(user(username).password(password))).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturn400WhenLackOfOneParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/roomOccupancy")
                        .param("freePremiumRooms", "3")
                        .param("potentialGuests", "1,2,3")
                        .with(user(username).password(password))).
                andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnProperResponseValuesWhenProvideAllParams() throws Exception {
        Integer[] potentialGuests = new ObjectMapper().readValue(resourceFile.getFile(), Integer[].class);
        String potentialGuestsValue = StringUtils.arrayToCommaDelimitedString(potentialGuests);

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/roomOccupancy")
                        .param("freePremiumRooms", "3")
                        .param("freeEconomyRooms", "3")
                        .param("potentialGuests", potentialGuestsValue)
                        .with(user(username).password(password)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.usagePremium").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.usageEconomy").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.revenuePremium").value("738"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.revenueEconomy").value("167"));
    }
}
