package com.example.BEF.Search;

import com.example.BEF.MapLocation.DTO.MapLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public List<MapLocationResponse> searchLocations(@RequestParam("keyword") String keyword) {
        // 키워드를 기반으로 Location과 Disabled 정보를 조합한 응답 리스트를 반환
        return searchService.findByKeyword(keyword);
    }
}
