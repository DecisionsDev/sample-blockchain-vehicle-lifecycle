
package org.vda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import composer.base.Person;

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
