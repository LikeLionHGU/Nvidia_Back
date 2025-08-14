package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
