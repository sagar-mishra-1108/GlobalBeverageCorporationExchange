package com.globalbeveragecorp.exchange.repository;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.Trade;

import java.util.List;

public interface ITradeRepository {
    void addTrade(Stock stock, Trade trade);
    List<Trade> getRecentTrades(Stock stock);
}
