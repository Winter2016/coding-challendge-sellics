package com.example.codingchallendgesellics.controller;

import com.example.codingchallendgesellics.model.KeywordScore;
import com.example.codingchallendgesellics.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class ScoreControllerImpl implements ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreControllerImpl(@Qualifier(value = "sequentialScoreService") ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * @param keyword
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    @GET
    @RequestMapping(value = "/estimate", params = {"keyword!="})
    public KeywordScore getScore(@QueryParam("keyword") String keyword) throws InterruptedException,
            ExecutionException, TimeoutException {
        String spacedKeyword = keyword.replace('+', ' ');
        return CompletableFuture.supplyAsync(() -> {
            int score = scoreService.calculateScore(spacedKeyword);
            return new KeywordScore(spacedKeyword, score);
        }).get(10, TimeUnit.SECONDS);
    }

    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT,
            reason = "The request failed due to a timeout. Try a shorter word")
    @ExceptionHandler(TimeoutException.class)
    private void handleTimeoutException() {
        //TODO add logging
        System.err.println("Timeout Exception");
    }
}
