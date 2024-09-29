package com.example.BEF.MapLocation.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledRepository;
import org.springframework.stereotype.Service;

@Service
public class DisabledService {
    private final DisabledRepository disabledRepository;

    public DisabledService(DisabledRepository disabledRepository) {
        this.disabledRepository = disabledRepository;
    }

    public Disabled findDisabledByContentId(Long contentId) {
        return disabledRepository.findDisabledByContentId(contentId);
    }
}
