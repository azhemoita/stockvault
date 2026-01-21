package com.javarush.stockvault.controller;

import com.javarush.stockvault.dto.SavedStockResponse;
import com.javarush.stockvault.dto.StockHistoryRequest;
import com.javarush.stockvault.service.UserStockSaveService;
import com.javarush.stockvault.service.UserStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Stocks",
        description = "Operations for importing stock data from Massive and retrieving saved user stocks"
)
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserStockController {

    private final UserStockService service;
    private final UserStockSaveService userStockSaveService;

    @Operation(summary = "Get saved stock prices by ticker for the authenticated user")
    @GetMapping("/saved")
    public SavedStockResponse getSaved(@RequestParam String ticker,
                                       Authentication authentication) {
        return service.getSavedStock(
                authentication.getName(),
                ticker
        );
    }

    @Operation(summary = "Import and save stock prices for the authenticated user")
    @PostMapping("/save")
    public ResponseEntity<Void> saveStock(
            @Valid @RequestBody StockHistoryRequest request,
            Authentication authentication
            ) {
        userStockSaveService.saveStockPrices(
                authentication.getName(),
                request.getTicker(),
                request.getStart(),
                request.getEnd()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
