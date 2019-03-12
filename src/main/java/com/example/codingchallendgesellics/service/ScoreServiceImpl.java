package com.example.codingchallendgesellics.service;


import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;


@Service
public class ScoreServiceImpl implements ScoreService {

    private static final String REQUEST_URL = "https://completion.amazon.com/search/complete?search-alias=aps&client" +
            "=amazon-search-ui&mkt=1&q=";
    private static final String REQUEST_METHOD = "GET";

    @Override
    public boolean isWordAutocompleted(String keyword, String currentSubStr) {
        HttpURLConnection connection = null;
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(REQUEST_URL + currentSubStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            JsonParser jsonParser = JsonParserFactory.getJsonParser();
            List<String> suggestions = (List<String>) jsonParser.parseList(content.toString()).get(1);
            Optional isContain = suggestions.stream()
                    .filter(el -> el.toUpperCase().startsWith(keyword.toUpperCase() + " "))
                    .findAny();
            return isContain.isPresent();
        } catch (IOException e) {
            System.err.println("Couldn't get json object: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
}
