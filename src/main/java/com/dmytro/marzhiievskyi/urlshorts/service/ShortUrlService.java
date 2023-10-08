package com.dmytro.marzhiievskyi.urlshorts.service;

import com.dmytro.marzhiievskyi.urlshorts.dao.UrlMappingRepository;
import com.dmytro.marzhiievskyi.urlshorts.domain.dto.UrlDto;
import com.dmytro.marzhiievskyi.urlshorts.domain.entity.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final ShortenUrlConverter shortenUrlConverter;

    private final ValidateUrl validateUrl;

    private final UrlMappingRepository urlMappingRepository;

    public ResponseEntity<UrlDto> shortenUrl(UrlDto urlDto) {

        String longUrl = urlDto.getUrl();

        validateUrl.validateUrl(longUrl);
        Optional<UrlMapping> existUrlMapping = urlMappingRepository.findByLongUrl(longUrl);

        if (existUrlMapping.isPresent()) {
            return ResponseEntity.ok(
                    UrlDto.builder()
                            .url(existUrlMapping.get().getShortUrl())
                            .build()
            );
        } else {
            String shortUrl = shortenUrlConverter.convertUrlToShort();
            UrlMapping urlMapping = UrlMapping.builder()
                    .longUrl(longUrl)
                    .shortUrl(shortUrl)
                    .build();
            urlMappingRepository.save(urlMapping);
            return ResponseEntity.ok(UrlDto.builder()
                    .url(shortUrl)
                    .build());
        }
    }

    public ResponseEntity<UrlDto> resolveUrl(String shortUrl) {

        Optional<UrlMapping> existMapping = urlMappingRepository.findByShortUrl(shortUrl);
        String longUrl = existMapping.orElseThrow().getLongUrl();
        return ResponseEntity.ok(UrlDto.builder()
                .url(longUrl)
                .build());

    }

}
