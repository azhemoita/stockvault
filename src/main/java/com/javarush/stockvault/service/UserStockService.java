package com.javarush.stockvault.service;

import com.javarush.stockvault.dto.SavedStockResponse;
import com.javarush.stockvault.dto.StockPriceResponse;
import com.javarush.stockvault.entity.Stock;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.repository.StockPriceRepository;
import com.javarush.stockvault.repository.StockRepository;
import com.javarush.stockvault.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserStockService {
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;

    public SavedStockResponse getSavedStock(String userEmail, String ticker) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stock stock = stockRepository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("Ticker not found"));

        List<StockPriceResponse> data = stockPriceRepository.findByStockOrderByDateAsc(stock)
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
                stock.getId(),
                stock.getTicker(),
                data
        );
    }
}
