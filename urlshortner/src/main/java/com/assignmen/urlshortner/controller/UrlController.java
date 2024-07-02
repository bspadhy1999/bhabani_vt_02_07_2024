package com.assignmen.urlshortner.controller;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.assignmen.urlshortner.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;
    
    //http://localhost:8080/api/url/shorten?longUrl=https://www.example.com
     @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String longUrl) {
        String shortUrl = urlService.shortenUrl(longUrl);
        return ResponseEntity.ok("http://localhost:8080/" + shortUrl);
    }

    //http://localhost:8080/api/url/YZr4lJL8
    @GetMapping("/{shortUrl}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortUrl) {
        try {
            String longUrl = urlService.getLongUrl(shortUrl);
            response.sendRedirect(longUrl);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }

    //http://localhost:8080/api/url/update?shortUrl=YZr4lJL8&longUrl=https://www.newexample.com
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateShortUrl(@RequestParam String shortUrl, @RequestParam String longUrl) {
        boolean result = urlService.updateShortUrl(shortUrl, longUrl);
        return ResponseEntity.ok(result);
    }

    //http://localhost:8080/api/url/updateExpiry?shortUrl=YZr4lJL8&daysToAdd=30
    @PostMapping("/updateExpiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        boolean result = urlService.updateExpiry(shortUrl, daysToAdd);
        return ResponseEntity.ok(result);
    }
    
}
