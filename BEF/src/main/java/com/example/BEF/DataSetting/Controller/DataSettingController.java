package com.example.BEF.DataSetting.Controller;

import com.example.BEF.DataSetting.Service.SettingAreaService;
import com.example.BEF.DataSetting.Service.SettingLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@Slf4j
public class DataSettingController {

    @Autowired
    private SettingAreaService settingAreaService;

    @Autowired
    private SettingLocationService settingLocationService;

    @GetMapping("/area")
    public String settingArea() {
        try {
            settingAreaService.settingAreaData();
            return "Area data inserted successfully!";
        } catch (Exception e) {
            log.error(e.toString());
            return "Error occurred: " + e.getMessage();
        }
    }

    @GetMapping("/location")
    public String settingLocation() {
        settingLocationService.fetchAndInsertLocationData();

        return "Location data processed and saved.";
    }
}
