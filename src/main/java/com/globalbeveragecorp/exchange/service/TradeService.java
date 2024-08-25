package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.Trade;
import com.globalbeveragecorp.exchange.model.TradeType;
import com.globalbeveragecorp.exchange.repository.ITradeRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TradeService implements ITradeService {
    private final ITradeRepository tradeRepository;

    @Override
    public void recordTrade(Stock stock, int quantity, TradeType tradeType, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Record Trade : Quantity must be greater than 0");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Record Trade : Price must be greater than 0");
        }

        Trade trade = Trade.builder()
                .stock(stock)
                .timestamp(LocalDateTime.now())
                .quantity(quantity)
                .tradeType(tradeType)
                .price(price).build();
        tradeRepository.addTrade(stock, trade);
    }

    @Override
    public List<Trade> getTradesForStock(Stock stock) {
        return tradeRepository.getRecentTrades(stock);
    }
}
