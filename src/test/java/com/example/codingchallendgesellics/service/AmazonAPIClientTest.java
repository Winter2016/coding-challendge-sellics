package com.example.codingchallendgesellics.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class AmazonAPIClientTest {

    private final AmazonAPIClient amazonAPIClient = new AmazonAPIClient();

    @Test
    public void isWordAutocompletedTrue() {
        boolean isWordAutocompleted = amazonAPIClient.isWordAutocompleted("laptop", "laptop");
        assertTrue(isWordAutocompleted);
    }

    @Test
    public void isWordAutocompletedFalse() {
        boolean isWordAutocompleted = amazonAPIClient.isWordAutocompleted("vdfbebrgbn", "v");
        assertFalse(isWordAutocompleted);
    }
}