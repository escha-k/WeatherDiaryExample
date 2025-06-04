package com.project.weather.service;

import com.project.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

public interface DiaryService {

    void createDiary(LocalDate date, String text);

    void updateDiary(LocalDate date, String text);

    void deleteDiary(LocalDate date);

    List<Diary> getDiary(LocalDate date);

    List<Diary> getDiaries(LocalDate startDate, LocalDate endDate);
}
