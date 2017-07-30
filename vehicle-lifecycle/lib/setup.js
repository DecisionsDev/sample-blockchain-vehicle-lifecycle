/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Setup the demo
 * @param {org.acme.vehicle.lifecycle.SetupDemo} setupDemo - the SetupDemo transaction
 * @transaction
 */
function setupDemo(setupDemo) {
    console.log('setupDemo');

    var factory = getFactory();
    var NS_M = 'org.acme.vehicle.lifecycle.manufacturer';
    var NS = 'org.acme.vehicle.lifecycle';
    var NS_D = 'org.vda';
    var NS_B = 'composer.base';

    var names = ['dan', 'simon', 'jake', 'anastasia', 'matthew', 'mark', 'fenglian', 'sam', 'james', 'nick', 'caroline', 'rachel', 'john', 'rob', 'tom', 'paul', 'ed', 'dave', 'anthony', 'toby', 'ant', 'matt', 'anna'];
    var vehicles = {
        'Arium': {
            'Nova': [
                {
                    'vin': '156478954',
                    'colour': 'white',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                }
            ],
            'Nebula': [
                {
                    'vin': '652345894',
                    'colour': 'blue',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                }
            ]
        }, 
        'Morde': {
            'Putt': [
                {
                    'vin': '6437956437', 
                    'colour': 'black',
                    'vehicleStatus': 'ACTIVE', 
                    'suspiciousMessage': 'Mileage anomaly',
                    'taxationClass': 'PETROL_CAR'
                },
                {
                    'vin': '857642213', 
                    'colour': 'red',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                },
                {
                    'vin': '542376495', 
                    'colour': 'silver',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                }
            ],
            'Pluto': [
                {
                    'vin': '976431649', 
                    'colour': 'white',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'ELECTRIC_MOTOCYCLE'
                },
                {
                    'vin': '564215468', 
                    'colour': 'green',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR', 
                    'suspiciousMessage': 'Insurance write-off but still active'
                },
                {
                    'vin': '784512464', 
                    'colour': 'grey',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                }
            ]
        },
        'Ridge': {
            'Cannon': [
                {
                    'vin': '457645764',
                    'colour': 'red',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                },
                {
                    'vin': '312457645',
                    'colour': 'white',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR', 
                    'suspiciousMessage': 'Suspicious ownership sequence'
                },
                {
                    'vin': '65235647',
                    'colour': 'silver',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR', 
                    'suspiciousMessage': 'Untaxed vehicle'
                }
            ], 
            'Rancher': [
                {
                    'vin': '85654575',
                    'colour': 'blue',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                }, 
                {
                    'vin': '326548754',
                    'colour': 'white',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR', 
                    'suspiciousMessage': 'Uninsured vehicle'
                }
            ]
        },
        'Porsche': {
            'Cayenne': [
                {
                    'vin': '123456789',
                    'colour': 'black',
                    'vehicleStatus': 'ACTIVE', 
                    'taxationClass': 'SPECIAL_VEHICLES'
                }
            ], 
            '911': [
                {
                    'vin': '012345678',
                    'colour': 'blue',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'PETROL_CAR'
                }                 
            ]
        },
        'BMW': {
            'X5': [
                {
                    'vin': '234567890',
                    'colour': 'grey',
                    'vehicleStatus': 'ACTIVE', 
                    'taxationClass': 'EMERGENCY_VEHICLE'
                }
            ], 
            'Z1': [
                {
                    'vin': '345678901',
                    'colour': 'blue',
                    'vehicleStatus': 'ACTIVE',
                    'taxationClass': 'EXCEMPT_VEHICLE'
                }                 
            ]
        }        
    };

    var persons = {
    'dan' : {
        'firstName' : 'Dan',
        'lastName' : 'Selman',
        'gender' : 'MALE',
        'nationalities' : ['French' , 'UK'],
        'contactDetails' : {
            'email' : 'dan@acme.org',
            'address' : {
                'city' :  'Winchester',                
                'region' :  'England',                
                'country' : 'UK'
            }
        },
        'birthDetails' : {
            'dateOfBirth' : '1972-06-01T20:41:46.568Z',
            'placeOfBirth' : 'London'
        }
    }, 
    'simon' : {}, 
    'jake' : {}, 
    'anastasia' : {}, 
    'matthew' : {}, 
    'mark' : {}, 
    'fenglian' : {}, 
    'sam' : {}, 
    'james' : {}, 
    'nick' : {}, 
    'caroline' : {}, 
    'rachel' : {}, 
    'john' : {}, 
    'rob' : {}, 
    'tom' : {}, 
    'paul' : {}, 
    'ed' : {}, 
    'dave' : {}, 
    'anthony' : {
        'firstName' : 'Anthony',
        'lastName' : 'Smith',
        'gender' : 'MALE',
        'nationalities' : ['French'],
        'contactDetails' : {
            'email' : 'anthony@acme.org',
            'address' : {
                'city' :  'Paris',                
                'region' :  'IDF',                
                'country' : 'France'
            }
        },
        'birthDetails' : {
            'dateOfBirth' : '1975-07-01T20:41:46.568Z',
            'placeOfBirth' : 'Calais'
        }
    }, 
    'toby' : {}, 
    'ant' : {}, 
    'matt' : {}, 
    'anna' : {}
    };
    
    var manufacturers = [];
    var privateOwners = [];

    for (var name in vehicles) {
        var manufacturer = factory.newResource(NS_M, 'Manufacturer', name);
        manufacturers.push(manufacturer);
    }

   for(var i=0; i<names.length; i++) {
       var name = names[i];
       var privateOwner = factory.newResource(NS_B, 'Person', name);
       var personData = persons[name];
       privateOwner.firstName = personData.firstName;
       privateOwner.lastName = personData.lastName;
       privateOwner.gender = personData.gender;
       privateOwner.nationalities = personData.nationalities;
       if (personData.contactDetails) {
        privateOwner.contactDetails = factory.newConcept(NS_B, 'ContactDetails');
        privateOwner.contactDetails.email = personData.contactDetails.email;
        privateOwner.contactDetails.address = factory.newConcept(NS_B, 'Address');
        privateOwner.contactDetails.address.city = personData.contactDetails.address.city;
        privateOwner.contactDetails.address.region = personData.contactDetails.address.region;
        privateOwner.contactDetails.address.country = personData.contactDetails.address.country;           
       }
       privateOwners.push(privateOwner);
   }

    var regulator = factory.newResource(NS, 'Regulator', 'regulator');


    var privateOwnerRegistry;
    var vehicleRegistry;

    return getParticipantRegistry(NS + '.Regulator')
        .then(function(regulatorRegistry) {
            return regulatorRegistry.add(regulator);
        })
        .then(function() {
            return getParticipantRegistry(NS_M + '.Manufacturer');
        })
        .then(function(manufacturerRegistry) {
            return manufacturerRegistry.addAll(manufacturers);
        })
        .then(function() {
            return getParticipantRegistry(NS_B + '.Person');
        })
        .then(function(privateOwnerRegistry) {
            return privateOwnerRegistry.addAll(privateOwners);
        })
        .then(function() {
            return getAssetRegistry(NS_D + '.Vehicle');
        })
        .then(function(vehicleRegistry) {
            var vs = [];
            var carCount = 0;
            for (var mName in vehicles) {
                var manufacturer = vehicles[mName];
                for (var mModel in manufacturer) {
                    var model = manufacturer[mModel];
                    for(var i=0; i<model.length; i++) {
                        var vehicleTemplate = model[i];
                        var vehicle = factory.newResource(NS_D, 'Vehicle', vehicleTemplate.vin);
                        vehicle.owner = factory.newRelationship(NS_B, 'Person', names[carCount]);
                        vehicle.vehicleStatus = vehicleTemplate.vehicleStatus;
                        vehicle.vehicleDetails = factory.newConcept(NS_D, 'VehicleDetails');
                        vehicle.vehicleDetails.make = mName; 
                        vehicle.vehicleDetails.modelType = mModel; 
                        vehicle.vehicleDetails.colour = vehicleTemplate.colour; 
                        vehicle.vehicleDetails.vin = vehicleTemplate.vin;
                        vehicle.vehicleDetails.taxationClass = vehicleTemplate.taxationClass;

                        if (vehicleTemplate.suspiciousMessage) {
                            vehicle.suspiciousMessage = vehicleTemplate.suspiciousMessage;
                        }

                        if (!vehicle.logEntries) {
                            vehicle.logEntries = [];
                        }

                        var logEntry = factory.newConcept(NS_D, 'VehicleTransferLogEntry');
                        logEntry.vehicle = factory.newRelationship(NS_D, 'Vehicle', vehicleTemplate.vin);
                        logEntry.buyer = factory.newRelationship(NS_B, 'Person', names[carCount]);
                        logEntry.timestamp = setupDemo.timestamp;
                        vehicle.logEntries.push(logEntry);

                        vs.push(vehicle);
                        carCount++;
                    }
                }
            }
            return vehicleRegistry.addAll(vs);
        });
}