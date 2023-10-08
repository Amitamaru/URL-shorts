package com.dmytro.marzhiievskyi.urlshorts.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ShortenUrlConverter {
    @Value("${parameter.url-converter.key-length}")
    private int keyLength;

    @Value("${parameter.url-converter.allowed-chars}")
    private String allowedCharacters;

    public String convertUrlToShort() {

        String protocol = "https://";
        StringBuilder keyBuilder = new StringBuilder(protocol);
        Random random = new Random();

        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            keyBuilder.append(allowedCharacters.charAt(randomIndex));
        }

        return keyBuilder.toString();
    }

}
