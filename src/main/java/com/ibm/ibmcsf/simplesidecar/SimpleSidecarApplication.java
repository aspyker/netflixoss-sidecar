package com.ibm.ibmcsf.simplesidecar;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.karyon.spi.Application;

@Application
public class SimpleSidecarApplication {
	private static final Logger logger = LoggerFactory.getLogger(SimpleSidecarApplication.class);
	
	@PostConstruct
	public void initialize() {
		logger.debug("initializing");
		
		// TODO: you should put any code here that you want to run before the instance is marked as "up" in Eureka
	}
}
