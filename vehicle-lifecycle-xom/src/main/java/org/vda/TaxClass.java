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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public enum TaxClass 
{
	  PRIVATE_LIGHT_GOODS
	  , PETROL_CAR
	  , DIESEL_CAR
	  , ALTERNATIVE_FUEL_CAR
	  , LIGHT_GOODS_VEHICLE
	  , EURO4_LIGHT_GOODS_VEHICLE
	  , EURO5_LIGHT_GOODS_VEHICLE
	  , HEAVY_GOODS_VEHICLE
	  , PRIVATE_HEAVY_GOODS_VEHICLE
	  , SPECIAL_TYPES
	  , HAULAGE_VEHICLES
	  , BUS
	  , MOTORCYCLE
	  , ELECTRIC_MOTOCYCLE
	  , SPECIAL_VEHICLES
	  , SMALL_ISLAND_VEHICLES
	  , RECOVERY_VEHICLE
	  , SPECIAL_CONCESSIONARY
	  , EMERGENCY_VEHICLE
	  , EXCEMPT_VEHICLE

}
