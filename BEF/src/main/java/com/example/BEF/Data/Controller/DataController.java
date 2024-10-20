package com.example.BEF.Data.Controller;

import com.example.BEF.Data.Service.SettingAreaService;
import com.example.BEF.Data.Service.SettingLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
@Slf4j
@RequiredArgsConstructor
public class DataController {

    private final SettingAreaService settingAreaService;
    private final SettingLocationService settingLocationService;

    @PostMapping("/area")
    public String saveAreas() {
        try {
            settingAreaService.settingAreaData();
            return "Area data inserted successfully!";
        } catch (Exception e) {
            log.error(e.toString());
            return "Error occurred: " + e.getMessage();
        }
    }

    @PostMapping("/location/{areaCode}/{page}")
    public ResponseEntity<String> getLocation(
            @PathVariable("areaCode") String areaCode,
            @PathVariable("page") String page) {

            settingLocationService.setLocationAndDisabled(areaCode, page);

        return ResponseEntity.ok("Location data inserted successfully!\n");
    }

}
