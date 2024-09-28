package com.example.BEF.Disabled.Service;


import com.example.BEF.Disabled.Domain.Disabled;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisabledRepository extends JpaRepository<Disabled, Long> {
    Disabled findDisabledByContentId(Long contentId);
}
