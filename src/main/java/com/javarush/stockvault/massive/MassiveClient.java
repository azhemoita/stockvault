package com.javarush.stockvault.massive;

import com.javarush.stockvault.dto.MassiveAggregatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
public class MassiveClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${massive.api.key}")
    private String apiKey;

    public MassiveAggregatesResponse getDailyAggregates(
            String ticker,
            LocalDate start,
            LocalDate end
    ) {
        String url = String.format(
                "https://api.massive.com/v2/aggs/ticker/%s/range/1/day/%s/%s" +
                "?adjusted=true&sort=asc&limit=500&apiKey=%s",
                ticker,
                start,
                end,
                apiKey
        );

        return restTemplate.getForObject(url, MassiveAggregatesResponse.class);
    }
}
