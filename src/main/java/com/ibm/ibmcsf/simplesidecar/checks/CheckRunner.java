package com.ibm.ibmcsf.simplesidecar.checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ibmcsf.simplesidecar.domain.Check;

public class CheckRunner implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(CheckRunner.class);
	
	Check check;
	CheckExecutor executor;
	
	public CheckRunner(Check check) {
		this.check = check;
		if (check.getClassToCall() != null) {
			this.executor = new JavaCheckExecutor();
		}
		else {
			this.executor = new ScriptCheckExecutor();
		}
	}
	
	@Override
	public void run() {
		executor.runCheck(check);
	}
}
