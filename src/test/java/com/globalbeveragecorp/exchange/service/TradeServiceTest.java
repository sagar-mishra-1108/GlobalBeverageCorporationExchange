package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.StockType;
import com.globalbeveragecorp.exchange.model.Trade;
import com.globalbeveragecorp.exchange.model.TradeType;
import com.globalbeveragecorp.exchange.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeServiceTest {
    private ITradeService tradeService;
    private Stock pop;
    private Stock gin;

    @BeforeEach
    public void setUp() {
        TradeRepository tradeRepository = new TradeRepository();
        tradeService = new TradeService(tradeRepository);
        pop = Stock.builder().symbol("POP").type(StockType.COMMON).lastDividend(8).parValue(100).build();
        gin = Stock.builder().symbol("GIN").type(StockType.PREFERRED).lastDividend(8).fixedDividend(0.02).parValue(100).build();
    }

    @Test
    void testRecordTrade() {
        tradeService.recordTrade(pop, 150, TradeType.BUY, 125);
        List<Trade> trades = tradeService.getTradesForStock(pop);

        assertEquals(1, trades.size());
        Trade trade = trades.get(0);
        assertEquals(150, trade.getQuantity());
        assertEquals(TradeType.BUY, trade.getTradeType());
        assertEquals(125.0, trade.getPrice());
    }

    @Test
    void testRecordTrade_NegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.recordTrade(pop, -150, TradeType.BUY, 125));
    }

    @Test
    void testRecordTrade_NegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.recordTrade(pop, 150, TradeType.BUY, -125));
    }

    @Test
    void testGetTradesForStock() {
        tradeService.recordTrade(pop, 150, TradeType.BUY, 125);
        tradeService.recordTrade(pop, 200, TradeType.SELL, 120);
        tradeService.recordTrade(gin, 150, TradeType.BUY, 115);

        List<Trade> popTrades = tradeService.getTradesForStock(pop);
        List<Trade> ginTrades = tradeService.getTradesForStock(gin);

        assertEquals(2, popTrades.size());
        assertEquals(1, ginTrades.size());
    }
}