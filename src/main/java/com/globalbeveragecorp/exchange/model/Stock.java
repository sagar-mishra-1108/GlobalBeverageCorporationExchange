package com.globalbeveragecorp.exchange.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Stock {
    private String symbol;
    private StockType type;
    private double lastDividend;
    private double fixedDividend;
    private double parValue;
}
