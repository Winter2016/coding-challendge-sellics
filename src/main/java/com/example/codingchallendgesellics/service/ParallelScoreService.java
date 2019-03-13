package com.example.codingchallendgesellics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service(value = "parallelScoreService")
public class ParallelScoreService implements ScoreService {

    private final AmazonAPIClient amazonAPIClient;

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
    //TODO figure out limits
    private volatile int shortestAutoCompleted = 999;
    private volatile int longestNotAutoCompleted = 0;
    private volatile boolean isDone;

    @Autowired
    public ParallelScoreService(AmazonAPIClient amazonAPIClient) {
        this.amazonAPIClient = amazonAPIClient;
    }

    public int calculateScore(String keyword) {
        int score = 0;
        int keywordLength = keyword.length();

        List<String> tokens = new ArrayList<>();
        for (int i = 1; i <= keywordLength; i++) {
            tokens.add(keyword.substring(0, i));
        }

        Set<Callable<Void>> callableSet = tokens.stream()
                .map(token -> (Callable<Void>) () -> {
                    if (!isDone) {
                        System.out.println("Call for token " + token + " is started");
                        boolean isWordAutocompleted = amazonAPIClient.isWordAutocompleted(keyword, token);
                        if (isWordAutocompleted) {
                            compareAndSetShortestAutoCompleted(token.length());
                        } else {
                            compareAndSetLongestNotAutoCompleted(token.length());
                        }
                        if (shortestAutoCompleted - longestNotAutoCompleted == 1) {
                            isDone = true;
                        }
                    }
//                    else {
//                        System.out.println("Call for token " + token + " isn't started");
//                    }
                    return null;
                })
                .collect(Collectors.toSet());
        try {
            fixedThreadPool.invokeAll(callableSet);
            score = shortestAutoCompleted - longestNotAutoCompleted == 1 ?
                    (keywordLength - longestNotAutoCompleted) * 100 / keywordLength : 0;
        } catch (InterruptedException e) {
            System.err.println("Couldn't calculate score because of: ");
            e.printStackTrace();
        } finally {
            shortestAutoCompleted = 999;
            longestNotAutoCompleted = 0;
            isDone = false;
        }
        return score;
    }

    private synchronized void compareAndSetShortestAutoCompleted(int newValue) {
        if (newValue < shortestAutoCompleted) {
            shortestAutoCompleted = newValue;
        }
    }

    private synchronized void compareAndSetLongestNotAutoCompleted(int newValue) {
        if (newValue > longestNotAutoCompleted) {
            longestNotAutoCompleted = newValue;
        }
    }
}
