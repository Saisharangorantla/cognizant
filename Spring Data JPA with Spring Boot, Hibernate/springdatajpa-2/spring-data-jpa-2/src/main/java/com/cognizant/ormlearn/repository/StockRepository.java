package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Stock entity.
 *
 * Hands-on 2 - Query Methods on Stock data:
 *  1. findByCodeAndDateBetween      → Facebook stocks in Sep 2019
 *  2. findByCodeAndCloseGreaterThan → Google stocks where close price > 1250
 *  3. findTop3ByOrderByVolumeDesc   → Top 3 highest volume dates
 *  4. findTop3ByCodeOrderByCloseAsc → 3 dates when Netflix stocks were lowest
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    /**
     * HO-2 Q1: Get all stock details of a given company between two dates.
     * Usage: findByCodeAndDateBetween("FB",
     *            LocalDate.of(2019,9,1), LocalDate.of(2019,9,30))
     *
     * SQL: WHERE st_code = ? AND st_date BETWEEN ? AND ?
     */
    List<Stock> findByCodeAndDateBetween(String code, LocalDate startDate, LocalDate endDate);

    /**
     * HO-2 Q2: Get all stock records for a company where closing price exceeds a threshold.
     * Usage: findByCodeAndCloseGreaterThan("GOOGL", new BigDecimal("1250"))
     *
     * SQL: WHERE st_code = ? AND st_close > ?
     */
    List<Stock> findByCodeAndCloseGreaterThan(String code, BigDecimal closePrice);

    /**
     * HO-2 Q3: Find the top 3 records with the highest trading volume (across all companies).
     * SQL: ORDER BY st_volume DESC LIMIT 3
     */
    List<Stock> findTop3ByOrderByVolumeDesc();

    /**
     * HO-2 Q4: Find the 3 dates when a specific company's closing price was lowest.
     * Usage: findTop3ByCodeOrderByCloseAsc("NFLX")
     * SQL: WHERE st_code = ? ORDER BY st_close ASC LIMIT 3
     */
    List<Stock> findTop3ByCodeOrderByCloseAsc(String code);
}
