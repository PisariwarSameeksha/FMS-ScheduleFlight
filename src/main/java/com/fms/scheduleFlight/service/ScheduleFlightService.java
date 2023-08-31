package com.fms.scheduleFlight.service;

import java.util.List;

import com.fms.scheduleFlight.DTO.ScheduleFlightDTO;
import com.fms.scheduleFlight.entity.ScheduleFlight;
import com.fms.scheduleFlight.exception.FlightNotFoundException;
import com.fms.scheduleFlight.exception.NoScheduleListFoundException;
import com.fms.scheduleFlight.exception.ScheduleAlreadyExistsException;
import com.fms.scheduleFlight.exception.ScheduleNotFoundException;

public interface ScheduleFlightService {
	
	String scheduledFlight(Long flightId, ScheduleFlightDTO scheduledFlightDTO) throws FlightNotFoundException, ScheduleAlreadyExistsException;
	
	String modifyScheduledFlight(ScheduleFlightDTO scheduleFlightDTO, Long scheduleId) throws ScheduleNotFoundException;
	
	String deleteScheduledFlight (Long scheduleId) throws ScheduleNotFoundException;
	
	ScheduleFlightDTO viewSchedule(Long scheduleId) throws ScheduleNotFoundException;
	
	List<ScheduleFlightDTO> viewAllScheduledFlights() throws NoScheduleListFoundException;
	
	List<ScheduleFlight> getSchedulesByFlightId(Long flightId);

	String deleteScheduledFlightsByFlightId(Long flightId);

}
