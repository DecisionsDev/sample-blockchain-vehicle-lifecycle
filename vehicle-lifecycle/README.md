# Sample Hyperledger Composer Business Network Definition
# Vehicle Lifecycle

This sample is derived from the similar HyperLedger Sample and shows how
the Business Logic in the transaction processing callbacks can be 
externalized in an ODM Decision Service to be implemented by a set of
Business Rules

# prerequisite

Please refer to https://hyperledger.github.io/composer/getting-started/getting-started.html
to understand the pre-requisite to install and use HyperLedger Composer. 

For the rest of this readme, we assume that you download the verhicle-lifecycle demo and you 
are executing commands from this directory.

# Composer installation and initial configuration


You need to install composer command line:
(the following command does not work with Node 7.x, you need to use node 6.10.3. 
 if you have nvm installed, you can swith using: nvm use 6.10.3)
npm install
npm install -g composer-client
npm install -g generator-hyperledger-composer
npm install -g composer-rest-server
npm install -g yo


# setting up HyperLedger Fabric V1.0 & Composer 0.7.3

This script download and start HyperLedger Fabric in Docker. You need to have docker installed on your machine. 

mkdir tmp
cd tmp
curl -sSL http://hyperledger.github.io/composer/install-hlfv1.sh | bash
cd ..

Once you have run this command, the installation scripts are staying in the tpm directory. 
If you need to modify Composer docker-compose file, you can copy composer.sh, remove everything
that recreate the installation files, and run the install from this script, reusing the existing
compose files that you can update. 

Check if you have a connection profile for the the Fabric you just install. You should have a file ~/.composer-connection-profiles/hlfv1/connection.json

If not, copy the one located in the install directory to this location

- mkdir ~/.composer-connection-profiles/hlfv1
- cp install/connection.json ~/.composer-connection-profiles/hlfv1
If you have to do this step, you should edit the copied connection.json and change the location of the keystore


Note, if you need to see logs of the Smart Contract (chaincode) you need to modify the docker-compose
file generated in tmp/composer-data and add: 
      - CORE_LOGGING_LEVEL=DEBUG
for all peers

You can run playground: http://localhost:8080. You might have to do it from a private browser window or call localstorage.clear() in the web browser console to start with a clean cache

# setting up the ODM Rule Execution Server

Refer to the README.md in odm-runtime project

- Launch a terminal window and go the odm-runtime project directory
- enter: 'docker-compose up' to build the image and start the service

# setting up the Ruleapp Deployer

Refer to the README.md in the odm-deployer project

- Launch a terminal window and go the odm-deployer project directory
- enter: 'docker-compose up' to build the image and start the service


# creating and deploying the business network archive for vehicle-lifecycle 

Don't forget to use node 6.10.3: nvm use 6.10.3

Please refer to https://hyperledger.github.io/composer/reference/commands.html for more documentation about
the commands to use.

To create the Business Network archive, launch the following command:
composer archive create --sourceType dir --sourceName .

The first time you deploy your business network, you need to do this command:
composer network deploy -a vehicle-lifecycle@1.0.0.bna -p hlfv1 -i admin -s adminpw 

If you want to update the business network with a new version, you need to use:
composer network update -a vehicle-lifecycle@1.0.0.bna -p hlfv1 -i admin -s adminpw 

The following command list a business network and all its data
composer network list -n vehicle-lifecycle -p hlfv1 -i admin -s adminpw

Note that you can use the following commands:
npm run deploy
npm run update
npm run list

# initializing the Business Network

To populate the Business Network with initial data, you need to submit a 'setup' transaction. The initial
data are created in the transaction processor of this transaction

composer transaction submit -p hlfv1 -n vehicle-lifecycle -i admin -s adminpw -d '{"$class": "org.acme.vehicle.lifecycle.SetupDemo"}'
or 
npm run setup

You can list the vehicles managed in this application with:
npm run listVehicles

You can clean-up the Business Network with:
npm run clean

# deploying XOM

At this point, you should have a RES running as a Docker Container. We need to deploy the Decision Service on it. 
A deployment feature has been integrated in the vehicle-lifecycle demo to deploy ruleapps through the Blockchain. 

You can deploy the XOM from Rule Designer using the deployment feature of this product. Otherwise you can deploy the XOM through
the Blockchain. 

To deploy the XOM throught the Blockchain, you should perform the following actions:
- in Rule Designer, you need to generate the XOM (and the Ruleapp)
    - Launch Rule Designer 8.9.0 on a workspace that contains vehicle-lifecycle-xom and vehicle-lifecycle-decision-service
    - right click on the deployer file in the project explorer and select 'Rule Execution Server / Deploy...'
    - in the wizard, UNCHECK THE TARGET SERVER so that the Ruelapp archive is generated in the output directory and not deployed
  directly to the RES
        - this operation generate a 'vehicle.jar' in the 'output' directory
        - this operation generate a 'vehicle-lifecycle-xom.zip' in the 'resources/xom-libraries' directory
- run 'npm run deployXom ../vehicle-lifecycle-decision-service/resources/xom-libraries/vehicle-lifecycle-xom.zip 1.0'

# deploying the Decision Service

- run 'npm run deployRuleapp ../vehicle-lifecycle-decision-service/output/vehicle.jar 1.0 1.0'
    - note that 'npm run deployRuleapp' deploy this ruleapp with these versions

When you change the rules, you need to increment the version number of the ruleset. You can do that in Rule Designer:
- Open the deployer file in 'deployment'
- make sure the Ruleset base version (in Decision Operation tab) is set to the right version (1.0 to start, to be incremented when you want
  to deploye newer version)

# submitting a suspicious transaction

You submit a suspicious transaction with:
npm run makeSuspiciousTransfer1

You can run 'npm run listVehicles' to see that the suspicious message of vehicle 156478954 is now "Cross Border Suspicious Transfer: please double check buyer regulation"

├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤
│ 156478954  │ anthony   │ Arium  │ Nova    │ Cross Border Suspicious Transfer: please double check buyer regulation │
├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤

npm run makeSuspiciousTransfer2

├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤
│ 123456789  │ anthony   │ Porshe │ Cayenne │ Suspicious sale: Expensive car identified as special                   │
├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤

npm run makeSuspiciousTransfer3

├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤
│ 234567890  │ anthony   │ BMW    │ X5      │ REJECTED: Can't sell Emergency Vehicle in California                   │
├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤


# Summary

Assuming that you deployed the XOM to the RES and you generated the Ruleapp archive in the output directory, the following
command run the full demo:
    - 'npm run demo'

# demo scenario summary

- start up HyperLedger Fabric and Composer
- start up ODM RES
- start up ODM Ruleapp Deployer
- deploy and initialize the vehicle-lifecycle demo
    npm run deploy; npm run setup
- deploy the Decision Service to the RES
    - From Rule Designer, deploy the XOM to the RES
    - From Rule Designer, generate the Ruleapp archive in the 'output' directory
    - npm run deployRuleapp ../vehicle-lifecycle-decision-service/output/vehicle.jar 1.0 1.0
- submit suspicious transactions
    - npm run makeSuspiciousTransfer1 ; npm run listVehicles
    - npm run makeSuspiciousTransfer2 ; npm run listVehicles
    - npm run makeSuspiciousTransfer3 ; npm run listVehicles

# Extension of the scenario: show rules life-cycle

- In Rule Designer, change the Rules
    - for instance, change the message in' suspicious rules/Cross Border Transfer'
    - in the deployer file, change the base version of the Ruleset to 1.1
    - From Rule Designer, generate the new Ruleapp Archive in the 'output' directory
- deploy the new version through the Blockhchain:
    - 'npm run deployRuleapp ../vehicle-lifecycle-decision-service/output/vehicle.jar 1.0 1.1'
- re-run the first suspicious transaction: 
    - npm run makeSuspiciousTransfer1 ; npm run listVehicles  
    - ==> The message for vehicle 156478954 should have changed


# Moving to Composer 0.9
- separate the business network from the app
- remove transaction and 'identified by' for all transactions
- update the ACL
        resource: "org.acme.vehicle.lifecycle.*"
- change admin into PeerAdmin in composer deploy command        
    - composer network deploy --archiveFile vehicle-lifecycle*.bna  -p hlfv1 -i PeerAdmin -s adminpw 






