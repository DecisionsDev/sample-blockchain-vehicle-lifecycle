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


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ibm.rules.blockchain.model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.DatatypeConverter;

@Path("/api")
public final class DeployRESTEndPoint {

	private static String H_AUTHORIZATION = "Basic " + getBase64Credentials();
	// odmruntime_odm-runtime_1:9060
	private static String RES_BASE_URL = "http://localhost:9060/res/api/v1";

	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();


	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnmanagedInstances() throws Exception {
		return Response.ok("{ \"status\": \"OK\" }").build();
	}


	@POST
	@Path("/deploy")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postDeploy(@Context UriInfo uriInfo, DeployRequest data) throws Exception {
		String[] ruleAppAndSet = data.ruleAppName.split("/");
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(RES_BASE_URL + "/ruleapps?merging=REPLACE_MERGING_POLICY");

		httpPost.addHeader(HttpHeaders.AUTHORIZATION, H_AUTHORIZATION);
		httpPost.addHeader(HttpHeaders.ACCEPT, "application/json");
		httpPost.addHeader("X-File-Name", "archive.jar");

		HttpEntity entity = MultipartEntityBuilder.create().addPart("body", new ByteArrayBody(data.archive, "archive.jar") ).build();
		httpPost.setEntity(entity);

		CloseableHttpResponse response = null;
		CloseableHttpResponse responseUri = null;
		String responseStr = "";
		try {
			response = client.execute(httpPost);
			PostRuleAppResponse res;
			String str = EntityUtils.toString(response.getEntity());
			try {
				res = OBJECT_MAPPER.readValue(str, PostRuleAppResponse.class);
			} catch(UnrecognizedPropertyException e) {
				PropertiesResponse res2 = OBJECT_MAPPER.readValue(str, PropertiesResponse.class);
				return Response.ok("{" +
						"\"response\": \""+res2.message+"\", " +
						"\"ruleapp\": \""+ruleAppAndSet[0]+"\", " +
						"\"ruleapp_version\": \""+data.ruleapp_version+"\", " +
						"\"ruleset_version\": \""+data.ruleset_version+"\"  " +
						"}")
						.header("Content-Type", "application/json")
						.build();
			}
			if(res.succeeded)
				System.out.println("The ruleapp " + ruleAppAndSet[0] + " has been correctly sent to the res.");

			String urlResponse = RES_BASE_URL + "/ruleapps" +
					"/" + ruleAppAndSet[0] + "/" + data.ruleapp_version +
					"/" + ruleAppAndSet[1] + "/" + data.ruleset_version + "/properties/ruleset.managedxom.uris";

			HttpPost httpPostUri = new HttpPost(urlResponse);
			httpPostUri.addHeader(HttpHeaders.AUTHORIZATION,H_AUTHORIZATION);
			httpPostUri.addHeader(HttpHeaders.ACCEPT,"application/json");
			httpPostUri.addHeader(HttpHeaders.CONTENT_TYPE,"text/plain");
			httpPostUri.setEntity(new StringEntity(data.managedXomURI));
			responseUri = client.execute(httpPostUri);
			PropertiesResponse resProp = OBJECT_MAPPER.readValue(EntityUtils.toString(responseUri.getEntity()), PropertiesResponse.class);

			responseStr = "Deployment to RES OK";
			if (!res.succeeded)
				responseStr = "deployment to RES failed.";
			if (null != resProp && !resProp.succeeded)
				responseStr += " - XOM URI update failed: " + resProp.message;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
			if (response != null)
				response.close();
			if (responseUri != null)
				responseUri.close();
		}

		return Response.ok("{" +
				"\"response\": \""+responseStr+"\", " +
				"\"ruleapp\": \""+ruleAppAndSet[0]+"\", " +
				"\"ruleapp_version\": \""+data.ruleapp_version+"\", " +
				"\"ruleset_version\": \""+data.ruleset_version+"\"  " +
			"}")
			.header("Content-Type", "application/json")
			.build();
	}

	@POST
	@Path("/deployXom")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postDeployXom(@Context UriInfo uriInfo, DeployXomRequest data) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(RES_BASE_URL + "/xoms/" + data.xomName);
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
		httpPost.addHeader(HttpHeaders.ACCEPT, "application/json");
		httpPost.addHeader(HttpHeaders.AUTHORIZATION, H_AUTHORIZATION);

		HttpEntity entity = MultipartEntityBuilder.create().addPart("body", new ByteArrayBody(data.xom, "archive-xom.jar") ).build();
		httpPost.setEntity(entity);

		CloseableHttpResponse response;
		String str;
		try {
			response = client.execute(httpPost);
			str = EntityUtils.toString(response.getEntity());
			PostXomResponse res;
			try {
				res = OBJECT_MAPPER.readValue(str, PostXomResponse.class);
			} catch (JsonMappingException e) {
				PropertiesResponse res2 = OBJECT_MAPPER.readValue(str, PropertiesResponse.class);
				return Response.ok("{" +
						"\"response\": \""+res2.message+"\", " +
						"\"xomname\": \""+data.xomName+"\", " +
					"}")
					.header("Content-Type", "application/json")
					.build();
			}

			HttpPut httpPut = new HttpPut(RES_BASE_URL + "/libraries/" + data.libraryName + "/" + data.library_version);
			httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			httpPut.addHeader(HttpHeaders.ACCEPT, "application/json");
			httpPut.addHeader(HttpHeaders.AUTHORIZATION, H_AUTHORIZATION);
			httpPut.setEntity(new StringEntity(res.resource.uri));

			String msg = "{" +
					"\"response\": \"";
			if (!res.succeeded)
				msg += "XOM deployment to RES failed.";
			else {
				response = client.execute(httpPut);
				String responsePut = EntityUtils.toString(response.getEntity());
				PostXomLibraryResponse res2 = OBJECT_MAPPER.readValue(responsePut, PostXomLibraryResponse.class);

				msg += "XOM deployment to RES OK.";
				if (res2.succeeded)
					msg += " - Library update OK";
				else {
					msg += " - Library update failed";
					System.err.println(res2.message);
				}
				msg += "\", " +
					"\"xom_version\": \""+1+"\", "; // res2.xom_version
			}
			msg +=  "\"xomName\": \""+data.xomName+"\", " +
					"\"libraryName\": \""+data.libraryName+"\", " +
					"\"library_version\": \""+data.library_version+"\" " +
				"}";

			return Response.ok(msg)
					.header("Content-Type", "application/json")
					.build();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getBase64Credentials() {
		String authstring = "resAdmin" + ":" + "resAdmin";
		try {
			return DatatypeConverter.printBase64Binary(authstring.getBytes("ISO8859_1"));
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return DatatypeConverter.printBase64Binary(authstring.getBytes());
	}
}
