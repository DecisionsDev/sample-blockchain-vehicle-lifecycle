package org.acme.vehicle.lifecycle.manufacturer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public enum OrderStatus 
{
	PLACED,
	SCHEDULED_FOR_MANUFACTURE,
	VIN_ASSIGNED,
	OWNER_ASSIGNED,
	DELIVERED
}
