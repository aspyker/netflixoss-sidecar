package com.ibm.ibmcsf.simplesidecar;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.karyon.server.ServerBootstrap;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class SimpleSidecarBootstrap extends ServerBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(SimpleSidecarBootstrap.class);

	@Override
	protected void beforeInjectorCreation(LifecycleInjectorBuilder builderToBeUsed) {
		logger.debug("injector configuration");
		builderToBeUsed.withAdditionalModules(
			new JerseyServletModule() {
				@Override
				protected void configureServlets() {
					Map<String, String> params = new HashMap<String, String>();
					// This line tells the sidecar to scan this package for Jersey JAXRS resources
					params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.ibm.ibmcsf.simplesidecar");
					// This line tells Jersey to support POJO to JSON automatic mapping through Jackson
					params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
					// This line tells Jersey to serve JAXRS classes from this URL path
					serve("/rest/api/*").with(GuiceContainer.class, params);
					binder().bind(GuiceContainer.class).asEagerSingleton();
				}
			},
			new SimpleSidecarBGuiceModule()
		);
	}
	
	class SimpleSidecarBGuiceModule implements Module {
		public void configure(Binder binder) {
			// if you want to use Guice DI for your sidecar code, add bind calls here
			//binder.bind(Into.class).to(To.class);
		}
	}
}
