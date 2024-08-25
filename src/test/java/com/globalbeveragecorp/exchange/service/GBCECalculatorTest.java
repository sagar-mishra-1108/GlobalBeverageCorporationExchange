package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;
import com.globalbeveragecorp.exchange.model.StockType;
import com.globalbeveragecorp.exchange.model.Trade;
import com.globalbeveragecorp.exchange.model.TradeType;
import com.globalbeveragecorp.exchange.repository.ITradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GBCECalculatorTest {
    @Mock
    private ITradeRepository tradeRepository;
    @InjectMocks
    private StockService stockService;

    private GBCECalculator gbceCalculator;

    Stock tea;
    Stock pop;
    Stock ale;
    Stock gin;
    Stock joe;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gbceCalculator = new GBCECalculator(stockService);
        tea = Stock.builder().symbol("TEA").type(StockType.COMMON).parValue(100).build();
        pop = Stock.builder().symbol("POP").type(StockType.COMMON).lastDividend(8).parValue(100).build();
        ale = Stock.builder().symbol("ALE").type(StockType.COMMON).lastDividend(23).parValue(60).build();
        gin = Stock.builder().symbol("GIN").type(StockType.PREFERRED).lastDividend(8).fixedDividend(0.02).parValue(100).build();
        joe = Stock.builder().symbol("JOE").type(StockType.COMMON).lastDividend(13).parValue(250).build();
    }

    @Test
    void testCalculateAllShareIndex() {
        Trade popTrade1 = Trade.builder().stock(pop).timestamp(LocalDateTime.now()).quantity(150).tradeType(TradeType.BUY).price(125).build();
        Trade popTrade2 = Trade.builder().stock(pop).timestamp(LocalDateTime.now()).quantity(200).tradeType(TradeType.SELL).price(120).build();
        Trade aleTrade1 = Trade.builder().stock(ale).timestamp(LocalDateTime.now()).quantity(150).tradeType(TradeType.BUY).price(80).build();
        Trade ginTrade1 = Trade.builder().stock(gin).timestamp(LocalDateTime.now()).quantity(150).tradeType(TradeType.BUY).price(115).build();

        when(tradeRepository.getRecentTrades(pop)).thenReturn(Arrays.asList(popTrade1, popTrade2));
        when(tradeRepository.getRecentTrades(ale)).thenReturn(Collections.singletonList(aleTrade1));
        when(tradeRepository.getRecentTrades(gin)).thenReturn(Collections.singletonList(ginTrade1));

        List<Stock> stocks = Arrays.asList(tea, pop, ale, gin, joe);
        double actualAllShareIndex = gbceCalculator.calculateAllShareIndex(stocks);

        verify(tradeRepository, times(1)).getRecentTrades(pop);
        verify(tradeRepository, times(1)).getRecentTrades(ale);
        verify(tradeRepository, times(1)).getRecentTrades(gin);

        double expectedAllShareIndex = Math.pow((double) ((125 * 150) + (120 * 200)) / (150 + 200) * 80 * 115, 1.0 / 3);

        assertEquals(expectedAllShareIndex, actualAllShareIndex, 0.001); // expected and actual values should be within delta of 0.001
    }
}