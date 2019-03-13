package com.example.codingchallendgesellics.controller;

import com.example.codingchallendgesellics.model.KeywordScore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ScoreController {

    /**
     * Maps an http request to the service method calculating the score
     *
     * @param keyword a string for which a score should be calculated
     * @return an object containing the keyword and its score
     * @throws TimeoutException when a microservice exceeds an SLA of 10 seconds for a request round-trip.
     */
    KeywordScore getScore(String keyword) throws InterruptedException,
            ExecutionException, TimeoutException;
}
