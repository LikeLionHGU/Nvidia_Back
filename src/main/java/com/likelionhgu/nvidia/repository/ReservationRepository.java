package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPhoneNumberOrderByDateAsc(String PhoneNumber);

    List<Reservation> findByRoomIdAndDateBetween(Long roomId, LocalDate dateAfter, LocalDate dateBefore);

    List<Reservation> findByRoomIdAndDate(Long roomId, LocalDate date);

}
