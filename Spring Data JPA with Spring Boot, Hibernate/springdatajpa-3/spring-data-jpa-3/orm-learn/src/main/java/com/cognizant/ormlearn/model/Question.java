package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Entity mapping for the 'question' table (quiz application schema).
 * Hands-on 3 (springdata3.pdf).
 */
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qt_id")
    private int id;

    @Column(name = "qt_text")
    private String text;

    /**
     * One question has many options.
     * mappedBy refers to the 'question' field in Option.
     */
    @OneToMany(mappedBy = "question")
    private Set<Option> optionList;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Set<Option> getOptionList() { return optionList; }
    public void setOptionList(Set<Option> optionList) { this.optionList = optionList; }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + text + "'}";
    }
}
