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
import static org.mockito.Mockito.times;

class StockServiceTest {
    @Mock
    private ITradeRepository tradeRepository;
    @InjectMocks
    private StockService stockService;

    private Stock pop;
    private Stock gin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pop = Stock.builder().symbol("POP").type(StockType.COMMON).lastDividend(8).parValue(100).build();
        gin = Stock.builder().symbol("GIN").type(StockType.PREFERRED).lastDividend(8).fixedDividend(0.02).parValue(100).build();
    }

    @Test
    void testCalculateDividendYield_CommonStockType() {
        double actualDividendYield = stockService.calculateDividendYield(pop, 125);
        double expectedDividendYield = 8.0 / 125.0; // lastDividend / price
        assertEquals(expectedDividendYield, actualDividendYield);
    }

    @Test
    void testCalculateDividendYield_PreferredStockType() {
        double actualDividendYield = stockService.calculateDividendYield(gin, 115);
        double expectedDividendYield = (0.02 * 100) / 115.0; // (fixedDividend * parValue) / price
        assertEquals(expectedDividendYield, actualDividendYield);
    }

    @Test
    void testCalculateDividendYield_NegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> stockService.calculateDividendYield(pop, -125));
    }

    @Test
    void testCalculatePERatio() {
        double actualPERatio = stockService.calculatePERatio(pop, 125);
        double expectedPERatio = 125.0 / 8.0; // prive / dividend
        assertEquals(expectedPERatio, actualPERatio);
    }

    @Test
    void testCalculatePERatio_NegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> stockService.calculatePERatio(pop, -125));
    }

    @Test
    void testCalculateVolumeWeightedStockPrice() {
        // Trades within the 5-minute window
        Trade popTrade1 = Trade.builder().stock(pop).timestamp(LocalDateTime.now().minusMinutes(2)).quantity(150).tradeType(TradeType.BUY).price(125).build();
        Trade popTrade2 = Trade.builder().stock(pop).timestamp(LocalDateTime.now().minusMinutes(3)).quantity(200).tradeType(TradeType.BUY).price(120).build();
        Trade ginTrade1 = Trade.builder().stock(gin).timestamp(LocalDateTime.now().minusMinutes(3)).quantity(150).tradeType(TradeType.BUY).price(115).build();

        // Trade outside the 5-minute window
        Trade popTrade3 = Trade.builder().stock(pop).timestamp(LocalDateTime.now().minusMinutes(8)).quantity(150).tradeType(TradeType.BUY).price(125).build();

        when(tradeRepository.getRecentTrades(pop)).thenReturn(Arrays.asList(popTrade1, popTrade2, popTrade3));
        when(tradeRepository.getRecentTrades(gin)).thenReturn(Collections.singletonList(ginTrade1));

        double actualVolumeWeightedStockPricePop = stockService.calculateVolumeWeightedStockPrice(pop);
        double expectedVolumeWeightedStockPricePop = (double) ((125 * 150) + (120 * 200)) / (150 + 200);

        double actualVolumeWeightedStockPriceGin = stockService.calculateVolumeWeightedStockPrice(gin);
        double expectedVolumeWeightedStockPriceGin = (double) (115 * 150) / 150;

        verify(tradeRepository, times(1)).getRecentTrades(pop);
        verify(tradeRepository, times(1)).getRecentTrades(gin);

        assertEquals(expectedVolumeWeightedStockPricePop, actualVolumeWeightedStockPricePop);
        assertEquals(expectedVolumeWeightedStockPriceGin, actualVolumeWeightedStockPriceGin);
    }

    @Test
    void testCalculateVolumeWeightedStockPrice_NoTradesWithin5Minutes() {
        // Trade outside the 5-minute window
        Trade popTrade = Trade.builder().stock(pop).timestamp(LocalDateTime.now().minusMinutes(8)).quantity(150).tradeType(TradeType.BUY).price(125).build();

        when(tradeRepository.getRecentTrades(pop)).thenReturn(Collections.singletonList(popTrade));

        double actualVolumeWeightedStockPricePop = stockService.calculateVolumeWeightedStockPrice(pop);

        verify(tradeRepository, times(1)).getRecentTrades(pop);

        assertEquals(0, actualVolumeWeightedStockPricePop);
    }
}