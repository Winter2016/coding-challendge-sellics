package com.example.codingchallendgesellics.controller;

import com.example.codingchallendgesellics.model.KeywordScore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ScoreController {

    /**
     * Maps the http request to a service method calculating the score
     *
     * @param keyword the string entered in the http request for which a score should be calculated
     * @return a POJO class containing the keyword and its score
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException     when a microservice exceeds an SLA of 10 seconds for a request round-trip.
     */
    KeywordScore getScore(String keyword) throws InterruptedException,
            ExecutionException, TimeoutException;
}
