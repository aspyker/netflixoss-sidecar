package com.ibm.ibmcsf.simplesidecar.checks;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ibmcsf.simplesidecar.domain.Check;
import com.ibm.ibmcsf.simplesidecar.domain.Check.StatusType;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;

public class ScriptCheckExecutor implements CheckExecutor {
	private static final Logger logger = LoggerFactory.getLogger(ScriptCheckExecutor.class);
	private static DynamicIntProperty timeout = DynamicPropertyFactory.getInstance().getIntProperty(CheckManager.PROP_ROOT + ".defaultscripttimeout", 1000);

	@Override
	public int runCheck(Check check) {
		logger.debug("should run script check = " + check);
		
		try {
			String[] command = check.getScriptToRun().split(" ");
			List<String> commandStrings = Arrays.asList(command);
			ProcessBuilder pb = new ProcessBuilder(commandStrings);
			Map<String, String> env = pb.environment();
			env.put("archaius.deployment.environment", ConfigurationManager.getDeploymentContext().getDeploymentEnvironment());
			env.put("archaius.deployment.applicationId", ConfigurationManager.getDeploymentContext().getApplicationId());
			pb.directory(new File(check.getWorkingDir()));
			// TODO: do better with putting this into a log file (could pipe back to sidecar?)
			File log = new File("/tmp/" + check.getId() + ".log");
			pb.redirectErrorStream(true);
			pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
			Process p = pb.start();
			
			Thread.sleep(timeout.get());
			
			int exitValue = p.exitValue();
			switch (exitValue) {
				case 0:
					check.addStatus(StatusType.HEALTHY);
					break;
				case 1:
					check.addStatus(StatusType.WARN);
					break;
				default:
					check.addStatus(StatusType.UNHEALTHY);
					break;
			}
		}
		catch (IOException ioe) {
			logger.error(ioe.toString());
		}
		catch (InterruptedException ie) {
			logger.error(ie.toString());
		}
		return 0;
	}

}
