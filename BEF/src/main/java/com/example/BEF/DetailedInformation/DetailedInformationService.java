package com.example.BEF.DetailedInformation;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Service.LocationRepository;
import com.example.BEF.MapLocation.DTO.MapLocationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class DetailedInformationService {
    private final LocationRepository locationRepository;
    private final DisabledRepository disabledRepository;

    public DetailedInformationResponse getLocationDetailed(Long contetntId){
        Location location = locationRepository.findLocationByContentId(contetntId);
        Disabled disabled = disabledRepository.findDisabledByLocation(location);

        return new DetailedInformationResponse(location, disabled);
    }

}
