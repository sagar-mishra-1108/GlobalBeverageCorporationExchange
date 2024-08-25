package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.Trade;
import com.globalbeveragecorp.exchange.model.TradeType;

import java.util.List;

public interface ITradeService {
    void recordTrade(Stock stock, int quantity, TradeType tradeType, double price);
    List<Trade> getTradesForStock(Stock stock);
}
