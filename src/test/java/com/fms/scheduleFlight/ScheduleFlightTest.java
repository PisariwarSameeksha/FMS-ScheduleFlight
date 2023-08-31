package com.fms.scheduleFlight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.reactive.function.client.WebClient;

import com.fms.scheduleFlight.DTO.ScheduleFlightDTO;
import com.fms.scheduleFlight.controller.ScheduleFlightController;
import com.fms.scheduleFlight.entity.ScheduleFlight;
import com.fms.scheduleFlight.exception.NoScheduleListFoundException;
import com.fms.scheduleFlight.exception.ScheduleNotFoundException;
import com.fms.scheduleFlight.repository.ScheduleFlightRepository;
import com.fms.scheduleFlight.service.ScheduleFlightServiceImpl;

@ExtendWith(MockitoExtension.class)
class ScheduleFlightTest {

	@InjectMocks
	private ScheduleFlightServiceImpl scheduleFlightService;

	@Mock
	private ScheduleFlightRepository scheduleFlightRepository;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private ScheduleFlightController scheduleFlightController;

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

	@BeforeEach
	void seUp() {
		MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() {
		Mockito.reset(scheduleFlightRepository, modelMapper);
	}

	@Order(1)
	@Test
	void testViewScheduleException() throws ScheduleNotFoundException {
		assertThrows(ScheduleNotFoundException.class, () -> scheduleFlightService.viewSchedule(7L));
	}

	@Order(2)
	@Test
	void testViewScheduleException1() throws ScheduleNotFoundException {
		assertThrows(ScheduleNotFoundException.class, () -> scheduleFlightService.viewSchedule(90L));
	}

	@Order(4)
	@Test
	void testDeleteScheduledFlight_Success() throws ScheduleNotFoundException {
		MockitoAnnotations.openMocks(this);

		Long scheduleId = 7L;
		when(scheduleFlightRepository.findById(scheduleId)).thenReturn(Optional.of(new ScheduleFlight()));

		String result = scheduleFlightService.deleteScheduledFlight(scheduleId);

		assertEquals("Schedule Deleted Successfully", result);
		verify(scheduleFlightRepository, times(1)).findById(scheduleId);
		verify(scheduleFlightRepository, times(1)).deleteById(scheduleId);
	}

	@Order(5)
	@Test
	void testDeleteScheduledFlight_ScheduleNotFound() {
		MockitoAnnotations.openMocks(this);

		Long scheduleId = 1234L;
		when(scheduleFlightRepository.findById(scheduleId)).thenReturn(Optional.empty());

		assertThrows(ScheduleNotFoundException.class, () -> scheduleFlightService.deleteScheduledFlight(scheduleId));

		verify(scheduleFlightRepository, times(1)).findById(scheduleId);
		verify(scheduleFlightRepository, never()).deleteById(scheduleId);
	}
//	@Order(6)
//	@Test
//	void testDeleteScheduledFlight_NullScheduleId() {
//		MockitoAnnotations.openMocks(this);
//
//		assertThrows(IllegalArgumentException.class, () -> scheduleFlightService.deleteScheduledFlight(null));
//
//		verify(scheduleFlightRepository, never()).findById(any(Long.class));
//		verify(scheduleFlightRepository, never()).deleteById(any(Long.class));
//	}


	@Order(7)
	@Test
	void testDeleteScheduledFlight_DeleteFailure() {
		MockitoAnnotations.openMocks(this);

		Long scheduleId = 123L;
		when(scheduleFlightRepository.findById(scheduleId)).thenReturn(Optional.of(new ScheduleFlight()));
		doThrow(RuntimeException.class).when(scheduleFlightRepository).deleteById(scheduleId);

		assertThrows(RuntimeException.class, () -> scheduleFlightService.deleteScheduledFlight(scheduleId));

		verify(scheduleFlightRepository, times(1)).findById(scheduleId);
		verify(scheduleFlightRepository, times(1)).deleteById(scheduleId);
	}

	@Order(8)
	@Test
	void testDeleteScheduledFlight_EmptyRepository() {
		MockitoAnnotations.openMocks(this);

		Long scheduleId = 123L;
		when(scheduleFlightRepository.findById(scheduleId)).thenReturn(Optional.empty());

		assertThrows(ScheduleNotFoundException.class, () -> scheduleFlightService.deleteScheduledFlight(scheduleId));

		verify(scheduleFlightRepository, times(1)).findById(scheduleId);
		verify(scheduleFlightRepository, never()).deleteById(scheduleId);
	}

	@Order(9)
	@Test
	void testDeleteScheduledFlightScheduleNotFoundException() {
		Long scheduledId = 123L;
		when(scheduleFlightRepository.findById(scheduledId)).thenReturn(Optional.empty());
		assertThrows(ScheduleNotFoundException.class, () -> scheduleFlightService.deleteScheduledFlight(scheduledId));
		verify(scheduleFlightRepository, never()).deleteById(scheduledId);
	}

	@Order(10)
	@Test
	void testViewAllScheduledFlightsSuccess() throws NoScheduleListFoundException {
		List<ScheduleFlight> scheduleFlightList = new ArrayList<>();
		scheduleFlightList.add(new ScheduleFlight());
		scheduleFlightList.add(new ScheduleFlight());
		when(scheduleFlightRepository.findAll()).thenReturn(scheduleFlightList);
		ScheduleFlightDTO scheduleFlightDTO1 = new ScheduleFlightDTO();
		ScheduleFlightDTO scheduleFlightDTO2 = new ScheduleFlightDTO();
		when(modelMapper.map(scheduleFlightList.get(0), ScheduleFlightDTO.class)).thenReturn(scheduleFlightDTO1);
		when(modelMapper.map(scheduleFlightList.get(1), ScheduleFlightDTO.class)).thenReturn(scheduleFlightDTO2);
		List<ScheduleFlightDTO> result = scheduleFlightService.viewAllScheduledFlights();
		assertEquals(2, result.size());
		assertEquals(scheduleFlightDTO1, result.get(0));
		assertEquals(scheduleFlightDTO2, result.get(1));
	}

	@Order(11)
	@Test
    void testViewAllScheduledFlightsNoScheduleListFoundException() {
        when(scheduleFlightRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoScheduleListFoundException.class,
               () -> scheduleFlightService.viewAllScheduledFlights());
   }

	@Order(12)
	@Test
	void testViewScheduleSuccess() throws ScheduleNotFoundException {
		Long scheduleId = 1L;
		ScheduleFlight scheduleFlight = new ScheduleFlight();
		when(scheduleFlightRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleFlight));
		ScheduleFlightDTO expectedScheduleFlightDTO = new ScheduleFlightDTO();
		when(modelMapper.map(scheduleFlight, ScheduleFlightDTO.class)).thenReturn(expectedScheduleFlightDTO);
		ScheduleFlightDTO result = scheduleFlightService.viewSchedule(scheduleId);
		assertEquals(expectedScheduleFlightDTO, result);
	}

	@Order(13)
	@Test
	void testViewScheduleScheduleNotFoundException() {
		Long scheduleId = 12L;
		when(scheduleFlightRepository.findById(scheduleId)).thenReturn(Optional.empty());
		assertThrows(ScheduleNotFoundException.class, () -> scheduleFlightService.viewSchedule(scheduleId));
		verify(modelMapper, never()).map(any(), any());
	}

	@Order(14)
	@Test
	void testGetSchedulesByFlightId_NoSchedulesFound() {
		MockitoAnnotations.openMocks(this);

		Long flightId = 123L;
		when(scheduleFlightRepository.findByFlightId(flightId)).thenReturn(new ArrayList<>());

		List<ScheduleFlight> result = scheduleFlightService.getSchedulesByFlightId(flightId);

		assertTrue(result.isEmpty());
		verify(scheduleFlightRepository, times(1)).findByFlightId(flightId);
	}

	@Order(15)
	@Test
	void testGetSchedulesByFlightId_Success() {
		MockitoAnnotations.openMocks(this);

		Long flightId = 1L;
		List<ScheduleFlight> mockSchedules = new ArrayList<>();
		// Add mock ScheduleFlight objects to the list

		when(scheduleFlightRepository.findByFlightId(flightId)).thenReturn(mockSchedules);

		List<ScheduleFlight> result = scheduleFlightService.getSchedulesByFlightId(flightId);

		assertEquals(mockSchedules.size(), result.size());
		assertEquals(mockSchedules, result);
		verify(scheduleFlightRepository, times(1)).findByFlightId(flightId);
	}

	@Order(16)
	@Test
	void testGetSchedulesByFlightId_Exception() {
		MockitoAnnotations.openMocks(this);

		Long flightId = 123L;
		when(scheduleFlightRepository.findByFlightId(flightId))
				.thenThrow(new RuntimeException("Database connection error"));

		assertThrows(RuntimeException.class, () -> scheduleFlightService.getSchedulesByFlightId(flightId));
		verify(scheduleFlightRepository, times(1)).findByFlightId(flightId);
	}

	@Order(17)
	@Test
	void testGetSchedulesByFlightId_NullFlightId() {
		MockitoAnnotations.openMocks(this);

		List<ScheduleFlight> mockSchedules = new ArrayList<>();
		when(scheduleFlightRepository.findByFlightId(null)).thenReturn(mockSchedules);

		List<ScheduleFlight> result = scheduleFlightService.getSchedulesByFlightId(null);

		assertTrue(result.isEmpty());
		verify(scheduleFlightRepository, times(1)).findByFlightId(null);
	}

	
}