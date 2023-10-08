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
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/urlConverter/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url":"https://google.com"
                        }
                        """);
        //when
        perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.url").exists()

                );
        assertEquals(longUrl, urlMappingRepository.findByLongUrl(longUrl).orElseThrow().getLongUrl());
    }

    @Test
    public void shorten_url_invalid() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/urlConverter/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url":"httpps://google.com"
                        }
                        """);
        //when
        perform(requestBuilder)
                //then
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    public void resolve_url_valid() throws Exception {
        //given
        urlMappingRepository.save(UrlMapping.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build());

        assertEquals(shortUrl, urlMappingRepository.findByShortUrl(shortUrl).orElseThrow().getShortUrl());

        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/urlConverter/resolve?shortUrl=" + shortUrl);
        //when
        perform(requestBuilder)
                //then
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
        //given
        urlMappingRepository.save(UrlMapping.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build());

        assertEquals(shortUrl, urlMappingRepository.findByShortUrl(shortUrl).orElseThrow().getShortUrl());

        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/urlConverter/resolve?shortUrl=123" + shortUrl);
        //when
        perform(requestBuilder)
                //then
                .andExpectAll(
                        status().is4xxClientError()
                );
    }


}
