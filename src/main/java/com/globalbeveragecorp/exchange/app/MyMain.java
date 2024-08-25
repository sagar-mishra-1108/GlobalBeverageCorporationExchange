package com.globalbeveragecorp.exchange.app;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.StockType;
import com.globalbeveragecorp.exchange.model.TradeType;
import com.globalbeveragecorp.exchange.repository.StockRepository;
import com.globalbeveragecorp.exchange.repository.TradeRepository;
import com.globalbeveragecorp.exchange.service.*;

import java.util.Arrays;
import java.util.List;

public class MyMain {
    public static void main(String[] args) {
        // Initialize repos for in-memory storage in Hashmap
        StockRepository stockRepository = new StockRepository();
        TradeRepository tradeRepository = new TradeRepository();

        // Initialize services for business calculations
        IStockService stockService = new StockService(tradeRepository);
        ITradeService tradeService = new TradeService(tradeRepository);
        IGBCECalculator gbceCalculator = new GBCECalculator(stockService);

        // Create sample Stocks
        Stock tea = Stock.builder().symbol("TEA").type(StockType.COMMON).parValue(100).build();
        Stock pop = Stock.builder().symbol("POP").type(StockType.COMMON).lastDividend(8).parValue(100).build();
        Stock ale = Stock.builder().symbol("ALE").type(StockType.COMMON).lastDividend(23).parValue(60).build();
        Stock gin = Stock.builder().symbol("GIN").type(StockType.PREFERRED).lastDividend(8).fixedDividend(0.02).parValue(100).build();
        Stock joe = Stock.builder().symbol("JOE").type(StockType.COMMON).lastDividend(13).parValue(250).build();

        // Add sample Stocks to repository
        stockRepository.addStock(tea);
        stockRepository.addStock(pop);
        stockRepository.addStock(ale);
        stockRepository.addStock(gin);
        stockRepository.addStock(joe);

        // Record sample Trades
        tradeService.recordTrade(pop, 150, TradeType.BUY, 125);
        tradeService.recordTrade(pop, 200, TradeType.SELL, 120);
        tradeService.recordTrade(ale, 150, TradeType.BUY, 80);
        tradeService.recordTrade(gin, 150, TradeType.BUY, 115);

        // print sample calculations
        printData(pop, stockService, tradeService, 120);
        printData(ale, stockService, tradeService, 80);
        printData(gin, stockService, tradeService, 115);

        List<Stock> stocks = Arrays.asList(tea, pop, ale, gin, joe);
        System.out.println("\nGBCE All Share index : " + gbceCalculator.calculateAllShareIndex(stocks));

    }

    private static void printData(Stock stock, IStockService stockService, ITradeService tradeService, int price) {
        System.out.println("\nDividend Yield for " + stock.getSymbol() + " : " + stockService.calculateDividendYield(stock, price));
        System.out.println("P/E Ratio for " + stock.getSymbol() + " : " + stockService.calculatePERatio(stock, price));
        System.out.println("Recorded Trades for " + stock.getSymbol() + " : ");
        tradeService.getTradesForStock(stock).forEach(System.out::println);
        System.out.println("Volume Weighted Stock Price for " + stock.getSymbol() + " : " + stockService.calculateVolumeWeightedStockPrice(stock));
    }
}
