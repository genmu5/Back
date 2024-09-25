package com.example.BEF.DataSetting.Service;

import com.example.BEF.Area.Domain.Area;
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
public class SettingAreaService {
    @Value("${openapi.servicekey}")
    private String serviceKey;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(SettingAreaService.class);

    public void settingAreaData() {
        StringBuffer result = new StringBuffer();

        try {
            // API URL 설정
            URL url = new URL("https://apis.data.go.kr/B551011/KorWithService1/areaCode1?serviceKey=" + serviceKey + "&MobileOS=ETC&MobileApp=BEF&numOfRows=20&_type=json");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");


            // BufferedReader로 응답을 UTF-8로 읽어오기
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }

            // 읽은 결과 로그 출력 (확인용)
            log.info("API Response: {}", result.toString());

            // JSON 파싱 (JSON 형태를 가정)
            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONObject responseBody = jsonResponse.getJSONObject("response").getJSONObject("body");
            JSONObject items = responseBody.getJSONObject("items");
            JSONArray itemArray = items.getJSONArray("item");

            // 'code'와 'name' 추출
            List<Area> areaList = new ArrayList<>();
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);
                String code = item.getString("code");
                String name = item.getString("name");

                log.info("Code: {}", code);
                log.info("Name: {}", name);

                // Area 객체 생성 및 리스트에 추가
                Area area = new Area(Long.parseLong(code), name);
                areaList.add(area);
            }

            // 데이터 DB 삽입 (여기서는 생략)
            insertAreasIntoDB(areaList);

        } catch (Exception e) {
            log.error("An error occurred while calling the API or processing data: ", e);
        }
    }

    private void insertAreasIntoDB(List<Area> areaList) {
        String sql = "INSERT INTO area (area_code, area_name) VALUES (?, ?)";

        for (Area area : areaList) {
            jdbcTemplate.update(sql, area.getAreaCode(), area.getAreaName());
        }

        // 삽입 완료 로그 출력
        System.out.println("Areas inserted successfully into the database.");
    }
}
