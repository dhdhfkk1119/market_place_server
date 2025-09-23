package com.market.market_place.naver.map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class NaverMapService {

    @Value("${naver.api.client-id}")
    private String naverMapClientId;

    @Value("${naver.api.client-secret}")
    private String naverMapClientSecret;

    public String getNaverMapClientId() {
        if (naverMapClientId == null || naverMapClientId.trim().isEmpty()) {
            return "";
        }
        return naverMapClientId;
    }

    public Optional<String> getAddress(String longitude, String latitude) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverMapClientId);
        headers.set("X-NCP-APIGW-API-KEY", naverMapClientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API URL과 파라미터 수정!
        URI uri = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-reversegeocode/v2/gc")
                .queryParam("coords", longitude + "," + latitude)
                .queryParam("output", "json")
                .encode()
                .build()
                .toUri();

        NaverMapResponse response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                NaverMapResponse.class
        ).getBody();

        // 결과에서 주소 정보만 추출
        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            String fullAddress = response.getResults().get(0).getRegion().getFullName();
            return Optional.of(fullAddress.trim());
        }

        return Optional.empty();
    }
}
