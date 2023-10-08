package com.dmytro.marzhiievskyi.urlshorts.dao;

import com.dmytro.marzhiievskyi.urlshorts.domain.entity.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {

    Optional<UrlMapping> findByLongUrl(String longUrl);

    Optional<UrlMapping> findByShortUrl(String shortUrl);
}
