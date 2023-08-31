package com.fms.scheduleFlight.repository;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fms.scheduleFlight.entity.ScheduleFlight;


@Repository
public interface ScheduleFlightRepository extends JpaRepository<ScheduleFlight, Long>{

	Optional<ScheduleFlight> findByDepartureTime(LocalDateTime localDateTime);

	Iterable<ScheduleFlight> findByFlightId(Long flightId);

	void save(Optional<ScheduleFlight> existingSchedule);


}
