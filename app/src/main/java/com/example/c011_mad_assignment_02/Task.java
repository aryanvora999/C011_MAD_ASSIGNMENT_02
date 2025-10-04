package com.example.c011_mad_assignment_02;

public class Task {
    private String id;
    private String description;
    private String priority; // "High" / "Medium" / "Low"
    private int priorityValue; // 1 = High, 2 = Medium, 3 = Low

    public Task() { } // required for Firestore

    public Task(String id, String description, String priority, int priorityValue) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.priorityValue = priorityValue;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public int getPriorityValue() { return priorityValue; }

    public void setId(String id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setPriorityValue(int priorityValue) { this.priorityValue = priorityValue; }
}
