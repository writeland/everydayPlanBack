package com.exmple.practice;

public class Task {
    private String title;

    public Task(String title) {
        setTitle(title);
    }

    public Task() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("标题不能为空");
        }

        title = newTitle;
    }
}
