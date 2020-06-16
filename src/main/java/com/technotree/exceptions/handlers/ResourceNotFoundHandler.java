package com.technotree.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.technotree.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceNotFoundHandler
{
	@ResponseBody
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String customerNotFoundHandler(ResourceNotFoundException ex) 
	{
	    return ex.getMessage();
	}
}
