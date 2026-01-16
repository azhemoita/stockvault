package com.javarush.stockvault.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class StockPriceResponse {
    private LocalDate date;
    private double open;
    private double close;
    private double high;
    private double low;
    private long volume;
}
