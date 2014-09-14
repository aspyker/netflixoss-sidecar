package com.ibm.ibmcsf.simplesidecar.checks;

import com.ibm.ibmcsf.simplesidecar.domain.Check;

public interface CheckExecutor {
	public int runCheck(Check check);
}
