package com.project.weather.repository;

import com.project.weather.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByDate(LocalDate date);

    List<Diary> findAllByDateBetween(LocalDate start, LocalDate end);

    Diary findFirstByDate(LocalDate date);

    void deleteAllByDate(LocalDate date);
}
