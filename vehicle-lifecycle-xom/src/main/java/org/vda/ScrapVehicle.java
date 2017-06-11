package org.vda;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public class ScrapVehicle extends VehicleTransaction 
{
	public ArrayList<VehicleTransaction> logEntries ;
}
