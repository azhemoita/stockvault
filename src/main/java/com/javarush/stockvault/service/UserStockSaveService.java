package com.javarush.stockvault.service;

import com.javarush.stockvault.dto.MassiveAggregatesResponse;
import com.javarush.stockvault.entity.Ticker;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.entity.UserStockPrice;
import com.javarush.stockvault.massive.MassiveClient;
import com.javarush.stockvault.repository.TickerRepository;
import com.javarush.stockvault.repository.UserRepository;
import com.javarush.stockvault.repository.UserStockPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserStockSaveService {

    private final UserRepository userRepository;
    private final TickerRepository tickerRepository;
    private final UserStockPriceRepository userStockPriceRepository;
    private final MassiveClient massiveClient;

    public List<LocalDate> calculateMissingDates(String userEmail, String tickerSymbol, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start date is after end date");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Ticker ticker = tickerRepository.findBySymbol(tickerSymbol)
                .orElseThrow(() -> new IllegalArgumentException("Unknown ticker"));

        Set<LocalDate> existingDates = userStockPriceRepository.findExistingDates(user.getId(), ticker.getId(), start, end);

        List<LocalDate> missingDates = new ArrayList<>();

        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (!existingDates.contains(current)) {
                missingDates.add(current);
            }
            current = current.plusDays(1);
        }
        return missingDates;
    }

    @Transactional
    public void saveStockPrices(String userEmail, String tickerSymbol, LocalDate start, LocalDate end) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Ticker ticker = tickerRepository.findBySymbol(tickerSymbol).orElseThrow();

        List<LocalDate> missingDates = calculateMissingDates(userEmail, tickerSymbol, start, end);

        if (missingDates.isEmpty()) {
            return;
        }

        MassiveAggregatesResponse response = massiveClient.getDailyAggregates(tickerSymbol, start, end);

        for (MassiveAggregatesResponse.Result r : response.getResults()) {

            LocalDate date = Instant.ofEpochMilli(r.getT())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (!missingDates.contains(date)) {
                continue;
            }

            UserStockPrice usp = new UserStockPrice();
            usp.setUser(user);
            usp.setTicker(ticker);
            usp.setDate(date);
            usp.setOpen(r.getO());
            usp.setClose(r.getC());
            usp.setHigh(r.getH());
            usp.setLow(r.getL());
            usp.setVolume(r.getV());

            userStockPriceRepository.save(usp);
        }
    }

}
