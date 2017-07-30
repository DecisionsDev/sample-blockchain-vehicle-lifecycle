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
// this code is generated and should not be modified
package org.vda;

import org.hyperledger.composer.system.CustomTypeIdResolver;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "$class")
@JsonTypeIdResolver(CustomTypeIdResolver.class)
public class VehicleDetails {
   public String make;
   public String modelType;
   public String colour;
   public String vin;
   public String modelVariant;
   public String modelVersion;
   public String bodyType;
   public TaxClass taxationClass;
   public int revenueWeight;
   public int cylinderCapacity;
   public double co2;
   public String typeOfFuel;
   public int numberOfSeats;
   public int numberOfStandingPlaces;
   public String wheelPlan;
   public String vehicleCategory;
   public String typeApprovalNumber;
   public double maxNetPower;
   public String engineNumber;
   public double maxPermissibleMass;
   public double massInService;
   public double powerWeightRatio;
   public TrailerDetails trailerDetails;
   public SoundDetails soundDetails;
   public ExhaustEmissions exhaustEmissions;
}
