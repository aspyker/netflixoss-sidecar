package com.ibm.ibmcsf.simplesidecar.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ibmcsf.simplesidecar.domain.Check;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.karyon.spi.Component;

@Component
@Singleton
public class CheckManager {
	public static final String PROP_ROOT = "com.ibm.ibmcsf.sidecar.externalhealthcheck.";
	
	private static final Logger logger = LoggerFactory.getLogger(CheckManager.class);
	private ArrayList<Check> checks;
	private ArrayList<ScheduledExecutorService> checkers;
	
	public boolean allHealthChecksPassing() {
		for (Check check : checks) {
			if (!check.getCurrentStatus().equals(Check.StatusType.HEALTHY)) {
				if (check.getLastStatusRepeated() > 3) {
					logger.debug("returning false");
					return false;
				}
			}
		}
		logger.debug("returning true");
		return true;
	}

	@PostConstruct
	public void init() {
		checkers = new ArrayList<>();
		logger.debug("starting the CheckManager");
		loadCheckDefinitions();
		if (DynamicPropertyFactory.getInstance().getBooleanProperty(PROP_ROOT + "enabled", true).get()) {
			for (Check check: checks) {
				CheckRunner runner = new CheckRunner(check);
				ScheduledExecutorService service = Executors.newScheduledThreadPool(2, new DaemonNamedThreadFactory("checker-thread-"+check.getId()));
				checkers.add(service);
				service.scheduleAtFixedRate(runner, 0, check.getInterval(), TimeUnit.MILLISECONDS);
			}
		}
	}
	
	@PreDestroy
	public void shutDown() {
		logger.debug("shuttind down the CheckManager");
		for (int ii = 0; ii < checkers.size(); ii++) {
			ScheduledExecutorService service = checkers.get(ii);
			// should I be a bit more nice about asking to shutdown and then do shutdownNow?
			List<Runnable> running = service.shutdownNow();
		}
	}

	public void loadCheckDefinitions() {
		checks = new ArrayList<Check>();
		int numChecks = DynamicPropertyFactory.getInstance().getIntProperty(PROP_ROOT + "numchecks", 0).get();
		for (int ii = 0; ii <= numChecks; ii++) {
			String id = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + ii + ".id", "checkid" + ii).get();
			String desc = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + ii + ".description", "unspecified description").get();
			int interval = DynamicPropertyFactory.getInstance().getIntProperty(PROP_ROOT + ii + ".interval", 30*1000).get();
			String className = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + ii + ".localclass", null).get();
			String script = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + ii + ".script", null).get();
			String workingDir = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + ii + ".workingdir", null).get();
			
			if (className == null && script == null) {
				logger.warn("check did not specify either localclass or script, will ignore");
				continue;
			}
			
			if (script != null && workingDir == null) {
				workingDir = "/tmp";
			}
			
			Check check = new Check(id, desc, interval, script, workingDir, className);
			checks.add(check);
		}
	}	
}
