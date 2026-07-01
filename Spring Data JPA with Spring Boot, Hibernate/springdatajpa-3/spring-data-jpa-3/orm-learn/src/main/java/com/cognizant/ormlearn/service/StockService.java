package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Stock;
import com.cognizant.ormlearn.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Stock operations.
 * Hands-on 2.
 */
@Service
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    /** HO-2 Q1: Facebook stocks in September 2019. */
    @Transactional
    public List<Stock> getFacebookStockSep2019() {
        LOGGER.info("Start getFacebookStockSep2019");
        LocalDate start = LocalDate.of(2019, 9, 1);
        LocalDate end   = LocalDate.of(2019, 9, 30);
        List<Stock> list = stockRepository.findByCodeAndDateBetween("FB", start, end);
        LOGGER.info("End getFacebookStockSep2019: count={}", list.size());
        return list;
    }

    /** HO-2 Q2: Google stocks where close price > 1250. */
    @Transactional
    public List<Stock> getGoogleStocksAbove1250() {
        LOGGER.info("Start getGoogleStocksAbove1250");
        List<Stock> list = stockRepository.findByCodeAndCloseGreaterThan(
                "GOOGL", new BigDecimal("1250"));
        LOGGER.info("End getGoogleStocksAbove1250: count={}", list.size());
        return list;
    }

    /** HO-2 Q3: Top 3 dates with highest trading volume. */
    @Transactional
    public List<Stock> getTop3ByVolume() {
        LOGGER.info("Start getTop3ByVolume");
        List<Stock> list = stockRepository.findTop3ByOrderByVolumeDesc();
        LOGGER.info("End getTop3ByVolume");
        return list;
    }

    /** HO-2 Q4: 3 dates when Netflix stocks were lowest (by close price). */
    @Transactional
    public List<Stock> getNetflixLowest3() {
        LOGGER.info("Start getNetflixLowest3");
        List<Stock> list = stockRepository.findTop3ByCodeOrderByCloseAsc("NFLX");
        LOGGER.info("End getNetflixLowest3");
        return list;
    }
}
