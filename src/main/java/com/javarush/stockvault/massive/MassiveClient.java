package com.javarush.stockvault.massive;

import com.javarush.stockvault.dto.MassiveAggregatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MassiveClient {

    @Value("${massive.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Value("${massive.api.key}")
    private String apiKey;

    public MassiveAggregatesResponse getDailyAggregates(
            String ticker,
            LocalDate start,
            LocalDate end
    ) {
        String url = String.format(
                "%s/v2/aggs/ticker/%s/range/1/day/%s/%s?adjusted=true&sort=asc&limit=500&apiKey=%s",
                baseUrl,
                ticker,
                start,
                end,
                apiKey
        );

        return restTemplate.getForObject(url, MassiveAggregatesResponse.class);
    }
}
