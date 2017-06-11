package org.vda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public enum VehicleStatus 
{
	ACTIVE,
	OFF_THE_ROAD,
	SCRAPPED
}
