package com.fms.scheduleFlight.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
public class ScheduleFlight {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long scheduleId;
	
	private Long flightId;
	
	@NotBlank
	@Pattern(regexp="^[a-zA-Z]*$", message="Enter Valid Name")
	private String sourceAirport;
	
	@NotBlank
	@Pattern(regexp="^[a-zA-Z]*$", message="Enter Valid Name")
	private String destinationAirport;

	@JsonFormat(pattern = ("yyyy-MM-dd'T'HH:mm:ss.SSS"), shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@NotNull(message = "Please enter a time it cannot be null")
	private LocalDateTime arrivalTime;
	
	@JsonFormat(pattern = ("yyyy-MM-dd'T'HH:mm:ss.SSS"), shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@NotNull(message = "Please enter a time it cannot be null")
	private LocalDateTime departureTime;
	
	@NotNull
	private Double price;


	public ScheduleFlight() {
		super();
	}


	public ScheduleFlight(Long flightId, String sourceAirport, String destinationAirport,
			LocalDateTime arrivalTime, LocalDateTime departureTime, Double price) {
		super();
		this.flightId = flightId;
		this.sourceAirport = sourceAirport;
		this.destinationAirport = destinationAirport;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.price = price;
	}


	public ScheduleFlight(Long scheduleId, Long flightId, String sourceAirport, String destinationAirport, 
				LocalDateTime arrivalTime, LocalDateTime departureTime, Double price) {
		super();
		this.scheduleId = scheduleId;
		this.flightId = flightId;
		this.sourceAirport = sourceAirport;
		this.destinationAirport = destinationAirport;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.price = price;
	}


	public Long getScheduleId() {
		return scheduleId;
	}


	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}


	public Long getFlightId() {
		return flightId;
	}


	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}


	public String getSourceAirport() {
		return sourceAirport;
	}


	public void setSourceAirport(String sourceAirport) {
		this.sourceAirport = sourceAirport;
	}


	public String getDestinationAirport() {
		return destinationAirport;
	}


	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public LocalDateTime getDepartureTime() {
		return departureTime;
	}


	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}

	
	
}
