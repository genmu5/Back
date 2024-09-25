package com.example.BEF.DataSetting.Service;

import com.example.BEF.Area.Domain.Area;
import com.example.BEF.Area.Service.AreaRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Service.LocationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettingLocationService {
    @Value("${openapi.servicekey}")
    private String serviceKey;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(SettingLocationService.class);

    public void fetchAndInsertLocationData() {
        StringBuffer result = new StringBuffer();

        try {
            // 지역 기반 관광 정보 조회 api[제주 - 10개]
            URL localUrl = new URL("https://apis.data.go.kr/B551011/KorWithService1/areaBasedList1?serviceKey=" + serviceKey + "&MobileOS=ETC&MobileApp=BEF&pageNo=1&contentTypeId=12&arrange=R&areaCode=39&_type=json&numOfRows=10");
            HttpURLConnection localUrlConnection = (HttpURLConnection) localUrl.openConnection();
            localUrlConnection.setRequestMethod("GET");
            localUrlConnection.setRequestProperty("Content-type", "application/json");

            // BufferedReader로 응답을 UTF-8로 읽어오기
            BufferedReader bf = new BufferedReader(new InputStreamReader(localUrlConnection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }

            // 읽은 결과 로그 출력 (확인용)
            log.info("API Response: {}", result.toString());

            // JSON 파싱
            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONObject responseBody = jsonResponse.getJSONObject("response").getJSONObject("body");
            JSONArray items = responseBody.getJSONObject("items").getJSONArray("item");

            List<Location> locationList = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                Long contentId = item.getLong("contentid");
                Long areaCode = item.getLong("areacode");
                Long contentTypeId = item.getLong("contenttypeid");
                String contentTitle = item.getString("title");
                String addr = item.getString("addr1");
                String detailAddr = item.optString("addr2", "");  // 빈 값일 수 있음
                Double gpsX = item.getDouble("mapx");
                Double gpsY = item.getDouble("mapy");
                String originalImage = item.optString("firstimage", "");
                String thumbnailImage = item.optString("firstimage2", "");

                // Area 엔티티를 찾는다
                Area area = areaRepository.findByAreaCode(areaCode);
                if (area == null) {
                    log.error("Area with code {} not found", areaCode);
                    continue;  // Area가 없으면 해당 Location은 건너뜀
                }

                // 공통 정보 조회 api
                StringBuffer infoResult = new StringBuffer();

                URL infoUrl = new URL("https://apis.data.go.kr/B551011/KorWithService1/detailCommon1?serviceKey=" + serviceKey + "&defaultYN=Y&MobileOS=ETC&MobileApp=BEF&overviewYN=Y&_type=json&contentId=" + contentId.toString());

                log.info(infoUrl.toString());
                HttpURLConnection infoUrlConnection = (HttpURLConnection) infoUrl.openConnection();
                infoUrlConnection.setRequestMethod("GET");
                infoUrlConnection.setRequestProperty("Content-type", "application/json");

                BufferedReader infoBf = new BufferedReader(new InputStreamReader(infoUrlConnection.getInputStream(), StandardCharsets.UTF_8));

                String infoline;
                while ((infoline = infoBf.readLine()) != null) {
                    infoResult.append(infoline);
                }

                // 읽은 결과 로그 출력 (확인용)
                log.info("API Response: {}", infoResult.toString());

                // JSON 파싱
                JSONObject infoJsonResponse = new JSONObject(infoResult.toString());
                JSONObject infoResponseBody = infoJsonResponse.getJSONObject("response").getJSONObject("body");
                JSONObject infoItem = infoResponseBody.getJSONObject("items").getJSONArray("item").getJSONObject(0);

                String description = infoItem.optString("overview", "설명 없음");
                if (description.length() > 255) {
                    description = description.substring(0, 255);
                }
//                String phone = infoItem.getString("tel"); // 존재하지 않나...?

                // Location 엔티티 생성
                Location location = new Location();
                location.setContentId(contentId);
                location.setArea(area);
                location.setContentTypeId(contentTypeId);
                location.setContentTitle(contentTitle);
                location.setAddr(addr);
                location.setDetailAddr(detailAddr);
                location.setGpsX(gpsX);
                location.setGpsY(gpsY);
                location.setOriginalImage(originalImage);
                location.setThumbnailImage(thumbnailImage);
                location.setDescription(description);
//                location.setPhoneNumber(phone);


                // 리스트에 추가
                locationList.add(location);
            }

            // DB에 저장
            locationRepository.saveAll(locationList);
            log.info("Locations inserted successfully into the database.");

        } catch (Exception e) {
            log.error("An error occurred while parsing and saving location data: ", e);
        }
    }
}
