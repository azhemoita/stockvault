package com.javarush.stockvault.repository;

import com.javarush.stockvault.entity.Stock;
import com.javarush.stockvault.entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    List<StockPrice> findByStockAndDateBetween(Stock stock, LocalDate from, LocalDate to);

    List<StockPrice> findByStockOrderByDateAsc(Stock stock);
}
