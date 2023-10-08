package com.dmytro.marzhiievskyi.urlshorts.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

@Service
public class ValidateUrl {
    void validateUrl(String url) {
        UrlValidator validator = new UrlValidator();
        if (!(validator.isValid(url))) {
            throw new RuntimeException("Url is not valid");
        }
    }
}
