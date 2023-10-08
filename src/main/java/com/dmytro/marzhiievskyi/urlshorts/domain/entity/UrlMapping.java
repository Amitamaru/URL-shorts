package com.dmytro.marzhiievskyi.urlshorts.domain.entity;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class UrlMapping {

    private String longUrl;
    private String shortUrl;
}
