package com.assignmen.urlshortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignmen.urlshortner.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long>{
    Optional<Url> findByShortUrl(String shortUrl);
}
