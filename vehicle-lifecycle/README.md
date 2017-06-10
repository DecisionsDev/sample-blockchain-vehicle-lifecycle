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

Check if you have a connection profile for the the Fabric you just install. You should have a file ~/.composer-connection-profildes/hlfv1/connection.json

If not, copy the one located in the install directory to this location

- mkdir ~/.composer-connection-profiles/hlfv1
- cp install/connection.json ~/.composer-connection-profiles/hlfv1

Note, if you need to see logs of the Smart Contract (chaincode) you need to modify the docker-compose
file generated in tmp/composer-data and add: 
      - CORE_LOGGING_LEVEL=DEBUG
for all peers

You can run playground: http://localhost:8080. You might have to do it from a private browser window or call localstorage.clear() in the web browser console to start with a clean cache

# setting up the ODM decision service

Refer to the README.md in odm-runtime project

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
The full demo is played by running the following command: 'npm run demo'


