package com.technotree.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceExistsException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger( RuntimeException.class );

	public ResourceExistsException(String resource, String unique) 
	{
	    super(resource + " " + unique + " already exists");
	    LOG.error("{} {} already exists", resource, unique);
	}
}
