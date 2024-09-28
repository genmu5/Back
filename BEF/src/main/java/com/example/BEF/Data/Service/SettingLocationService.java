package com.example.BEF.Data.Service;

import com.example.BEF.Area.Service.AreaRepository;
import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Service.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettingLocationService {
    private static final Logger log = LoggerFactory.getLogger(SettingLocationService.class);

    @Value("${openapi.service-key2}")
    private String serviceKey;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DisabledRepository disabledRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    String defaultURL = "https://apis.data.go.kr/B551011/KorWithService1/";
    String defaultURL2 = "&MobileOS=IOS&MobileApp=BEF&_type=json";

    public void setLocationAndDisabled(String areaCode, String page) {

        List<Location> locationList = new ArrayList<>();
        setLocations(areaCode, page, locationList);

        List<Disabled> disabledList = new ArrayList<>();
        setDisabled(locationList, disabledList);
    }

    public void setLocations(String areaCode, String page, List<Location> locationList){

        String area = "&areaCode=" + areaCode; // 지역
        String pageNum = "&pageNo=" + page; // 페이지
        String row = "&numOfRows=100"; // 데이터 개수 : 10
        String service = "&serviceKey=" + serviceKey;

        ObjectMapper objectMapper = new ObjectMapper();
        StringBuffer result = new StringBuffer();

        try {
            // 지역 기반 관광 정보 조회 api
            URL localUrl = new URL(defaultURL + "areaBasedList1?" + defaultURL2 + service +
                    "&contentTypeId=12&arrange=R" + area + pageNum + row);
            HttpURLConnection localUrlConnection = (HttpURLConnection) localUrl.openConnection();
            localUrlConnection.setRequestMethod("GET");
            localUrlConnection.setRequestProperty("Content-type", "application/json");

            // BufferedReader로 응답을 UTF-8로 읽어오기
            BufferedReader bf = new BufferedReader(new InputStreamReader(localUrlConnection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }

            // JSON 파싱
            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONObject responseBody = jsonResponse.getJSONObject("response").getJSONObject("body");
            JSONArray items = responseBody.getJSONObject("items").getJSONArray("item");


            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Location location = objectMapper.readValue(item.toString(), Location.class);

                // Location 엔티티 생성
                location.setArea(areaRepository.findByAreaCode(item.getLong("areacode")));

                // Location 설명, 전화번호 추가
                setDescriptionAndPhoneNumber(location);

                // 리스트에 추가
                locationList.add(location);
            }

            // 장소 리스트 DB에 저장
            locationRepository.saveAll(locationList);
            log.info("Locations inserted successfully into the database.");
        }
        catch (IOException e) {
            log.error("An error occurred while parsing and saving location data: ", e);
        }
    }

    public void setDescriptionAndPhoneNumber(Location location) {

        String service = "&serviceKey=" + serviceKey;
        Long contentId = location.getContentId();

        // 공통 정보 조회 api
        StringBuffer infoResult = new StringBuffer();

        try {
            URL infoUrl = new URL(defaultURL + "detailCommon1?" + defaultURL2 +
                    service + "&defaultYN=Y&overviewYN=Y&contentId=" + contentId.toString());

            HttpURLConnection infoUrlConnection = (HttpURLConnection) infoUrl.openConnection();
            infoUrlConnection.setRequestMethod("GET");
            infoUrlConnection.setRequestProperty("Content-type", "application/json");

            BufferedReader infoBf = new BufferedReader(new InputStreamReader(infoUrlConnection.getInputStream(), StandardCharsets.UTF_8));

            String infoline;
            while ((infoline = infoBf.readLine()) != null) {
                infoResult.append(infoline);
            }

            // JSON 파싱
            // JSONObject로 변환
            JSONObject infoJsonResponse = new JSONObject(infoResult.toString());
            JSONObject infoResponseBody = infoJsonResponse.getJSONObject("response").getJSONObject("body");
            JSONObject infoItem = infoResponseBody.getJSONObject("items").getJSONArray("item").getJSONObject(0);

//            String phone = infoItem.getString("tel");
            String description = infoItem.optString("overview", "설명 없음");
            if (description.length() > 255) {
                description = description.substring(0, 255);
            }

            location.setDescription(description);
//            location.setPhoneNumber(phone);
        }
        catch (IOException e) {
            log.error("An error occurred while parsing and saving Desciption And PhoneNumber data: ", e);
        }
    }

    public void setDisabled(List<Location> locationList, List<Disabled> disabledList) {

        String service = "&serviceKey=" + serviceKey;

        ObjectMapper objectMapper = new ObjectMapper();

        // 무장애 여행 조회 API
        try {
            for (int i = 0; i < locationList.size(); i++) {
                StringBuffer disabledResult = new StringBuffer();

                Long contentId = locationList.get(i).getContentId();
                URL disabledURL = new URL(defaultURL + "detailWithTour1?" + defaultURL2 +
                        service + "&contentId=" + contentId.toString());

                HttpURLConnection disabledUrlConnection = (HttpURLConnection) disabledURL.openConnection();
                disabledUrlConnection.setRequestMethod("GET");
                disabledUrlConnection.setRequestProperty("Content-type", "application/json");

                BufferedReader disabledBf = new BufferedReader(new InputStreamReader(disabledUrlConnection.getInputStream(), StandardCharsets.UTF_8));

                String disabledline;
                while ((disabledline = disabledBf.readLine()) != null) {
                    disabledResult.append(disabledline);
                }

                // JSON 파싱
                JSONObject disabledJsonResponse = new JSONObject(disabledResult.toString());
                JSONObject disabledResponseBody = disabledJsonResponse.getJSONObject("response").getJSONObject("body");
                JSONObject disabledItem = disabledResponseBody.getJSONObject("items").getJSONArray("item").getJSONObject(0);

                Disabled disabled = objectMapper.readValue(disabledItem.toString(), Disabled.class);
                disabledList.add(disabled);
            }

            // 장애 정보 리스트 DB에 저장
            disabledRepository.saveAll(disabledList);
            log.info("Locations inserted successfully into the database.");
        }
        catch (IOException e)
        {
            log.error("An error occurred while parsing and saving Disabled data: ", e);
        }
    }
}
