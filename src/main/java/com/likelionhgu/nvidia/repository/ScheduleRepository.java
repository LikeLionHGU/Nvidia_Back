package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByDate(LocalDate date);
}
