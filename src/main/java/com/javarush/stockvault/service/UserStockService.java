package com.javarush.stockvault.service;

import com.javarush.stockvault.dto.SavedStockResponse;
import com.javarush.stockvault.dto.StockPriceResponse;
import com.javarush.stockvault.entity.Ticker;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserStockService {
    private final UserRepository userRepository;
    private final TickerRepository tickerRepository;
    private final UserStockPriceRepository userStockPriceRepository;

    public SavedStockResponse getSavedStock(String userEmail, String tickerSymbol) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticker ticker = tickerRepository.findBySymbol(tickerSymbol)
                .orElseThrow(() -> new RuntimeException("Ticker not found"));

        List<StockPriceResponse> data = userStockPriceRepository
                .findByUserIdAndTickerIdOrderByDateAsc(user.getId(), ticker.getId())
                .stream()
                .map(p -> new StockPriceResponse(
                        p.getDate(),
                        p.getOpen(),
                        p.getClose(),
                        p.getHigh(),
                        p.getLow(),
                        p.getVolume()
                )).toList();

        return new SavedStockResponse(
                ticker.getId(),
                ticker.getSymbol(),
                data
        );
    }
}
