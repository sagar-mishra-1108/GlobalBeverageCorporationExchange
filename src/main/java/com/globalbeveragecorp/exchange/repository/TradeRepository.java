package com.globalbeveragecorp.exchange.repository;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeRepository implements ITradeRepository {
    private final Map<String, List<Trade>> tradeData;

    public TradeRepository() {
        this.tradeData = new HashMap<>();
    }

    @Override
    public void addTrade(Stock stock, Trade trade) {
        tradeData.computeIfAbsent(stock.getSymbol(), k -> new ArrayList<>()).add(trade);
    }

    @Override
    public List<Trade> getRecentTrades(Stock stock) {
        return tradeData.getOrDefault(stock.getSymbol(), List.of());
    }
}
