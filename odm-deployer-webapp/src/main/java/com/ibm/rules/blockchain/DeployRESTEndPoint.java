/*
 *
 *   Copyright IBM Corp. 2017
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ibm.rules.blockchain;


import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/api")
public final class DeployRESTEndPoint {

	@Context
	ServletContext context;
	private static final Logger LOGGER = Logger
			.getLogger(DeployRESTEndPoint.class.getName());


	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnmanagedInstances() throws Exception {
		return Response.ok("OK").build();
	}
	

}
