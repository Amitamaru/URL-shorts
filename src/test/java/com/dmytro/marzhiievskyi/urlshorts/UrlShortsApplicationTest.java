package com.dmytro.marzhiievskyi.urlshorts;

import com.dmytro.marzhiievskyi.urlshorts.dao.UrlMappingRepository;
import com.dmytro.marzhiievskyi.urlshorts.domain.entity.UrlMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UrlShortsApplicationTest extends AbstractControllerTest {

    public static final String URL_CONVERTER_SHORTEN = "/api/v1/urlConverter/shorten";
    public static final String RESOLVE_SHORT_URL = "/api/v1/urlConverter/resolve?shortUrl=";
    public static final String RESOLVE_SHORT_URL_INVALID = "/api/v1/urlConverter/resolve?shortUrl=123";
    private final String longUrl = "https://google.com";
    private final String shortUrl = "https://ggl.cm";

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @BeforeEach
    public void down() {
        urlMappingRepository.deleteAll();
    }

    @Test
    public void shorten_url_valid() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post(URL_CONVERTER_SHORTEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url":"https://google.com"
                        }
                        """);
        perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.url").exists()

                );
        assertEquals(longUrl, urlMappingRepository.findByLongUrl(longUrl).orElseThrow().getLongUrl());
    }

    @Test
    public void shorten_url_invalid() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post(URL_CONVERTER_SHORTEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url":"https1://google.com"
                        }
                        """);
        perform(requestBuilder)
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    public void resolve_url_valid() throws Exception {
        urlMappingRepository.save(UrlMapping.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build());

        assertEquals(shortUrl, urlMappingRepository.findByShortUrl(shortUrl).orElseThrow().getShortUrl());

        var requestBuilder = MockMvcRequestBuilders.get(RESOLVE_SHORT_URL + shortUrl);
        perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "url":"https://google.com"
                                }
                                """)
                );
    }

    @Test
    public void resolve_url_invalid() throws Exception {
        urlMappingRepository.save(UrlMapping.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build());

        assertEquals(shortUrl, urlMappingRepository.findByShortUrl(shortUrl).orElseThrow().getShortUrl());

        var requestBuilder = MockMvcRequestBuilders.get(RESOLVE_SHORT_URL_INVALID + shortUrl);
        perform(requestBuilder)
                .andExpectAll(
                        status().is4xxClientError()
                );
    }


}
