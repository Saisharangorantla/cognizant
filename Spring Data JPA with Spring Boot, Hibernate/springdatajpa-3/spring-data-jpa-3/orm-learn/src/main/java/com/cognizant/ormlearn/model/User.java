package com.cognizant.ormlearn.model;

import jakarta.persistence.*;

/**
 * Entity mapping for the 'user' table (quiz application schema).
 * Hands-on 3 (springdata3.pdf).
 *
 * NOTE: table name is quoted as `user` since USER is a reserved word in
 * some databases (including MySQL 8+). We map the Java class to the
 * literal table name `user` via @Table.
 */
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "us_id")
    private int id;

    @Column(name = "us_name")
    private String name;

    @Column(name = "us_email")
    private String email;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
