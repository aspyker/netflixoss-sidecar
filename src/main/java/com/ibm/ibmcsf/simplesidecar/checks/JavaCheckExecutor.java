package com.ibm.ibmcsf.simplesidecar.checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ibmcsf.simplesidecar.domain.Check;

public class JavaCheckExecutor implements CheckExecutor {
	private static final Logger logger = LoggerFactory.getLogger(JavaCheckExecutor.class);
	
	@Override
	public int runCheck(Check check) {
		logger.debug("should run java check = " + check);
		return 0;
	}

}
