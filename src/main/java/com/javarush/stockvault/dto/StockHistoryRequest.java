package com.javarush.stockvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class StockHistoryRequest {

    @NotBlank
    private String ticker;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}
