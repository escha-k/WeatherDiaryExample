package com.project.weather.service;

import com.project.weather.WeatherDiaryProjectApplication;
import com.project.weather.domain.DateWeather;
import com.project.weather.domain.Diary;
import com.project.weather.repository.DateWeatherRepository;
import com.project.weather.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeatherDiaryProjectApplication.class);

    @Value("${openweathermap.key}")
    private String apiKey;

    @Override
    public void createDiary(LocalDate date, String text) {
        logger.info("Started to create diary");

        DateWeather dateWeather = getDateWeather(date);

        Diary diary = Diary.builder()
                .weather(dateWeather.getWeather())
                .icon(dateWeather.getIcon())
                .temperature(dateWeather.getTemperature())
                .text(text)
                .date(date)
                .build();

        diaryRepository.save(diary);

        logger.info("Finished to create diary");
    }

    private DateWeather getDateWeather(LocalDate date) {
        return dateWeatherRepository.findById(date).orElse(getWeatherFromApi());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Diary> getDiary(LocalDate date) {
        logger.debug("Read diary");

        List<Diary> diaries = diaryRepository.findAllByDate(date);

        return diaries;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Diary> getDiaries(LocalDate startDate, LocalDate endDate) {
        List<Diary> diaries = diaryRepository.findAllByDateBetween(startDate, endDate);

        return diaries;
    }

    @Override
    public void updateDiary(LocalDate date, String text) {
        // 일기장 가져오기
        Diary updatedDiary = diaryRepository.findFirstByDate(date);

        // 일기 내용 수정
        updatedDiary.setText(text);
    }

    @Override
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        logger.info("날씨 데이터 자동 저장");
        dateWeatherRepository.save(getWeatherFromApi());
    }

    private DateWeather getWeatherFromApi() {
        // openweathermap 에서 날씨 데이터 가져오기
        String weatherApiString = getWeatherString();

        // 받아온 날씨 json 파싱
        Map<String, Object> parsedMap = parseWeather(weatherApiString);

        String weather = parsedMap.get("main").toString();
        String icon = parsedMap.get("icon").toString();
        Double temperature = (Double) parsedMap.get("temp");

        DateWeather dateWeather = DateWeather.builder()
                .date(LocalDate.now())
                .weather(weather)
                .icon(icon)
                .temperature(temperature)
                .build();

        return dateWeather;
    }

    private Map<String, Object> parseWeather(String weatherApiString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(weatherApiString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject weatherData = (JSONObject) ((JSONArray) jsonObject.get("weather")).get(0);
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("main", weatherData.get("main")); // 날씨
        resultMap.put("icon", weatherData.get("icon")); // 아이콘
        resultMap.put("temp", mainData.get("temp")); // 온도

        return resultMap;
    }

    private String getWeatherString() {
        String getWeatherApiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=" + apiKey;

        // api 호출
        try {
            URL url = new URL(getWeatherApiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            br.close();
            return stringBuilder.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
