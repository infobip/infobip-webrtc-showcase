package com.infobip.rtc.showcase.token.infobip;

import com.infobip.rtc.showcase.token.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {

    @Value("http://${infobip.api-host}${infobip.rtc-token-path}")
    private String infobipTokenUrl;

    private final String applicationId;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    private int counter = 0;

    public TokenService(@Value("${infobip.api-key}") String infobipApiKey,
                        @Value("${infobip.application-id}") String infobipApplicationId) {
        applicationId = infobipApplicationId;
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "App " + infobipApiKey);
    }

    public TokenResponse nextToken() {
        String identity = nextIdentity();
        HttpEntity<TokenRequest> request = new HttpEntity<>(new TokenRequest(identity, applicationId), httpHeaders);
        TokenResponse response = restTemplate.postForObject(infobipTokenUrl, request, TokenResponse.class);
        response.setIdentity(identity);
        return response;
    }

    private String nextIdentity() {
        return String.format("user%d", counter++);
    }
}
