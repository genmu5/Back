package com.example.BEF.Location.Repository;


import com.example.BEF.Location.Domain.Location;

import java.util.List;
import java.util.Set;

public interface LocationRepositoryCustom {
    Set<Location> filterByAreaAndDisabilityAndTravelType(Long area, List<Long> disabilities, List<Long> travelTypes);
    Set<Location> filterByAreaAndContentType(Long area);

}
