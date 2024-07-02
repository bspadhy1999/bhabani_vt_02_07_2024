package com.assignmen.urlshortner.service.impl;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignmen.urlshortner.entity.Url;
import com.assignmen.urlshortner.repository.UrlRepository;
import com.assignmen.urlshortner.service.UrlService;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public String shortenUrl(String longUrl) {
        String shortUrl = generateShortUrl();
        Url url = new Url();
        url.setShortUrl(shortUrl);
        url.setLongUrl(longUrl);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(LocalDateTime.now().plusMonths(10));
        urlRepository.save(url);
        return shortUrl;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new NoSuchElementException("URL not found"));
        if (url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new NoSuchElementException("URL expired");
        }
        return url.getLongUrl();
    }

    @Override
    public boolean updateShortUrl(String shortUrl, String longUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new NoSuchElementException("URL not found"));
        url.setLongUrl(longUrl);
        urlRepository.save(url);
        return true;
    }

    @Override
    public boolean updateExpiry(String shortUrl, int daysToAdd) {
         Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new NoSuchElementException("URL not found"));
        url.setExpiresAt(url.getExpiresAt().plusDays(daysToAdd));
        urlRepository.save(url);
        return true;
    }

    private String generateShortUrl() {
        String shortUrl;
        do {
            shortUrl = RandomStringUtils.randomAlphanumeric(8);
        } while (urlRepository.findByShortUrl(shortUrl).isPresent());
        return shortUrl;
    }
    
}
