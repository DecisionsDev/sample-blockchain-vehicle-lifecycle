# Introduction

This sample illustrates how we can use IBM Operational Decision Manager (ODM) to implement rule-based Smart Contracts in the context of an HyperLedger Fabric Blockchain application. 

This sample is derived from the Vehicle Lifecycle demo of HyperLedger Composer and enhance it with rule-based decisions taken by the Smart Contracts. More information about this demo can be found here: https://www.youtube.com/watch?v=IgNfoQQ5Reg and source can be found here: https://github.com/hyperledger/composer-sample-networks/tree/master/packages/vehicle-lifecycle-network

This readme contains the instructions to go from the installation of the software to the execution of the demo. Please start with '1/ Pre-requisites' section and go down to '7/ Running the Vehicle Lifecycle demo'. 

# Content

The sample is made of several projects. Each project contains its own README with more detail information about what it contains. 

#### odm-runtime

Is a project that allows to build and run an ODM docker image that contains the ODM Rule Execution Server (RES) and a Database. The RES hosts the ODM rule engine and provide the interface to execute Decision Services. 

#### odm-deployer

Is a NodeJS service that acts as a facade to the RES to receive Ruleapp deployment commands from the Blockchain application.

#### odm-deployer-webapp

Is a Java WebApp that can be packaged in the same Liberty Server as a the RES to provide a facade to the RES to receive Ruleapp deployment commands from the Blockchain application.
 
You will be using either odm-deployer or odm-deployer-webapp to provide this facade, not both. 

#### vehicle-lifecycle

Is a HyperLedger Composer sample, derived from the one developed by the Composer Team. 
The JavaScript transaction processors in this sample are invoking decisions that are executed
as Decision Services in ODM. 

#### vehicle-lifecycle-cli

Is a command line application invoking Composer CLI to perform various operations like deploying RuleApps and submitting transactions. 

#### vehicle-lifecycle-xom

Is a Java implementation of the model used in the ODM Decision Service. Its implementation is derived from the model used in the vehicle-lifecycle Composer Sample. 

#### vehicle-lifecycle-decision-service

Is the ODM Decision Service that implements the decision logic invoked from the Smart Contracts of the vehicle-lifecycle Blockchain application.

# Installation

### 1/ Pre-requisites

This sample can only work on Linux and MacOS. If you are using a Windows box, you need to install a Ubuntu Virtual Machine. 

You need to have IBM ODM 8.9.0 installed on your machine. Please refer to your company-specific instructions to have access to this product and install it on your computer. Note that it is not necessary to fully configure ODM on your machine as the only components that will be used are Rule Designer and the RES jar files used to build the RES docker image.

The following prerequisites are required:

- Operating Systems: Ubuntu Linux 14.04 / 16.04 LTS (both 64-bit), or Mac OS 10.12
  - `sw_vers`
- Docker Engine: Version 17.03 or higher
  - `docker -v`
- Docker-Compose: Version 1.8 or higher
  - `docker-compose -v`
- Node: 6.x (note version 7 is not supported)
  - `node -v`
- npm: v3.x or v5.v
  - `npm -v`
- git: 2.9.x
  - `git --version`
- A code editor of your choice, we recommend VSCode (https://code.visualstudio.com).


Refer to the "Before you begin" section in https://hyperledger.github.io/composer/installing/development-tools.html to make sure you have all the pre-requisites to run HyperLedger Fabric and Composer on your machine. 

### 2/ Setting up HyperLedger Fabric V1.0 & Composer 0.11.0

Please refer to https://hyperledger.github.io/composer/installing/development-tools.html to install and run HyperLedger Fabric and Composer. 

Following the steps described in this page should allow you to install Composer (0.11.0 or later) on your machine, install Fabric images and run Fabric on your machine as Docker containers.

Use `composer -v` to check the version of Composer that has been installed.

Note that `downloadFabric.sh` and `createComposerProfile.sh` should be done only once, the first time. 

Use `startFabric.sh` to start HyperLedger Fabric processes. (You can use `stopFabric.sh` to stop Fabric on your machine). 

At this point you should have HyperLedger Fabric 1.0 running and Composer ready to deploy Composer applications. 

Use `docker ps -a` to check that you have the proper containers up and running:

```
$ docker ps -a
CONTAINER ID        IMAGE                                     COMMAND                  CREATED              STATUS              PORTS                                            NAMES
1a4b387f872c        hyperledger/fabric-peer:x86_64-1.0.0      "peer node start -..."   About a minute ago   Up About a minute   0.0.0.0:7051->7051/tcp, 0.0.0.0:7053->7053/tcp   peer0.org1.example.com

fa5689d9bbaa        hyperledger/fabric-orderer:x86_64-1.0.0   "orderer"                About a minute ago   Up About a minute   0.0.0.0:7050->7050/tcp                           orderer.example.com

5d6a927ab0dd        hyperledger/fabric-couchdb:x86_64-1.0.0   "tini -- /docker-e..."   About a minute ago   Up About a minute   4369/tcp, 9100/tcp, 0.0.0.0:5984->5984/tcp       couchdb

90a45895fcb8        hyperledger/fabric-ca:x86_64-1.0.0        "sh -c 'fabric-ca-..."   About a minute ago   Up About a minute   0.0.0.0:7054->7054/tcp                           ca.org1.example.com
```

Next step is to augment this installation with ODM capabilities.

### 3/ Setting-up IBM ODM with HyperLedger Fabric and Composer

- Launch a terminal window and go the `odm-runtime` project directory

**&lt;FIRST TIME ONLY&gt;**

- You need to copy RES binary files from your ODM installation:

  - Open the `init.sh` script file and set the `ODM_HOME` variable to point to your actual ODM installation
  - **Save** the file and **Run** the `init.sh` script
  
**&lt;/FIRST TIME ONLY&gt;**

- Enter: `docker-compose up -d` to build the RES Docker image and start it as a Docker Container.

Refer to the README in `odm-runtime` project for more information

The next step is to a run a deployment facade as a companion process to the RES. Called ODM Deployer, 
this application comes from the 'odm-deployer' project. 

- Go to `odm-deployer` 
- Enter: `docker-compose up -d` to build the image and start the deployment service.

Refer to the README in the `odm-deployer` project for more information.

At this point, the whole Blockchain infrastructure is up-and-running and ready to receive Blockchain applications. 

The RES deployed in the Blockchain network is still empty at this point. It must but populated with the XOM of the vehicle lifecycle application and the Decision Service used by the Smart Contracts of this application.

The next step will be to deploy the vehicle lifecycle Hyperledger Composer application.

### 4/ Deploying the vehicle-lifecycle Composer application

- Go to `vehicle-lifecycle` directory
- Enter: `npm run deploy` to build and deploy the application. 

Refer to the README in `vehicle-lifecycle` directory for more information about this Composer application.

### 5/ Setting-up an Eclipse environment for ODM projects

The ODM application is packaged as 2 eclipse projects that can be edited with ODM Rule Designer: 
`vehicle-lifecycle-xom` and `vehicle-lifecycle-decision-service`

You need to import them in an Eclipse workspace. You need to perform these instructions only once.

- Launch ODM 8.9.0 Rule Designer 
- Create a fresh workspace in whatever location on your disk
- Import `vehicle-lifecycle-xom` and `vehicle-lifecycle-decision-service` projects using "Import > General > Existing Projects Into The Workspace". You don't need to copy them, just import them. 

The 2 projects should build without errors. Warnings can be ignored.

  Note: if you are copying these project in a different location, the 'npm run deployXom' and 'npm run deployRuleapp' can't be use. Please refer to the 'package.json' file in 'vehicle-lifecycle-cli' directory
  to see how to point these commands to a different location. 

### 6/ Deploying the XOM

A deployment feature has been integrated in the vehicle lifecycle demo to deploy the XOM and the Decision Services through the Blockchain. 

To deploy the XOM throught the Blockchain, you should perform the following actions:
- In Rule Designer, you need to generate the XOM (and the Ruleapp):
  - Right click on the `deployment/deployer` file in the `vehicle-lifecycle-decision-service` project explorer and select "Rule Execution Server / Deploy ...". This operation generate a `vehicle_lifecycle_ds.jar` in the `output` directory.
  - Right click on the `deployment/deployer` file in the `vehicle-lifecycle-decision-service` project explorer and select "Rule Execution Server / Deploy XOM ...". This operation generate a `vehicle-lifecycle-xom.zip` in the `output` directory
- Go to `vehicle-lifecyle-cli` directory 

**&lt;FIRST TIME ONLY&gt;**

- The first time you need to perform: `npm install`
  
**&lt;FIRST TIME ONLY&gt;**

- Enter: `npm run deployXom`

This operation needs to be done each time you modify the XOM of the decision service. 

Refer to the README in `vehicle-lifecycle-cli` directory for more information about this command.

### 7/ Deploying the Decision Service

The Decision Service is the packaging of the vehicle lifecyle business rules exposed as a REST service invoked from the Smart Contracts. It must be deployed in the RES associated to all nodes of the Blockchain network. 

Like the XOM, the Decision Service is deployed through the Blockchain, leveraging a deployment feature integrated in the demo. 

In the previous step, you should have generated the ruleapp supporting the vehicle lifecycle decision service using Rule Designer. The ruleapp is the `vehicle_lifecycle_ds.jar` generated in the `output` directory of the `vehicle-lifecycle-decision-service` project. 

- Go to `vehicle-lifecyle-cli` directory 
- Enter: `npm run deployRuleapp`

This operation needs to be done each time you modify the business rules of the application. 

When you change the rules, you need to increment the version number of the ruleset. You can do it in Rule Designer:
- Open the `deployer` file in `deployment`
- Make sure the Ruleset base version (in Decision Operation tab) is set to the right version (1.0 to start, to be incremented when you want to deploy newer version)

Refer to the README in `vehicle-lifecycle-cli` directory for more information about this command.

### 8/ Running the Vehicle Lifecycle demo

Refer to the README in `vehicle-lifecycle-cli` directory to initialize the application and run a demo scenario that illustrates the application of the business rules when suspicious transactions are generated. 

Assuming the XOM and the Ruleapp have been deployed, you can run the full demo from a fresh model
using the following command from the `vehicle-lifecycle-cli` directory: 
- `npm run demo`

The message

    │ Cross Border Suspicious Transfer: please double check buyer regulation │ 

generated in your display is generated from the business rules of the decision service invoked from a Smart Contract.

### 9/ End

You're all set. Refer to the README in `vehicle-lifecycle-cli` for more information about the demo scenario. 

# All Steps and Demo Scenario summary

Here is a summary of all steps to run the demo. 

1/ Run Fabric 1.0
- go to the `fabric-tools` directory (created from instructions in https://hyperledger.github.io/composer/installing/development-tools.html)
- `./downloadFabric.sh` (1st time only)
- `./startFabric.sh`   
- `./createComposerProfile.sh` (1st time only)

2/ Run ODM RES
- go to `odm-runtime`
- `docker-compose up -d`

3/ Run ODM Deployer
- go to `odm-deployer`
- `docker-compose up -d`

4/ Deploy vehicle-lifecycle Business Network
- go to `vehicle-lifecycle`
- `npm run deploy`

5/ Deploy the XOM via Blockchain (provided the XOM has been generated by Rule Designer before)
- go to `vehicle-lifecycle-cli`
- the first time you need to run `npm install` to set-up the client application
- `npm run deployXom`

6/ Deploy the Ruleapp via Blockchain (provided the Ruleapp has been generated by Rule Designer before)
- `npm run deployRuleapp`

7/ Setup the data of the Business Network
- `npm run setup ; npm run listVehicles`

8/ Run the suspicious transactions
- `npm run transfers`

Assuming the XOM and the Ruleapp have been deployed, you can run the full demo from a fresh model
using the following command from the `vehicle-lifecycle-cli` directory: `npm run demo`

# License
[Apache 2.0](LICENSE)

# Notice
© Copyright IBM Corporation 2017.
