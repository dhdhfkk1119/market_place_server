package com.market.market_place.naver.map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/naver")
@RequiredArgsConstructor
public class NaverMapController {

    private final NaverMapService naverMapService;

    @GetMapping("/map/geocode")
    public ResponseEntity<String> getMapGeocode(@RequestParam String longitude, // 경도
                                                @RequestParam String latitude,
                                                @RequestParam String clientId) {
        Optional<String> addressOptional = naverMapService.getAddress(longitude, latitude, clientId);

        return addressOptional
                .map(address -> ResponseEntity.ok(address))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
