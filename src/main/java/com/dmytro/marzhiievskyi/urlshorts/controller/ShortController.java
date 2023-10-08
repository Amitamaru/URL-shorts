package com.dmytro.marzhiievskyi.urlshorts.controller;

import com.dmytro.marzhiievskyi.urlshorts.domain.dto.UrlDto;
import com.dmytro.marzhiievskyi.urlshorts.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urlConverter")
public class ShortController {

    private final ShortUrlService shortUrlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlDto> shortenUrl(@RequestBody UrlDto longUrl) {
        return shortUrlService.shortenUrl(longUrl);
    }

    @GetMapping("/resolve")
    public ResponseEntity<UrlDto> resolveUrl(@RequestParam String shortUrl) {
        return shortUrlService.resolveUrl(shortUrl);
    }

}
