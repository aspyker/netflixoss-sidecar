/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.ibm.ibmcsf.simplesidecar;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

@Path("/sidecar")
@Singleton
public class SimpleSidecarREST {
	private static final Logger logger = LoggerFactory.getLogger(SimpleSidecarREST.class);
	
	static DynamicStringProperty dynProp = DynamicPropertyFactory.getInstance().getStringProperty("com.ibm.service.yourservice.dynamicproperty", "notfound");

	@Context 
	private HttpServletRequest request;

	@GET
	@Path("/hello")
	@Produces("application/json")
	public /* HelloMessage */ Response hello() {
		logger.debug("hello called");
		HelloMessage hm = new HelloMessage("nodyanmicproperty", "hello from the sidecar");
		return Response.ok(hm).build();
	}
	
	@GET
	@Path("/dynamicproperty")
	@Produces("application/json")
	public Response dynProperty(@PathParam("tokenid") String tokenid) {
		logger.debug("dynProperty called");
		HelloMessage hm = new HelloMessage(dynProp.get(), "hello from the sidecar");
		return Response.ok(hm).build();
	}
	
	class HelloMessage {
		public String appSpecificDynamicproperty;
		public String helloMessage;
		
		public HelloMessage(String val, String message) {
			this.appSpecificDynamicproperty = val;
			this.helloMessage = message;
		}
	}
}
