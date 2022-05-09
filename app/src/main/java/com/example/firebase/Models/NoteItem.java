package com.example.firebase.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class NoteItem implements Serializable {

    @Exclude
    private String id;

    String label, textContent;
    public NoteItem() {

    }

    public NoteItem(String label, String textContent) {
        this.label = label;
        this.textContent = textContent;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getLabel() { return label; }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
