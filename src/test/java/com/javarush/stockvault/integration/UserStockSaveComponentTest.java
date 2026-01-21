package com.javarush.stockvault.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.stockvault.dto.StockHistoryRequest;
import com.javarush.stockvault.entity.Ticker;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.repository.TickerRepository;
import com.javarush.stockvault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
@Transactional
public class UserStockSaveComponentTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TickerRepository tickerRepository;

    @Test
    @WithMockUser(
            username = "test@mail.com",
            roles = "USER"
    )
    void saveStock_importsDataFromMassiveAndStoresInDb() throws Exception {

        stubFor(get(urlMatching("/v2/aggs/ticker/AAPL/range/1/day/.*"))
                .willReturn(okJson("""
                        {
                            "results": [
                                {
                                    "t": 1640995200000,
                                    "o": 100.0,
                                    "c": 110.0,
                                    "h": 115.0,
                                    "l": 95.0,
                                    "v": 1000
                                }
                            ]
                        }
                        """)));

        StockHistoryRequest request = new StockHistoryRequest();
        request.setTicker("AAPL");
        request.setStart(java.time.LocalDate.of(2022, 1, 1));
        request.setEnd(java.time.LocalDate.of(2022, 1, 1));

        User user = new User();
        user.setEmail("test@mail.com");
        user.setUsername("test");
        user.setPassword("password");
        user.setRole("USER");

        userRepository.save(user);

        Ticker ticker = new Ticker();
        ticker.setSymbol("AAPL");

        tickerRepository.save(ticker);

        mockMvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }
}
