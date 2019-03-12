package com.example.codingchallendgesellics.controller;

import com.example.codingchallendgesellics.model.KeywordScore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ScoreController {

    KeywordScore getScore(String keyword) throws InterruptedException,
            ExecutionException, TimeoutException;
}
