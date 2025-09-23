package com.market.market_place.naver.map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/naver")
@RequiredArgsConstructor
public class NaverMapController {

    private final NaverMapService naverMapService;

    @GetMapping("/map/client-id")
    public ResponseEntity<String> getMapClientId() {
        log.info("클라이언트 ID 전송 처리");
        return ResponseEntity.ok().body(naverMapService.getNaverMapClientId());
    }

    @GetMapping("/map/geocode")
    public ResponseEntity<String> getMapGeocode(@RequestParam String longitude, // 경도
                                                @RequestParam String latitude) {
        Optional<String> addressOptional = naverMapService.getAddress(longitude, latitude);

        return addressOptional
                .map(address -> ResponseEntity.ok(address))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
