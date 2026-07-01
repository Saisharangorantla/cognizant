package com.cognizant.ormlearn.model;

import jakarta.persistence.*;

/**
 * Entity mapping for the 'attempt_option' table (quiz application schema).
 * Records which option was selected (or shown) for a given attempt-question.
 * Hands-on 3 (springdata3.pdf).
 */
@Entity
@Table(name = "attempt_option")
public class AttemptOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ao_id")
    private int id;

    /**
     * Many attempt-options reference one option (master option text/score).
     */
    @ManyToOne
    @JoinColumn(name = "ao_op_id")
    private Option option;

    /**
     * Many attempt-options belong to one attempt-question.
     */
    @ManyToOne
    @JoinColumn(name = "ao_aq_id")
    private AttemptQuestion attemptQuestion;

    /**
     * Whether the user selected this option (true/false).
     */
    @Column(name = "ao_selected")
    private boolean selected;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Option getOption() { return option; }
    public void setOption(Option option) { this.option = option; }

    public AttemptQuestion getAttemptQuestion() { return attemptQuestion; }
    public void setAttemptQuestion(AttemptQuestion attemptQuestion) { this.attemptQuestion = attemptQuestion; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public String toString() {
        return "AttemptOption{id=" + id + ", option=" + option + ", selected=" + selected + "}";
    }
}
