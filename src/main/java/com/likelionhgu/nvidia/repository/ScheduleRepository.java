package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByEnPhoneNumber(String enPhoneNumber);
    Schedule findByEnPhoneNumberAndDate(String enPhoneNumber, LocalDate date);
    Schedule findByRoomIdAndDate(Long roomId, LocalDate date);
}
