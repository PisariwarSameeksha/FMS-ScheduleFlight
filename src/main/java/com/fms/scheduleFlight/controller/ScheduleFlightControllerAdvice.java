package com.fms.scheduleFlight.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fms.scheduleFlight.exception.FlightNotFoundException;
import com.fms.scheduleFlight.exception.NoScheduleListFoundException;
import com.fms.scheduleFlight.exception.ScheduleAlreadyExistsException;
import com.fms.scheduleFlight.exception.ScheduleNotFoundException;
import com.fms.scheduleFlight.exception.ScheduledFlightNotFoundException;


@RestControllerAdvice
public class ScheduleFlightControllerAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
		Map<String, String> errors=new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	
	@ExceptionHandler(FlightNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String handleCustomerExceptions(FlightNotFoundException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(NoScheduleListFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String handleCustomerExceptions(NoScheduleListFoundException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(ScheduleAlreadyExistsException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String handleCustomerExceptions(ScheduleAlreadyExistsException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(ScheduledFlightNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String handleCustomerExceptions(ScheduledFlightNotFoundException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(ScheduleNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String handleCustomerExceptions(ScheduleNotFoundException e) {
		return e.getMessage();
	}

}
