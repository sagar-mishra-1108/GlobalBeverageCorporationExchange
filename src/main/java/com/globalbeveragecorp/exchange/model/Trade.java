package com.globalbeveragecorp.exchange.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Trade {
    private Stock stock;
    private LocalDateTime timestamp;
    private int quantity;
    private TradeType tradeType;
    private double price;
}
