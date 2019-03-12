package com.example.codingchallendgesellics.controller;

import com.example.codingchallendgesellics.model.KeywordScore;
import com.example.codingchallendgesellics.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

@RestController
public class ScoreControllerImpl implements ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreControllerImpl(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Override
    @GET
    @RequestMapping("/estimate")
    public KeywordScore getScore(@QueryParam("keyword") String keyword) {
        keyword = keyword.replace('+', ' ');
        int n = keyword.length();
        boolean isContain;
        int i = 0;
        do {
            isContain = scoreService.isWordAutocompleted(keyword, keyword.substring(0, ++i));
        } while (i < n && !isContain);
        int score = isContain ? (n - i + 1) * 100 / n : 0;
        return new KeywordScore(keyword, score);
    }
}
