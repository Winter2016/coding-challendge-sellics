package com.example.codingchallendgesellics.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreServiceTest {

    private static final String TEST_WORD = "powerlifting";
    @Mock
    private AmazonAPIClient amazonAPIClient;
    @InjectMocks
    private ConcurrentScoreService concurrentScoreService;
    @InjectMocks
    private SequentialScoreService sequentialScoreService;

    @Test
    public void calculate100Score() {
        when(amazonAPIClient.isWordAutocompleted(anyString(), anyString())).thenReturn(true);

        int parallelScore = concurrentScoreService.calculateScore(TEST_WORD);
        int sequentialScore = sequentialScoreService.calculateScore(TEST_WORD);
        assertEquals(100, parallelScore);
        assertEquals(100, sequentialScore);
    }

    @Test
    public void calculate50Score() {
        when(amazonAPIClient.isWordAutocompleted(anyString(), startsWith("powerli"))).thenReturn(true);

        int parallelScore = concurrentScoreService.calculateScore(TEST_WORD);
        int sequentialScore = sequentialScoreService.calculateScore(TEST_WORD);
        assertEquals(50, parallelScore);
        assertEquals(50, sequentialScore);
    }

    @Test
    public void calculate0Score() {
        when(amazonAPIClient.isWordAutocompleted(anyString(), anyString())).thenReturn(false);

        int parallelScore = concurrentScoreService.calculateScore(TEST_WORD);
        int sequentialScore = sequentialScoreService.calculateScore(TEST_WORD);
        assertEquals(0, parallelScore);
        assertEquals(0, sequentialScore);
    }
}