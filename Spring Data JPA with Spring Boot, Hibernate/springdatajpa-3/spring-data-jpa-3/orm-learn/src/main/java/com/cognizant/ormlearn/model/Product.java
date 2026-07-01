package com.cognizant.ormlearn.model;

import jakarta.persistence.*;

/**
 * Entity mapping for the 'product' table.
 * Used in Hands-on 6 (springdata3.pdf) to demonstrate Criteria Query —
 * modeling the "search laptop, then filter by review/RAM/CPU/etc." scenario
 * described in the PDF.
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_id")
    private int id;

    @Column(name = "pr_name")
    private String name;

    @Column(name = "pr_category")
    private String category;

    /** Customer review rating, e.g. 1.0–5.0 */
    @Column(name = "pr_review")
    private double review;

    /** Hard disk size in GB */
    @Column(name = "pr_hard_disk_gb")
    private int hardDiskGb;

    /** RAM size in GB */
    @Column(name = "pr_ram_gb")
    private int ramGb;

    /** CPU speed in GHz */
    @Column(name = "pr_cpu_speed_ghz")
    private double cpuSpeedGhz;

    @Column(name = "pr_operating_system")
    private String operatingSystem;

    /** Weight in kg */
    @Column(name = "pr_weight_kg")
    private double weightKg;

    @Column(name = "pr_cpu")
    private String cpu;

    @Column(name = "pr_price")
    private double price;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getReview() { return review; }
    public void setReview(double review) { this.review = review; }

    public int getHardDiskGb() { return hardDiskGb; }
    public void setHardDiskGb(int hardDiskGb) { this.hardDiskGb = hardDiskGb; }

    public int getRamGb() { return ramGb; }
    public void setRamGb(int ramGb) { this.ramGb = ramGb; }

    public double getCpuSpeedGhz() { return cpuSpeedGhz; }
    public void setCpuSpeedGhz(double cpuSpeedGhz) { this.cpuSpeedGhz = cpuSpeedGhz; }

    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', category='" + category
                + "', review=" + review + ", ramGb=" + ramGb + ", hardDiskGb=" + hardDiskGb
                + ", cpuSpeedGhz=" + cpuSpeedGhz + ", os='" + operatingSystem
                + "', weightKg=" + weightKg + ", cpu='" + cpu + "', price=" + price + "}";
    }
}
