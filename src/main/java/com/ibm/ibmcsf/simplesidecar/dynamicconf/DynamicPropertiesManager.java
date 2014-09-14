package com.ibm.ibmcsf.simplesidecar.dynamicconf;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ibmcsf.simplesidecar.checks.DaemonNamedThreadFactory;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.karyon.spi.Component;

@Component
@Singleton
public class DynamicPropertiesManager {
	public static final String PROP_ROOT = "com.ibm.ibmcsf.sidecar.dynamicpropertywriter.";
	private static final Logger logger = LoggerFactory.getLogger(DynamicPropertiesManager.class);

	
	@PostConstruct
	public void init() {
		if (DynamicPropertyFactory.getInstance().getBooleanProperty(PROP_ROOT + "enabled", true).get()) {
			String propTemplate = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + "file.template", null).get();
			String propOut = DynamicPropertyFactory.getInstance().getStringProperty(PROP_ROOT + "file", null).get();
			if (propTemplate == null) {
				logger.warn("failed to provide template filename, no updates will occur");
			}
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new DaemonNamedThreadFactory("dynprops-thread"));
			PropertyUpdater pu = new PropertyUpdater(propTemplate, propOut);
			service.scheduleAtFixedRate(pu,
				DynamicPropertyFactory.getInstance().getLongProperty("archaius.fixedDelayPollingScheduler.initialDelayMills", 30000).get(),
				DynamicPropertyFactory.getInstance().getLongProperty("archaius.fixedDelayPollingScheduler.delayMills", 60000).get(),
				TimeUnit.MILLISECONDS);
		}
	}
	
	@PreDestroy
	public void shutDown() {
	}
}
