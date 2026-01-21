package com.javarush.stockvault.service;

import com.javarush.stockvault.entity.Ticker;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.repository.TickerRepository;
import com.javarush.stockvault.repository.UserRepository;
import com.javarush.stockvault.repository.UserStockPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserStockSaveServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TickerRepository tickerRepository;

    @Mock
    UserStockPriceRepository userStockPriceRepository;

    @InjectMocks
    UserStockSaveService userStockSaveService;

    @Test
    void calculateMissingDates_returnsOnlyDatesNotInDatabase() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        Ticker ticker = new Ticker();
        ticker.setId(2L);
        ticker.setSymbol("AAPL");

        LocalDate start = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2022, 1, 3);

        Set<LocalDate> existingDates = Set.of(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 2)
        );

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(java.util.Optional.of(user));
        when(tickerRepository.findBySymbol("AAPL"))
                .thenReturn(java.util.Optional.of(ticker));
        when(userStockPriceRepository.findExistingDates(
                1L,
                2L,
                start,
                end
        )).thenReturn(existingDates);

        var result = userStockSaveService.calculateMissingDates(
                "test@mail.com",
                "AAPL",
                start,
                end
        );

        assertThat(result)
                .containsExactly(LocalDate.of(2022, 1, 3));
    }
}
