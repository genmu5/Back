package com.example.BEF.Search;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.MapLocation.DTO.MapLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final DisabledRepository disabledRepository;

    public List<MapLocationResponse> findByKeyword(String keyword) {
        List<Location> locations = searchRepository.findByKeyword(keyword);

        return locations.stream()
                .map(location -> {
                    Disabled disabled = disabledRepository.findDisabledByLocation(location);

                    return new MapLocationResponse(location, disabled);
                })
                .collect(Collectors.toList());
    }
}
