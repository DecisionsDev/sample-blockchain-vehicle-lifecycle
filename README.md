# SmartContractODM [![Build Status](https://travis.ibm.com/Bolero/SmartContractODM.svg?token=ZxM16hrzpkaVzCiqK6S2&branch=master)](https://travis.ibm.com/Bolero/SmartContractODM)
------------------


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

sample-rest-service
-------------------
a very basic implementation of a REST service in NodeJS called by the http-post-network@0.0.1.bna Composer basic sample.
The README there explain how to deploy a Fabric and Composer topology in Docker and instruction to test the REST
service call from the JS smart contract

# TODO
- XOM
    - add Jackson properties in XOM to avoid using $class fields  
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = "$class", include = JsonTypeInfo.As.PROPERTY)
    - remove $class fields
    - support custom deserialization to support objects graph (see comment in vda.js) 
        - partially done, but hacked

- Decision Service
    - automate build of the XOM and the Ruleapp in scripts
    - script to deploy Ruleapp and XOM to the RES running in Docker
    - code a cto -> XOM/BOM/Voc generator
    - set-up an instance of RES per instance of peers
        - support HA: if a peer/RES goes down, the second one take over
        - need to map ports so that Smart Contract uses only one URL
- odm-runtime
    - remove the ruleset.jar and the xom from the image
- deployment of new rules to Fabric
    [
        We played with the idea of deploying new rules using a Blockchain transaction that can be processed by a dedicated Composer Transaction Processor to deploy the attached ruleset to the corresponding RES. 

        It might not work well as a transaction in Blockchain can be rolled-back and the side effect we would have done on the rules won't be rolled-backed. 

        Another idea is to deploy the ruleset in a transaction and having a dedicated Transaction Processor that writes down this ruleset in the World State, in a given asset.
        At the next transaction, before executing the rules, inside the RES, we would check this asset (timestamp or ruleset version stored there), would detect that our ruleset is obsolete and reload it from the asset. 

        Is there a way to do that with the existing RES?  

        Can we use an interceptor or a dedicated DAO implementation to do that? 
    ]
    [
        Not a good idea to embed the RES in the peer docker container: each container should be one process
    ]

RuleAppUpdated data: 

{
  "$class": "com.ibm.rules.RuleAppUpdated",
  "resDeployerURL": "http://odm-deployer:1880/deploy",
  "ruleAppName": "vehicle/1.0/isSuspiciousEntryPoint",
  "major_version": "1",
  "minor_version": "0",
  "qualifier_version": "0",
  "ruleApp": "BIN"
}