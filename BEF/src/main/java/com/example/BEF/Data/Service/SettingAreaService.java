package com.example.BEF.Data.Service;

import com.example.BEF.Area.Domain.Area;
import com.example.BEF.Area.Repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingAreaService {

    @Value("${openapi.service-key}")
    private String serviceKey;

    private final AreaRepository areaRepository;

    String defaultURL = "https://apis.data.go.kr/B551011/KorWithService1/";
    String defaultURL2 = "&MobileOS=IOS&MobileApp=BEF&_type=json";

    private static final Logger log = LoggerFactory.getLogger(SettingAreaService.class);

    public void settingAreaData() {

        String service = "&serviceKey=" + serviceKey;

        StringBuffer result = new StringBuffer();

        try {
            // API URL 설정
            URL url = new URL(defaultURL + "areaCode1?" + service + defaultURL2 + "numOfRows=20");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");


            // BufferedReader로 응답을 UTF-8로 읽어오기
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }

            // JSON 파싱 (JSON 형태를 가정)
            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONObject responseBody = jsonResponse.getJSONObject("response").getJSONObject("body");
            JSONArray items = responseBody.getJSONObject("items").getJSONArray("item");

            // 'code'와 'name' 추출
            List<Area> areaList = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String code = item.getString("code");
                String name = item.getString("name");

                log.info("Code: {}", code);
                log.info("Name: {}", name);

                // Area 객체 생성 및 리스트에 추가
                Area area = new Area(Long.parseLong(code), name);
                areaList.add(area);
            }

            // 데이터 DB 삽입 (여기서는 생략)
            areaRepository.saveAll(areaList);
            log.info("Areas inserted successfully into the database.");

        } catch (Exception e) {
            log.error("An error occurred while calling the API or processing data: ", e);
        }
    }
}
