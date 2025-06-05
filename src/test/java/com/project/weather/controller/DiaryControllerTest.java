package com.project.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DiaryControllerTest {

    private final String date = "2000-05-31";
    private final String startDate = "2000-01-01";
    private final String endDate = "2000-06-20";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST - Create - 정상")
    void createDiary() throws Exception {
        mockMvc.perform(post("/create/diary?date=" + date)
                        .content("createDiary 테스트용 일기입니다.")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get - Diary - 정상")
    void getDiary() throws Exception {
        mockMvc.perform(post("/create/diary?date=" + date)
                        .content("getDiary 테스트용 일기입니다.")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        mockMvc.perform(get("/read/diary?date=" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(date))
                .andExpect(jsonPath("$[0].text").value("getDiary 테스트용 일기입니다."));
    }

    @Test
    @DisplayName("Get - Diaries - 정상")
    void getDiaries() throws Exception {
        String date2 = "2000-03-05";
        mockMvc.perform(post("/create/diary?date=" + date)
                        .content("getDiaries 테스트용 일기입니다. 1")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
        mockMvc.perform(post("/create/diary?date=" + date2)
                        .content("getDiary 테스트용 일기입니다. 2")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        mockMvc.perform(get("/read/diaries?startDate=" + startDate + "&endDate=" + endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT - Update - 정상")
    void updateDiary() throws Exception {
        mockMvc.perform(post("/create/diary?date=" + date)
                        .content("updateDiary 테스트용 일기입니다.")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        mockMvc.perform(put("/update/diary?date=" + date)
                        .content("updateDiary 호출 후 일기입니다.")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());


        mockMvc.perform(get("/read/diary?date=" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(date))
                .andExpect(jsonPath("$[0].text").value("updateDiary 호출 후 일기입니다."));
    }

    @Test
    @DisplayName("DELETE - delete - 정상")
    void deleteDiary() throws Exception {
        mockMvc.perform(post("/create/diary?date=" + date)
                        .content("updateDiary 테스트용 일기입니다.")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/delete/diary?date=" + date))
                .andExpect(status().isOk());

        mockMvc.perform(get("/read/diary?date=" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}