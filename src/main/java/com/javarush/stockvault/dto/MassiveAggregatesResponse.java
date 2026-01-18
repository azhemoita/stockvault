package com.javarush.stockvault.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MassiveAggregatesResponse {

    private List<Result> results;

    @Getter
    public static class Result {
        private long t;
        private double o;
        private double c;
        private double h;
        private double l;
        private long v;
    }

}
