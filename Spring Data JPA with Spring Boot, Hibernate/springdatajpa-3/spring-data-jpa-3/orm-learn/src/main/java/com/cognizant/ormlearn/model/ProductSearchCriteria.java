package com.cognizant.ormlearn.model;

/**
 * Plain DTO carrying the OPTIONAL filter values a user may select on the
 * left-hand filter panel (springdata3.pdf Hands-on 6 — the "Amazon laptop
 * search" scenario: Customer review, Hard Disk Size, RAM Size, CPU Speed,
 * Operating System, Weight, CPU).
 *
 * Every field is nullable/boxed on purpose: a null value means "the user
 * did NOT select this filter", so ProductRepository's Criteria Query
 * builder skips that predicate entirely. This is exactly the dynamic
 * where-clause problem the hands-on describes — the filters applied
 * depend on what the user picked, so a fixed HQL string can't express it,
 * but a Criteria Query can build the predicate list at runtime.
 */
public class ProductSearchCriteria {

    private String keyword;            // free-text search, e.g. "laptop" (matches name/category)
    private Double minReview;          // Customer review >= minReview
    private Integer minHardDiskGb;     // Hard Disk Size >= minHardDiskGb
    private Integer minRamGb;          // RAM Size >= minRamGb
    private Double minCpuSpeedGhz;     // CPU Speed >= minCpuSpeedGhz
    private String operatingSystem;    // Operating System = exact match
    private Double maxWeightKg;        // Weight <= maxWeightKg
    private String cpu;                // CPU = exact match (e.g. "Intel i7")

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Double getMinReview() { return minReview; }
    public void setMinReview(Double minReview) { this.minReview = minReview; }

    public Integer getMinHardDiskGb() { return minHardDiskGb; }
    public void setMinHardDiskGb(Integer minHardDiskGb) { this.minHardDiskGb = minHardDiskGb; }

    public Integer getMinRamGb() { return minRamGb; }
    public void setMinRamGb(Integer minRamGb) { this.minRamGb = minRamGb; }

    public Double getMinCpuSpeedGhz() { return minCpuSpeedGhz; }
    public void setMinCpuSpeedGhz(Double minCpuSpeedGhz) { this.minCpuSpeedGhz = minCpuSpeedGhz; }

    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }

    public Double getMaxWeightKg() { return maxWeightKg; }
    public void setMaxWeightKg(Double maxWeightKg) { this.maxWeightKg = maxWeightKg; }

    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }
}
