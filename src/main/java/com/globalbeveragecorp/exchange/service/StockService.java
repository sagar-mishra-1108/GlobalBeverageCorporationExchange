package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.StockType;
import com.globalbeveragecorp.exchange.model.Trade;
import com.globalbeveragecorp.exchange.repository.ITradeRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class StockService implements IStockService {
    private final ITradeRepository tradeRepository;

    @Override
    public double calculateDividendYield(Stock stock, double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Calculate Dividend Yield : Price must be greater than 0");
        }

        if (stock.getType() == StockType.COMMON) {
            return stock.getLastDividend() / price;
        }
        // StockType == PREFERRED
        return (stock.getFixedDividend() * stock.getParValue()) / price;
    }

    @Override
    public double calculatePERatio(Stock stock, double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Calculate P/E Ratio : Price must be greater than 0");
        }

        double lastDividend = stock.getLastDividend();
        return lastDividend == 0 ? Double.POSITIVE_INFINITY : (price / lastDividend);
    }

    @Override
    public double calculateVolumeWeightedStockPrice(Stock stock) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);

        List<Trade> trades = tradeRepository.getRecentTrades(stock);

        double totalTradePriceQuantity = 0;
        int totalQuantity = 0;

        for (Trade trade : trades) {
            // Calculate for trades done in past five minutes
            if (trade.getTimestamp().isAfter(fiveMinutesAgo)) {
                totalTradePriceQuantity += trade.getPrice() * trade.getQuantity();
                totalQuantity += trade.getQuantity();
            }
        }

        return totalQuantity == 0 ? 0 : totalTradePriceQuantity / totalQuantity;
    }
}
