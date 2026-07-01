package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Entity mapping for the 'attempt_question' table (quiz application schema).
 * Links an Attempt to a Question (one row per question presented in an attempt).
 * Hands-on 3 (springdata3.pdf).
 */
@Entity
@Table(name = "attempt_question")
public class AttemptQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aq_id")
    private int id;

    /**
     * Many attempt-questions belong to one attempt.
     */
    @ManyToOne
    @JoinColumn(name = "aq_at_id")
    private Attempt attempt;

    /**
     * Many attempt-questions reference one question.
     */
    @ManyToOne
    @JoinColumn(name = "aq_qt_id")
    private Question question;

    /**
     * One attempt-question has many attempt-options
     * (the options shown / selected for this question in this attempt).
     * mappedBy refers to the 'attemptQuestion' field in AttemptOption.
     */
    @OneToMany(mappedBy = "attemptQuestion")
    private Set<AttemptOption> attemptOptionList;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Attempt getAttempt() { return attempt; }
    public void setAttempt(Attempt attempt) { this.attempt = attempt; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Set<AttemptOption> getAttemptOptionList() { return attemptOptionList; }
    public void setAttemptOptionList(Set<AttemptOption> attemptOptionList) {
        this.attemptOptionList = attemptOptionList;
    }

    @Override
    public String toString() {
        return "AttemptQuestion{id=" + id + ", question=" + question + "}";
    }
}
