package com.example.codingchallendgesellics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "sequentialScoreService")
public class SequentialScoreService implements ScoreService {

    private final AmazonAPIClient amazonAPIClient;

    @Autowired
    public SequentialScoreService(AmazonAPIClient amazonAPIClient) {
        this.amazonAPIClient = amazonAPIClient;
    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public int calculateScore(String keyword) {
        int keywordLength = keyword.length();
        boolean isWordAutocompleted;
        int i = 0;
        do {
            isWordAutocompleted = amazonAPIClient.isWordAutocompleted(keyword, keyword.substring(0, ++i));
        } while (i < keywordLength && !isWordAutocompleted);
        return isWordAutocompleted ? (keywordLength - i + 1) * 100 / keywordLength : 0;
    }

}
