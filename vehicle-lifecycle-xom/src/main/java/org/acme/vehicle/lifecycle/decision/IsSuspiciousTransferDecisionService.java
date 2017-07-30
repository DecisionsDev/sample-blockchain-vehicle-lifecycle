package org.acme.vehicle.lifecycle;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.vda.PrivateVehicleTransfer;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "wrapperId")
public class TransactionWrapper {
    public String wrapperId;
    public PrivateVehicleTransfer transaction;

}
