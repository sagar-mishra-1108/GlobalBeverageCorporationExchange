package com.globalbeveragecorp.exchange.service;

import com.globalbeveragecorp.exchange.model.Stock;

import java.util.List;

public interface IGBCECalculator {
    double calculateAllShareIndex(List<Stock> stocks);
}
