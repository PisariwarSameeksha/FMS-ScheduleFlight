package com.fms.scheduleFlight.DTO;

import com.fms.scheduleFlight.entity.FlightStatus;

public class FlightDTO {
		
	private Long flightNumber;
	private String carrierName;
	private String flightModel;
	private Integer seatCapacity;
	private FlightStatus flightStatus;
	
	
	public FlightDTO(String carrierName, String flightModel, Integer seatCapacity, FlightStatus flightStatus) {
		super();
		this.carrierName = carrierName;
		this.flightModel = flightModel;
		this.seatCapacity = seatCapacity;
		this.flightStatus = flightStatus;
	}
	public FlightDTO() {
		super();
	}
	public Long getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(Long flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	public String getFlightModel() {
		return flightModel;
	}
	public void setFlightModel(String flightModel) {
		this.flightModel = flightModel;
	}
	public Integer getSeatCapacity() {
		return seatCapacity;
	}
	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}
	public FlightStatus getFlightStatus() {
		return flightStatus;
	}
	public void setFlightStatus(FlightStatus flightStatus) {
		this.flightStatus = flightStatus;
	}
	

}
