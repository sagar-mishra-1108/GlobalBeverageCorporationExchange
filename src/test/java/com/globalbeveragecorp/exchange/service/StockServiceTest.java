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
    void calculateVolumeWeightedStockPrice() {
        Trade popTrade = Trade.builder().stock(pop).timestamp(LocalDateTime.now()).quantity(150).tradeType(TradeType.BUY).price(125).build();
        Trade ginTrade = Trade.builder().stock(gin).timestamp(LocalDateTime.now()).quantity(150).tradeType(TradeType.BUY).price(115).build();

        when(tradeRepository.getRecentTrades(pop)).thenReturn(List.of(popTrade));
        when(tradeRepository.getRecentTrades(gin)).thenReturn(List.of(ginTrade));

        double volumeWeightedStockPricePop = stockService.calculateVolumeWeightedStockPrice(pop);
        double volumeWeightedStockPriceGin = stockService.calculateVolumeWeightedStockPrice(gin);

        verify(tradeRepository, times(1)).getRecentTrades(pop);
        verify(tradeRepository, times(1)).getRecentTrades(gin);

        assertEquals(125, volumeWeightedStockPricePop);
        assertEquals(115, volumeWeightedStockPriceGin);
    }
}