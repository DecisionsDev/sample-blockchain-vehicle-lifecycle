# Introduction

This sample illustrates how we can use IBM Operational Decision Manager (ODM) to implement rule-based Smart Contracts in the context of an HyperLedger Fabric Blockchain application. 

This sample is derived from the Vehicle Lifecycle demo of HyperLedger Composer and enhance it with rule-based decisions taken by the Smart Contracts. More information about this demo can be found here: https://www.youtube.com/watch?v=IgNfoQQ5Reg and source can be found here: https://github.com/hyperledger/composer-sample-networks/tree/master/packages/vehicle-lifecycle-network

# Pre-requisites

You need to have IBM ODM 8.9.0 installed on your machine. This sample can only work on Linux and MacOS. 

Refer to the "Before you begin" section in https://hyperledger.github.io/composer/installing/development-tools.html to make sure you have all the pre-requisites to run HyperLedger Fabric and Composer on your machine. 

Use the following commands to make sure you have the proper environment:
docker -v
docker-compose -v
node -v 
npm -v

# Content

The sample is made of several projects. Each project contains its own README with more detail information about what it contains. 

odm-runtime
-----------

is a project that allows to build and run an ODM docker image that contains the ODM Rule Execution Server (RES) and a Database. The RES hosts the ODM rule engine and provide the interface to execute Decision Services. 

odm-deployer
------------

is a NodeJS service that acts as a facade to the RES to receive Ruleapp deployment commands from the Blockchain application.

odm-deployer-webapp
-------------------

is a Java WebApp that can be packaged in the same Liberty Server as a the RES to provide a facade to the RES to receive Ruleapp deployment commands from the Blockchain application.
 
You will be using either odm-deployer or odm-deployer-webapp to provide this facade, not both. 

vehicle-lifecycle
-----------------

is a HyperLedger Composer sample, derived from the one developed by the Composer Team. 
The JavaScript transaction processors in this sample are invoking decisions that are executed
as Decision Services in ODM. 

vehicle-lifecycle-cli
---------------------

is a command line application invoking Composer CLI to perform various operations like deploying RuleApps and submitting transactions. 

vehicle-lifecycle-xom
----------------------

is a Java implementation of the model used in the ODM Decision Service. Its implementation is derived from the model used in the vehicle-lifecycle Composer Sample. 

vehicle-lifecycle-decision-service
-----------------------------------

is the ODM Decision Service that implements the decision logic invoked from the Smart Contracts of the vehicle-lifecycle Blockchain application.

# Setting up HyperLedger Fabric V1.0 & Composer 0.10.1

Please refer to https://hyperledger.github.io/composer/installing/development-tools.html to install and run HyperLedger Fabric and Composer. 

Following the steps described in this page should allow you to install Composer (0.10.1 or later) on your machine, install Fabric images and run Fabric on your machine as Docker containers.

Use 'composer -v' to check the version of Composer that has been installed.

Note that 'downloadFabric.sh' and 'createComposerProfile.sh' should be done only once, the first time. 
'startFabric.sh' and 'stopFabric.sh' can be used to start and stop Fabric on your machine. 

At this point you should have HyperLedger Fabric 1.0 running and Composer ready to deploy Composer applications. 

Next step is to augment this installation with ODM capabilities.

# Setting up IBM ODM with HyperLedger Fabric and Composer

- Launch a terminal window and go the 'odm-runtime' project directory
- <FIRST TIME ONLY>
  You need to copy RES binary files from your ODM installation:
  - open the 'init.sh' script file and set the ODM_HOME variable to point to your actual ODM installation
  - save the file and run the 'init.sh' script
  </FIRST TIME ONLY>  
- enter: 'docker-compose up -d' to build the RES Docker image and start it as a Docker Container.

Refer to the README in odm-runtime project for more information

If odm-deployer-webapp has not been deployed with the RES, you need to run the deployment facade 
coming from the odm-deployer project. 

- go to 'odm-deployer' 
- enter: 'docker-compose up -d' to build the image and start the deployment service.

Refer to the README in the odm-deployer project for more information.

At this point, the whole Blockchain infrastructure is running and ready to receive Blockchain applications. 
The next step will be to deploy the vehicle lifecycle application.

The RES deployed in the Blockchain network is still empty at this point. It must but populated with the XOM of the vehicle lifecycle application and the Decision Service used by the Smart Contracts of this application.

# Deploying the vehicle-lifecycle Composer application

- go to 'vehicle-lifecycle' directory
- enter: 'npm run deploy' to build and deploy the application. 

Refer to the README in 'vehicle-lifecycle' directory for more information about this Composer application.

# Deploying the XOM

A deployment feature has been integrated in the vehicle lifecycle demo to deploy the XOM and the Decision Services through the Blockchain. 

To deploy the XOM throught the Blockchain, you should perform the following actions:
- in Rule Designer, you need to generate the XOM (and the Ruleapp):
    - Launch ODM 8.9.0 Rule Designer 
    - the first time you need to create a new workspace and import vehicle-lifecycle-xom and vehicle-lifecycle-decision-service projects into it
    - right click on the 'deployment/deployer' file in the vehicle-lifecycle-decision-service project explorer and select 'Rule Execution Server / Deploy...'
        - this operation generate a 'vehicle_lifecycle_ds.jar' in the 'output' directory
        - this operation generate a 'vehicle-lifecycle-xom.zip' in the 'output' directory
- go to 'vehicle-lifecyle-cli' directory 
- (the first time you need to perform: 'npm install')
- enter: 'npm run deployXom ../vehicle-lifecycle-decision-service/output/vehicle-lifecycle-xom.zip 1.0'

This operation needs to be done each time you modify the XOM of the decision service. 

Refer to the README in 'vehicle-lifecycle-cli' directory for more information about this command.

# Deploying the Decision Service

The Decision Service is the packaging of the vehicle lifecyle business rules exposed as a REST service invoked from the Smart Contracts. It must be deployed in the RES associated to all nodes of the Blockchain network. 

Like the XOM, the Decision Service is deployed through the Blockchain, leveraging a deployment feature integrated in the demo. 

In the previous step, you should have generated the ruleapp supporting the vehicle lifecycle decision service using Rule Designer. The ruleapp is the 'vehicle_lifecycle_ds.jar' generated in the 'output' directory. 

- go to 'vehicle-lifecyle-cli' directory 
- enter: 'npm run deployRuleapp ../vehicle-lifecycle-decision-service/output/vehicle_lifecycle_ds.jar 1.0 1.0'
    - note that 'npm run deployRuleapp' without arguments deploy this ruleapp with these versions

This operation needs to be done each time you modify the business rules of the application. 

When you change the rules, you need to increment the version number of the ruleset. You can do it in Rule Designer:
- Open the 'deployer' file in 'deployment'
- make sure the Ruleset base version (in Decision Operation tab) is set to the right version (1.0 to start, to be incremented when you want to deploy newer version)

Refer to the README in 'vehicle-lifecycle-cli' directory for more information about this command.

# Running the Vehicle Lifecycle demo

Refer to the README in 'vehicle-lifecycle-cli' directory to initialize the application and run a demo scenario that illustrates the application of the business rules when suspicious transactions are generated. 

# Demo Scenario summary

Here is a summary of all steps to run the demo. 

1/ Run Fabric 1.0
    - go to the fabric-tools directory (created from instructions in https://hyperledger.github.io/composer/installing/development-tools.html)
    - ./downloadFabric.sh (1st time only)
    - ./startFabric.sh    
    - ./createComposerProfile.sh (1st time only)
2/ Run ODM RES
    - go to 'odm-runtime'
    - 'docker-compose up -d'
3/ Run ODM Deployer
    - go to 'odm-deployer'
    - 'docker-compose up -d'
4/ Deploy vehicle-lifecycle Business Network
    - go to 'vehicle-lifecycle'
    - 'npm run deploy'
5/ Deploy the XOM via Blockchain (provided the XOM has been generated by Rule Designer before)
    - go to 'vehicle-lifecycle-cli'
    - the first time you need to run 'npm install' to set-up the client application
    - 'npm run deployXom'
6/ Deploy the Ruleapp via Blockchain (provided the Ruleapp has been generated by Rule Designer before)
    - 'npm run deployRuleapp'
7/ Setup the data of the Business Network
    - 'npm run setup ; npm run listVehicles'
8/ Run the suspicious transactions
    - 'npm run transfers'

Assuming the XOM and the Ruleapp have been deployed, you can run the full demo from a fresh model
using the following command from the 'vehicle-lifecycle-cli' directory: 'npm run demo'

# License
[Apache 2.0](LICENSE)

# Notice
© Copyright IBM Corporation 2017.
