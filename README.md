# SmartContractODM

This repository contains some example and some code showing and enabling the combination of Blockchain and ODM
to externalize business logic from Smart Contract and implement it in ODM as business rules. 

vehicle-lifecycle
-----------------
is a HyperLedger Composer sample, derived from the one developed by the Composer Team. 
The JavaScript transaction processors in this sample are invoking decisions that are executed
as Decision Services in a RES service. 

vehicle-lifecycle-xom
----------------------
is a Java implementation of the model used in the vehicle-lifecycle Composer Sample and used in Decision Service. 

vehicle-lifecycle-decision-service
-----------------------------------
is the ODM Decision Service that implement the decision logique invoked from the Smart Contract

odm-runtime
-------------------
An odm docker image that contain RES / HTDS and a DB in a docker image.
TODO : 
	- Add a bna to call this res.
	- Deploy the vehicule xom and ruleset by default in this image

sample-rest-service
-------------------
a very basic implementation of a REST service in NodeJS called by the http-post-network@0.0.1.bna Composer basic sample.
The README there explain how to deploy a Fabric and Composer topology in Docker and instruction to test the REST
service call from the JS smart contract

