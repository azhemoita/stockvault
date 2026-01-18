package com.javarush.stockvault.controller;

import com.javarush.stockvault.dto.SavedStockResponse;
import com.javarush.stockvault.dto.StockHistoryRequest;
import com.javarush.stockvault.service.UserStockSaveService;
import com.javarush.stockvault.service.UserStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserStockController {

    private final UserStockService service;
    private final UserStockSaveService userStockSaveService;

    @GetMapping("/saved")
    public SavedStockResponse getSaved(@RequestParam String ticker,
                                       Authentication authentication) {
        return service.getSavedStock(
                authentication.getName(),
                ticker
        );
    }

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
