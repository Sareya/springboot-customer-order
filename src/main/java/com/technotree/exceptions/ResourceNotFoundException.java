package com.technotree.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceNotFoundException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger( RuntimeException.class );

	public <T> ResourceNotFoundException(String message)
	{
		super(message);
	}
	public <T> ResourceNotFoundException(String resource, T id) 
	{
	    super("Could not find " + resource + " with ID: " + id);
	    LOG.error("Could not find {} with ID: {}", resource, id);
	}
}
