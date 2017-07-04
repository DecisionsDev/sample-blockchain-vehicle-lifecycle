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
-----------
An odm docker image that contain RES / HTDS and a DB in a docker image.

odm-deployer
------------
A service that act as a facade to the RES to receive Ruleapp deployment from Blockchain Smart Contracts. 

sample-rest-service
-------------------
a very basic implementation of a REST service in NodeJS called by the http-post-network@0.0.1.bna Composer basic sample.
The README there explain how to deploy a Fabric and Composer topology in Docker and instruction to test the REST
service call from the JS smart contract

# Demo Scenario

1/ Run Fabric 1.0
    - go to vehicle-lifecycle/tmp
    - run ./composer_2.sh
2/ Run ODM Deployer
    - go to odm-deployer
    - docker-compose up
3/ Run ODM RES
    - go to odm-runtime
    - docker-compose up
    - use the RES Console to remove the predefined content: http://localhost:9060/res/
        - remove the XOM, Library and Ruleapp
4/ Deploy vehicle-lifecycle Business Network
    - go to vehicle-lifecycle
    - npm run deploy
5/ Deploy the XOM via Blockchain
    - npm run deployXom
6/ Deploy the Ruleapp via Blockchain
    - npm run deployRuleapp
7/ Setup the data of the Business Network
    - npm run setup & npm run listVehicles
8/ Run the suspicious transactions
    - npm run transfers

Assuming the XOM and the Ruleapp have been deployed, you can run the full demo from a fresh model
    - npm run demo


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
        [alternative is to use the deployment through Blockchain]
    - code a cto -> XOM/BOM/Voc generator
- odm-runtime
    - remove the ruleset.jar and the xom from the image
    - set-up an instance of RES per instance of peers
        - support HA: if a peer/RES goes down, the second one take over
        - need to map ports so that Smart Contract uses only one URL
- odm-deployer
    - entry point to deploy XOM libraries
    - integrate the service in odm-runtime image so that we don't have too much things to run
    - code it in Java and integrate it in the App Server? 
- vehicle-lifecycle
    - comment the code, remove print
    - transform TransactionWrapper into a Concept. Try to point to a generic Object or Transaction (see if 0.8.0 implement that, could be)
        -> does not work: Composer does not support serializing Concept as the root of post()
    - review XOM deployment, does not work if the transaction is rolledback as side effect on RES is not rolledback

- deployment of new rules to Rule Execution Server through the Blockchain
Here is what is implemented: 
    - a client send the new version of a RuleApp as a com.ibm.rules.RuleAppUpdated transaction, passing:
      o String resDeployerURL <== URL of the Ruleapp Deplyer facade: 'http://odm-deployer:1880/deploy'
      o String ruleAppName <== ruleapp name should be of the form <ruleapp>/<ruleset>
      o String ruleapp_version
      o String ruleset_version
      o String archive <== the archive binary encoded in base64
      o String managedXomURI <== the URI of the XOM library used by the decision service (typically 'reslib://vehicle_1.0/1.0')
    - Smart Contract receive this transaction and
        - store the current version information in an asset com.ibm.rules.RuleAppCurrentVersion
        - store the RuleApp itsel in an asset com.ibm.rules.RuleApp
        - send the archive to the Ruleapp Deployer service that will deploy it to the RES
    - Smart Contracts that need to invoke a decision service need to read the current version information from the RuleAppCurrentVersion asset
    - If the deployment transaction is rolled back, the RuleAppCurrentVersion information is rolled-back too and the Smart Contract
      still use the previous version. The new version has been pushed to the RES but will not been used. 

RuleAppUpdated data: 

{
  "$class": "com.ibm.rules.RuleAppUpdated",
  "resDeployerURL": "http://odm-deployer:1880/deploy",
  "ruleAppName": "vehicle/isSuspiciousEntryPoint",
  "ruleapp_version": "1.0",
  "ruleset_version": "1.0",
  "managedXomURI": "reslib://vehicle_1.0/1.0",
  "archive": "BASE64 ARCHIVE JAR"
}