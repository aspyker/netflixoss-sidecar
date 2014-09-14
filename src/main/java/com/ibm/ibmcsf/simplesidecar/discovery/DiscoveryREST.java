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
package com.ibm.ibmcsf.simplesidecar.discovery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;

@Path("/discovery")
@Singleton
public class DiscoveryREST {
	private static final Logger logger = LoggerFactory.getLogger(DiscoveryREST.class);
	
	@Context 
	private HttpServletRequest request;

	@GET
	@Path("/{vipAddress}/nextServerInsecure")
	@Produces("application/json")
	public /* InstanceInfo */ Response getNextServerFromEureka(
			@PathParam("vipAddress") String vipAddress) {
		DiscoveryClient client = DiscoveryManager.getInstance().getDiscoveryClient();
		
		List<InstanceInfo> infos = client.getInstancesByVipAddress(vipAddress, false);
		if (infos == null || infos.isEmpty()) {
			return Response.noContent().build();
		}
		InstanceInfo info = client.getNextServerFromEureka(vipAddress, false);
		return Response.ok(info).build();
	}
	
	@GET
	@Path("/{vipAddress}/nextServerInsecure/ip")
	@Produces("application/json")
	public /* String */ Response getNextServerFromEurekaOnlyIp(
			@PathParam("vipAddress") String vipAddress) {
		DiscoveryClient client = DiscoveryManager.getInstance().getDiscoveryClient();

		List<InstanceInfo> infos = client.getInstancesByVipAddress(vipAddress, false);
		if (infos == null || infos.isEmpty()) {
			return Response.noContent().build();
		}
		InstanceInfo info = client.getNextServerFromEureka(vipAddress, false);
		return Response.ok(info.getIPAddr()).build();
	}
	
	@GET
	@Path("/{vipAddress}/getInstancesByVipAddressInsecure")
	@Produces("application/json")
	public /* List<InstanceInfo> */ Response getInstancesByVipAddress(
			@PathParam("vipAddress") String vipAddress) {
		DiscoveryClient client = DiscoveryManager.getInstance().getDiscoveryClient();
		List<InstanceInfo> infos = client.getInstancesByVipAddress(vipAddress, false);
		if (infos != null) {
			return Response.ok(infos).build();
		}
		return Response.noContent().build();
	}
	
	@GET
	@Path("/{vipAddress}/getInstancesByVipAddressInsecure/ip")
	@Produces("application/json")
	public /* List<String> */ Response getInstancesByVipAddressOnlyIp(
			@PathParam("vipAddress") String vipAddress) {
		DiscoveryClient client = DiscoveryManager.getInstance().getDiscoveryClient();
		List<InstanceInfo> infos = client.getInstancesByVipAddress(vipAddress, false);
		if (infos != null) {
			ArrayList<String> ips = new ArrayList<>();
			for (InstanceInfo info : infos) {
				ips.add(info.getIPAddr());
			}
			return Response.ok(ips).build();
		}
		return Response.noContent().build();
	}
}
