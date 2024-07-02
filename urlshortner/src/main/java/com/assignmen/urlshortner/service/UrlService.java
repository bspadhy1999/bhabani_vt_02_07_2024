package com.assignmen.urlshortner.service;

public interface UrlService {

    String shortenUrl(String longUrl);

    String getLongUrl(String shortUrl);

    boolean updateShortUrl(String shortUrl, String longUrl);

    boolean updateExpiry(String shortUrl, int daysToAdd);
    
}
