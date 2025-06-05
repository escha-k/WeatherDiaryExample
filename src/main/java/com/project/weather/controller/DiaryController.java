package com.project.weather.controller;

import com.project.weather.domain.Diary;
import com.project.weather.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "날씨 일기를 생성하는 POST 메서드", description = "일기 텍스트와 날씨를 이용해서 DB에 일기를 저장합니다.")
    @PostMapping("/create/diary")
    public void createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text
    ) {
        diaryService.createDiary(date, text);
    }

    @Operation(summary = "해당 날짜의 일기들을 가져오는 GET 메서드", description = "선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    public List<Diary> getDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return diaryService.getDiary(date);
    }

    @Operation(summary = "특정 범위 날짜의 일기들을 가져오는 GET 메서드")
    @GetMapping("/read/diaries")
    public List<Diary> getDiaries(
            @Parameter(description = "조회할 기간의 첫날", example = "2020-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate startDate,
            @Parameter(description = "조회할 기간의 마지막날", example = "2020-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return diaryService.getDiaries(startDate, endDate);
    }

    @Operation(summary = "해당 날짜의 첫 번째 일기를 수정하는 PUT 메서드")
    @PutMapping("/update/diary")
    public void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "해당 날짜의 모든 일기 데이터를 삭제하는 DELETE 메서드")
    @DeleteMapping("/delete/diary")
    public void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        diaryService.deleteDiary(date);
    }
}
