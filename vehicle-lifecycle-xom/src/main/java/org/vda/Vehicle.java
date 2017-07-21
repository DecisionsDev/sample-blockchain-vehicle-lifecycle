
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

//import composer.base.MyIdGenerator;
import composer.base.Person;

// we should be able to use the following annotation to manage the graph
// fields representing a reference probably need to be annotated too
// @JsonIdentityInfo(generator=MyIdGenerator.class, property="vin")
public class Vehicle 
{
	public static Map<Long,ThreadLocal<Map<String, Vehicle>>> VEHICLE_THREADMAP;
	
	static {
		VEHICLE_THREADMAP = new HashMap<Long,ThreadLocal<Map<String, Vehicle>>>();
	}
	
	public static Map<String, Vehicle> getVehicleMap() 
	{
		long id = Thread.currentThread().getId();
		Map<String, Vehicle> result = null;
		ThreadLocal<Map<String, Vehicle>> tl = VEHICLE_THREADMAP.get(id);
		if (tl != null) {
			result = tl.get();
		}		
		if (result == null) {
			result = new HashMap<String, Vehicle>();			
			VEHICLE_THREADMAP.put(id, new ThreadLocal<Map<String, Vehicle>>());
			VEHICLE_THREADMAP.get(id).set(result);
		}
		return result;
	}
	
	public static Vehicle getVehicle(String vin) 
	{
		System.out.println("--------> get vehicle (thread id:" + Thread.currentThread().getId() + "): " + vin);
		return getVehicleMap().get(vin);
	}
	
	public static void clearVehicles() 
	{
		getVehicleMap().clear();
		VEHICLE_THREADMAP.remove(Thread.currentThread().getId());
		System.out.println("clear vehicles (thread id:" + Thread.currentThread().getId() + ")");
	}
	
	public Vehicle() {
		// TODO Auto-generated constructor stub
	}
	
	public String $class;
	public String vin;
	
	@JsonProperty("vin")
	public void setVin(String v) {
		vin = v;
		System.out.println("--------> creating a new vehicle: (thread id:" + Thread.currentThread().getId() + "): " + v);
		getVehicleMap().put(v, this);
	}
	
	public VehicleDetails vehicleDetails;
	public VehicleStatus vehicleStatus;
	public Person owner ;
	public String numberPlate ;
	public String suspiciousMessage ;
	public ArrayList<VehicleTransferLogEntry> logEntries ;
}
