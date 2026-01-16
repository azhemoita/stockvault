package com.javarush.stockvault.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SavedStockResponse {
    private Long id;
    private String ticker;
    private List<StockPriceResponse> data;
}
