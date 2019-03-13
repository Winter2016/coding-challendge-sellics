package com.example.codingchallendgesellics.service;

public interface ScoreService {
    /**
     * Calculates a score of a given keyword depending on when it appears in Amazon autocomplete API
     *
     * @param keyword the string for which a score should be calculated
     * @return a value in the range [0 → 100]
     */
    int calculateScore(String keyword);
}
