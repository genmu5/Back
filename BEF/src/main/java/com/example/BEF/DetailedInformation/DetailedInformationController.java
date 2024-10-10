package com.example.BEF.DetailedInformation;

import com.example.BEF.MapLocation.DTO.MapLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DetailedInformationController {
    private final DetailedInformationService detailedInformationService;

    @GetMapping("/detail")
    public DetailedInformationResponse getDetailedInformation(@RequestParam long contentid) {
        return detailedInformationService.getLocationDetailed(contentid);
    }
}
