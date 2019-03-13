package com.example.codingchallendgesellics.controller;

import com.example.codingchallendgesellics.model.KeywordScore;
import com.example.codingchallendgesellics.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class ScoreControllerImpl implements ScoreController {

    private final ScoreService scoreService;

    /**
     * Here any of two Score Services can be injected
     */
    @Autowired
    public ScoreControllerImpl(@Qualifier(value = "concurrentScoreService") ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Override
    @GetMapping(value = "/estimate", params = {"keyword"})
    public KeywordScore getScore(@RequestParam("keyword") String keyword) throws InterruptedException,
            ExecutionException, TimeoutException {
        String spacedKeyword = keyword.replace('+', ' ');
        return CompletableFuture.supplyAsync(() -> {
            int score = scoreService.calculateScore(spacedKeyword);
            return new KeywordScore(spacedKeyword, score);
        }).get(10, TimeUnit.SECONDS);
    }

    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "The request failed due to a timeout. Try a shorter word")
    @ExceptionHandler(TimeoutException.class)
    private void handleTimeoutException() {
        //TODO add logging
        System.err.println("Timeout Exception");
    }
}
