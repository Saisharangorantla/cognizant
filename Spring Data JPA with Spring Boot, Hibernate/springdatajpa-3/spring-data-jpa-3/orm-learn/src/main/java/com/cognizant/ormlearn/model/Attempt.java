package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Entity mapping for the 'attempt' table (quiz application schema).
 * Represents one quiz attempt made by a user.
 * Hands-on 3 (springdata3.pdf).
 */
@Entity
@Table(name = "attempt")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "at_id")
    private int id;

    @Column(name = "at_date")
    private LocalDate date;

    /**
     * Many attempts belong to one user.
     */
    @ManyToOne
    @JoinColumn(name = "at_us_id")
    private User user;

    @Column(name = "at_score")
    private double score;

    /**
     * One attempt has many attempt-questions.
     * mappedBy refers to the 'attempt' field in AttemptQuestion.
     */
    @OneToMany(mappedBy = "attempt")
    private Set<AttemptQuestion> attemptQuestionList;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public Set<AttemptQuestion> getAttemptQuestionList() { return attemptQuestionList; }
    public void setAttemptQuestionList(Set<AttemptQuestion> attemptQuestionList) {
        this.attemptQuestionList = attemptQuestionList;
    }

    @Override
    public String toString() {
        return "Attempt{id=" + id + ", date=" + date + ", score=" + score + "}";
    }
}
