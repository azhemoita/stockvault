package com.javarush.stockvault.controller;

import com.javarush.stockvault.dto.SavedStockResponse;
import com.javarush.stockvault.service.UserStockService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserStockController {

    private final UserStockService service;

    @GetMapping("/saved")
    public SavedStockResponse getSaved(@RequestParam String ticker,
                                       Authentication authentication) {
        return service.getSavedStock(
                authentication.getName(),
                ticker
        );
    }
}
