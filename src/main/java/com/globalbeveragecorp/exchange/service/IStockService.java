package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;

public interface IStockService {
    double calculateDividendYield(Stock stock, double price);
    double calculatePERatio(Stock stock, double price);
    double calculateVolumeWeightedStockPrice(Stock stock);
}
