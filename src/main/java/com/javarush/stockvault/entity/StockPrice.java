package com.javarush.stockvault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "stock_prices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"stock_id", "date"}))
@Getter
@Setter
@NoArgsConstructor
public class StockPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double open;

    @Column(nullable = false)
    private double close;

    @Column(nullable = false)
    private double high;

    @Column(nullable = false)
    private double low;

    @Column(nullable = false)
    private long volume;

}
