package com.example.codingchallendgesellics.model;

public class KeywordScore {
    private final String keyword;
    private final int score;

    public KeywordScore(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getScore() {
        return score;
    }
}
