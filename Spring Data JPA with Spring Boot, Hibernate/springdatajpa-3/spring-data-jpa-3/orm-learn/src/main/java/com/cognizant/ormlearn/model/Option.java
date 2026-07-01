package com.cognizant.ormlearn.model;

import jakarta.persistence.*;

/**
 * Entity mapping for the 'options' table (quiz application schema).
 * Hands-on 3 (springdata3.pdf).
 *
 * Class is named 'Option' (singular) to avoid colliding with java.util.Optional
 * naming conventions; the @Table annotation maps it to the actual 'options' table.
 */
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "op_id")
    private int id;

    /**
     * Many options belong to one question.
     */
    @ManyToOne
    @JoinColumn(name = "op_qt_id")
    private Question question;

    @Column(name = "op_score")
    private double score;

    @Column(name = "op_text")
    private String text;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @Override
    public String toString() {
        return "Option{id=" + id + ", text='" + text + "', score=" + score + "}";
    }
}
