package com.globalbeveragecorp.exchange.repository;

import com.globalbeveragecorp.exchange.model.Stock;

import java.util.HashMap;
import java.util.Map;

public class StockRepository implements IStockRepository {
    private final Map<String, Stock> stockData;

    public StockRepository() {
        this.stockData = new HashMap<>();
    }

    @Override
    public void addStock(Stock stock) {
        stockData.put(stock.getSymbol(), stock);
    }

    @Override
    public Stock getStock(String symbol) {
        return stockData.get(symbol);
    }
}
