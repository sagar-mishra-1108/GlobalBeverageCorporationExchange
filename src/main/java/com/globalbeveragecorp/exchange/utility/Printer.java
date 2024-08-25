package com.globalbeveragecorp.exchange.utility;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.service.IStockService;
import com.globalbeveragecorp.exchange.service.ITradeService;

public class Printer {
    public static void printData(Stock stock, IStockService stockService, ITradeService tradeService, int price) {
        System.out.println("\nDividend Yield for " + stock.getSymbol() + " : " + stockService.calculateDividendYield(stock, price));
        System.out.println("P/E Ratio for " + stock.getSymbol() + " : " + stockService.calculatePERatio(stock, price));
        System.out.println("Recorded Trades for " + stock.getSymbol() + " : ");
        tradeService.getTradesForStock(stock).forEach(System.out::println);
        System.out.println("Volume Weighted Stock Price for " + stock.getSymbol() + " : " + stockService.calculateVolumeWeightedStockPrice(stock));
    }
}
