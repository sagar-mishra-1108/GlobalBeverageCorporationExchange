package com.globalbeveragecorp.exchange.repository;

import com.globalbeveragecorp.exchange.model.Stock;

public interface IStockRepository {
    void addStock(Stock stock);
    Stock getStock(String symbol);
}
