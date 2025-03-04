package com.example.BEF.Course.DTO;

import com.example.BEF.Location.Domain.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleLoc {
    private Long contentId;
    private String contentTitle;
    private String addr;

    private SimpleLoc(Long contentId, String contentTitle, String addr) {
        this.contentId = contentId;
        this.contentTitle = contentTitle;
        this.addr = addr;
    }

    @Builder
    public static SimpleLoc from(Location location) {
        return new SimpleLoc(location.getContentId(), location.getContentTitle(), location.getAddr());
    }
}
