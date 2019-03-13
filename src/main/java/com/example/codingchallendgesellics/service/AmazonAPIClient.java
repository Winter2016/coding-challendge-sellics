package com.example.codingchallendgesellics.service;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class AmazonAPIClient {
    private static final String REQUEST_URL = "https://completion.amazon.com/search/complete?search-alias=aps&client" +
            "=amazon-search-ui&mkt=1&q=";
    private static final JsonParser jsonParser = JsonParserFactory.getJsonParser();

    /**
     * Calls Amazon autocomplete API and checks either a result list contains the keyword or not
     *
     * @param keyword the string for which a score should be calculated
     * @param currentSubStr a substring of the keyword appending to the Amazon autocomplete API URL
     * @return a boolean value which is true if a result list contains the keyword and false otherwise
     */
    public boolean isWordAutocompleted(String keyword, String currentSubStr) {
        HttpURLConnection connection = null;
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(REQUEST_URL + currentSubStr.replace(" ", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            List<String> suggestions = (List<String>) jsonParser.parseList(content.toString()).get(1);
            return suggestions.stream().anyMatch(el -> el.equalsIgnoreCase(keyword));
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
