package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
