package com.example.codingchallendgesellics.model;

public class KeywordScore {
    private String keyword;
    private int score;

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
