package com.example.codingchallendgesellics.service;

public interface ScoreService {

    int calculateScore(String keyword);

    boolean isWordAutocompleted(String keyword, String currentSubStr);
}
