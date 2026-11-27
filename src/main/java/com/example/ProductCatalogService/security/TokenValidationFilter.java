package com.example.ProductCatalogService.security;

import com.example.ProductCatalogService.dto.AuthUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final String authServieUrl;
    private final RestTemplate restTemplate;

    public TokenValidationFilter(
            @Value("${auth.service.url}") String authServieUrl,
            RestTemplateBuilder restTemplateBuilder) {
        this.authServieUrl = authServieUrl;
        this.restTemplate = restTemplateBuilder.build();
    }

    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws IOException, ServletException
    {
         if(req.getMethod().equals("GET"))
         {
             filterChain.doFilter(req, res);
             return;
         }
         String header = req.getHeader("Authorization");
         if(header == null || !header.startsWith("Bearer "))
         {
             res.setStatus(HttpStatus.UNAUTHORIZED.value());
             return;
         }
        AuthUserDto authUserDto;
         try {
             HttpHeaders headers = new HttpHeaders();
             headers.add("Authorization", header);
             ResponseEntity<AuthUserDto> answer = restTemplate
                     .exchange(
                             this.authServieUrl + "/auth/validateToken",
                             HttpMethod.GET,
                             new HttpEntity<>(headers),
                             AuthUserDto.class
                     );
             authUserDto = answer.getBody();
         } catch (HttpClientErrorException e) {
             res.setStatus(HttpStatus.UNAUTHORIZED.value());
             return;
         }
         catch (RestClientException ex) {
             res.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            return;
         }

         if(req.getMethod().equals("DELETE") && !"ADMIN".equals(authUserDto.getRole()))
         {
             res.setStatus(HttpStatus.FORBIDDEN.value());
             return;
         }
        filterChain.doFilter(req, res);
    }
}
