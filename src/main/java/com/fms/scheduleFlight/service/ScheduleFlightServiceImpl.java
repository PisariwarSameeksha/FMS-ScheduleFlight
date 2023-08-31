package com.fms.scheduleFlight.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import com.fms.scheduleFlight.DTO.FlightDTO;
import com.fms.scheduleFlight.DTO.ScheduleFlightDTO;
import com.fms.scheduleFlight.entity.ScheduleFlight;
import com.fms.scheduleFlight.exception.FlightNotFoundException;
import com.fms.scheduleFlight.exception.NoScheduleListFoundException;
import com.fms.scheduleFlight.exception.ScheduleAlreadyExistsException;
import com.fms.scheduleFlight.exception.ScheduleNotFoundException;
import com.fms.scheduleFlight.repository.ScheduleFlightRepository;

import reactor.core.publisher.Mono;

@Service
public class ScheduleFlightServiceImpl implements ScheduleFlightService {

	@Autowired
	private ScheduleFlightRepository scheduleFlightRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WebClient webclient;

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public String scheduledFlight(Long flightId, ScheduleFlightDTO scheduledFlightDTO)
			throws FlightNotFoundException, ScheduleAlreadyExistsException {

		Mono<FlightDTO> response = webclient.get().uri("http://localhost:8092/api/flight/flights/{fnum}", flightId)
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(FlightDTO.class).log();

		FlightDTO flightDTO = response.block();

		if (flightDTO == null) {
			throw new FlightNotFoundException("!! Flight Not Found !!");
		}

		Optional<ScheduleFlight> existingSchedule = scheduleFlightRepository
				.findByDepartureTime(scheduledFlightDTO.getDepartureTime());

		if (existingSchedule.equals(scheduledFlightDTO.getDepartureTime())) {
			throw new ScheduleAlreadyExistsException("!! Schedule Already Exists at this Departure Time !!");
		}

		ScheduleFlight scheduleFlight = modelMapper.map(scheduledFlightDTO, ScheduleFlight.class);
		scheduleFlight.setFlightId(flightId);
		scheduleFlightRepository.save(scheduleFlight);
		return "Schedule Added Successfully";
	}

	@Override
	public String modifyScheduledFlight(ScheduleFlightDTO scheduleFlightDTO, Long scheduleId)
			throws ScheduleNotFoundException {
		Optional<ScheduleFlight> existingSchedule = this.scheduleFlightRepository.findById(scheduleId);
		if (existingSchedule.isEmpty()) {
			throw new ScheduleNotFoundException("Schedule does not exist.");
		}
		ScheduleFlight scheduleFlight = existingSchedule.get();
		scheduleFlight.setScheduleId(scheduleFlight.getScheduleId());
		scheduleFlight.setFlightId(scheduleFlight.getFlightId());
		scheduleFlight.setArrivalTime(scheduleFlightDTO.getArrivalTime());
		scheduleFlight.setDepartureTime(scheduleFlightDTO.getDepartureTime());
		scheduleFlight.setDestinationAirport(scheduleFlightDTO.getDestinationAirport());
		scheduleFlight.setSourceAirport(scheduleFlightDTO.getSourceAirport());
		scheduleFlight.setPrice(scheduleFlightDTO.getPrice());
		scheduleFlightRepository.save(scheduleFlight);
		return "Schedule Modified Successfully";
	}

	@Override
	public String deleteScheduledFlight(Long scheduleId) throws ScheduleNotFoundException {

		Optional<ScheduleFlight> optSchedule = this.scheduleFlightRepository.findById(scheduleId);
		if (optSchedule.isEmpty()) {
			throw new ScheduleNotFoundException("Schedule does not exist");
		}
		scheduleFlightRepository.deleteById(scheduleId);
		return "Schedule Deleted Successfully";
	}

	@Override
	public ScheduleFlightDTO viewSchedule(Long scheduleId) throws ScheduleNotFoundException {
		Optional<ScheduleFlight> optSchedule = this.scheduleFlightRepository.findById(scheduleId);
		if (optSchedule.isEmpty()) {
			throw new ScheduleNotFoundException("Schedule does not exist.");
		}
		ScheduleFlight scheduleFlight = optSchedule.get();
		return modelMapper.map(scheduleFlight, ScheduleFlightDTO.class);
	}

	@Override
	public List<ScheduleFlightDTO> viewAllScheduledFlights() throws NoScheduleListFoundException {

		List<ScheduleFlight> scheduleFlight = scheduleFlightRepository.findAll();
		if (scheduleFlight.isEmpty()) {
			throw new NoScheduleListFoundException("!! No Schedule List Found !!");
		}
		return scheduleFlight.stream().map(list -> modelMapper.map(list, ScheduleFlightDTO.class))
				.toList();
	}

	@Override
	public List<ScheduleFlight> getSchedulesByFlightId(Long flightId) {
		List<ScheduleFlight> optFlight = new ArrayList<>();
		scheduleFlightRepository.findByFlightId(flightId).forEach(optFlight::add);
		return optFlight;
	}

	@Override
	public String deleteScheduledFlightsByFlightId(Long flightId) {
		List<ScheduleFlight> optFlight = new ArrayList<>();
		scheduleFlightRepository.findByFlightId(flightId).forEach(optFlight::add);
		for (ScheduleFlight schedule : optFlight) {
			scheduleFlightRepository.delete(schedule);
		}
		return "deleted!";
	}

}
