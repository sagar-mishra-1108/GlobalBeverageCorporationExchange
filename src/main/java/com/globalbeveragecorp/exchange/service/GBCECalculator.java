package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GBCECalculator implements IGBCECalculator {
    private final IStockService stockService;

    @Override
    public double calculateAllShareIndex(List<Stock> stocks) {
        double product = 1.0;
        int count = 0;

        for (Stock stock : stocks) {
            double volumeWeightedStockPrice = stockService.calculateVolumeWeightedStockPrice(stock);
            if (volumeWeightedStockPrice > 0) {
                product *= volumeWeightedStockPrice;
                count++;
            }
        }

        return Math.pow(product, 1.0/count);
    }
}
