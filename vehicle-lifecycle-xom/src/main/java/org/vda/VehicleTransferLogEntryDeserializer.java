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

package org.vda;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import composer.base.Person;

public class VehicleTransferLogEntryDeserializer extends JsonDeserializer<VehicleTransferLogEntry> 
{
	
	public static String getId(String fqn) 
	{
		int idx = -1;
		if (fqn != null && (idx = fqn.lastIndexOf("#")) != -1) {
			return fqn.substring(idx + 1);			
		}
		return null;
	}
	
	public static Date getDateFromString(String d) 
	{
		Date date = null;
		if (d != null) {
			try {
				date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(d.replaceAll("Z$", "+0000"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return date;

	}
	
	public static class Entry {
		public String $class;
		public String buyer;
		public String seller;
		public String vehicle;
		public String timestamp;
	}
	
	
	@Override
	public VehicleTransferLogEntry deserialize(
			com.fasterxml.jackson.core.JsonParser parser,
			com.fasterxml.jackson.databind.DeserializationContext ctx)
			throws IOException,
			com.fasterxml.jackson.core.JsonProcessingException 
	{
		System.out.println("Deserializing LogEntry");
		
		parser.setCodec(new ObjectMapper());
		
		Entry entry = parser.readValueAs(Entry.class);
		if (entry == null) {
			System.out.println("Cannot read structure");
			return null;
		}
		VehicleTransferLogEntry result = new VehicleTransferLogEntry();
		
		System.out.println("Search vehicle: " + entry.vehicle);
		
		String vehicleId = null;
		if (entry.vehicle != null) {
			vehicleId = getId(entry.vehicle);
		}
		
		result.vehicle = Vehicle.getVehicle(vehicleId);
		if (result.vehicle != null) {
			System.out.println("found");
		} else {
			System.out.println("NOT found");
		}
		
		
		System.out.println("Search buyer: " + entry.buyer);
		String personId = null;
		if (entry.buyer != null) {
			personId = getId(entry.buyer);
		}
		
		result.buyer = Person.getPerson(personId);
		if (result.buyer != null) {
			System.out.println("found");
		} else {
			System.out.println("NOT found");
		}

		System.out.println("Search seller: " + entry.seller);
		personId = null;
		if (entry.seller != null) {
			personId = getId(entry.seller);
		}
		
		result.seller = Person.getPerson(personId);
		if (result.seller != null) {
			System.out.println("found");
		} else {
			System.out.println("NOT found");
		}
		
		result.timestamp = getDateFromString(entry.timestamp);
		
		
		return result;
		
		/*
		 // following code does not work as the parser has no codec
		ObjectCodec codec = parser.getCodec();
		if (codec == null) {
			System.out.println("Cannot find codec");
			return null;
		}
		
		JsonNode node = codec.readTree(parser);
		VehicleTransferLogEntry result = new VehicleTransferLogEntry();
		
		if (node == null) {
			System.out.println("Error: cannot get the JSON node");
		} else {
			System.out.println("Found JSON node");
			String buyerId = "";
			String sellerId = "";
			String vehicleId = "";
			String timestamp = "";
			
			JsonNode fieldNode = node.get("buyer");
			if (fieldNode != null) {
				buyerId = fieldNode.asText();
			} else {
				System.out.println("can't find buyer");
			}
			
			fieldNode = node.get("seller");
			if (fieldNode != null) {
				sellerId = fieldNode.asText();
			} else {
				System.out.println("can't find seller");
			}
			
			fieldNode = node.get("vehicle");
			if (fieldNode != null) {
				vehicleId = fieldNode.asText();
			} else {
				System.out.println("can't find vehicle");
			}
			fieldNode = node.get("timestamp");
			if (fieldNode != null) {
				timestamp = fieldNode.asText();
			} else {
				System.out.println("can't find timestamp");
			}
			System.out.println("Search vehicle: " + vehicleId);
			result.vehicle = Vehicle.getVehicle(vehicleId);
			if (result.vehicle != null) {
				System.out.println("found");
			} else {
				System.out.println("NOT found");
			}
		}
		return result;*/
	}

}
