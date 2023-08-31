package com.fms.scheduleFlight.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.scheduleFlight.DTO.ScheduleFlightDTO;
import com.fms.scheduleFlight.entity.ScheduleFlight;
import com.fms.scheduleFlight.exception.FlightNotFoundException;
import com.fms.scheduleFlight.exception.NoScheduleListFoundException;
import com.fms.scheduleFlight.exception.ScheduleAlreadyExistsException;
import com.fms.scheduleFlight.exception.ScheduleNotFoundException;
import com.fms.scheduleFlight.service.ScheduleFlightService;

@RestController
@RequestMapping("/api/scheduleFlight")
@CrossOrigin(origins = "http://localhost:4200")
public class ScheduleFlightController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleFlightController.class);

	@Autowired
	private ScheduleFlightService scheduleFlightService;

	@PostMapping("/scheduleFlight/{fid}")
	public ResponseEntity<String> scheduleFlight(@PathVariable("fid") Long fid,
			@Valid @RequestBody ScheduleFlightDTO scheduleFlightDTO) {
		try {
			logger.info("Received request schedule a flight: {}", scheduleFlightDTO);
			scheduleFlightService.scheduledFlight(fid, scheduleFlightDTO);
			logger.info("Schedule added: {}", scheduleFlightDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body("Scheduled!!");
		} catch (FlightNotFoundException e) {
			logger.warn("Flight with id {} not found", fid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" Flight Not Found !!");
		} catch (ScheduleAlreadyExistsException e) {
			logger.warn("Schedule with id {} Already exists", scheduleFlightDTO.getScheduleId());
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body("!! Schedule Already Exists at this Departure Time !!");
		}
	}

	@PutMapping("/modifySchedule/{sid}")
	ResponseEntity<String> modifySchedule(@PathVariable("sid") Long sid,
			@Valid @RequestBody ScheduleFlightDTO scheduleFlightDTO) {
		try {

			logger.info("Received request to modify schedule with id: {}", sid);
			scheduleFlightService.modifyScheduledFlight(scheduleFlightDTO, sid);
			logger.info("Schedule with id {} modified successfully", sid);
			return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully!!");
		} catch (ScheduleNotFoundException e) {
			logger.warn("Flight with id {} not found for modification", sid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule does not exist!!");
		}
	}

	@DeleteMapping("/deleteSchedule/{sid}")
	ResponseEntity<String> deleteSchedule(@PathVariable("sid") Long sid) {
		try {
			logger.info("Received request to delete schedule with id: {}", sid);
			scheduleFlightService.deleteScheduledFlight(sid);
			logger.info("Schedule with id {} deleted successfully", sid);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("!!Deleted!!");
		} catch (ScheduleNotFoundException e) {
			logger.warn("schedule with id {} not found", sid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("!!Flight not Scheduled Yet!!");
		}
	}

	@GetMapping("/schedule/{sid}")
	ResponseEntity<ScheduleFlightDTO> getSchedule(@PathVariable("sid") Long sid) throws ScheduleNotFoundException {
		try {
			logger.info("Received request to fetch schedule with id: {}", sid);
			ScheduleFlightDTO scheduleFlightDTO = scheduleFlightService.viewSchedule(sid);
			logger.info("Schedule with id {} fetched successfully", sid);
			return ResponseEntity.status(HttpStatus.OK).body(scheduleFlightDTO);
		} catch (ScheduleNotFoundException e) {
			logger.warn("schedule with id {} not found", sid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/flightSchedule/{fid}")
	ResponseEntity<Iterable<ScheduleFlight>> getScheduleOfFlight(@PathVariable("fid") Long fid) {
		logger.info("Received request to fetch all schedules");
		List<ScheduleFlight> schedules = scheduleFlightService.getSchedulesByFlightId(fid);
		logger.info("Fetched {} schedules", schedules.size());
		return ResponseEntity.status(HttpStatus.OK).body(schedules);
	}

	@GetMapping("/schedules")
	ResponseEntity<Iterable<ScheduleFlightDTO>> getAllSchedules() throws NoScheduleListFoundException {
		logger.info("Received request to fetch all schedules");
		List<ScheduleFlightDTO> schedules = scheduleFlightService.viewAllScheduledFlights();
		logger.info("Fetched {} schedules", schedules.size());
		return ResponseEntity.status(HttpStatus.OK).body(schedules);
	}

	@DeleteMapping("/allSchedules/{fid}")
	ResponseEntity<String> deleteAllSchedulesByFlightId(@PathVariable("fid") Long fid) {
		logger.info("Received request to delete all schedules by flight id");
		scheduleFlightService.deleteScheduledFlightsByFlightId(fid);
		logger.info("Schedules with id {} deleted successfully", fid);
		return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully!!");

	}
}
