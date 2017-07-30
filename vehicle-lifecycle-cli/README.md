# vehicle-lifecycle-cli
This project contains a NodeJS application that implements several commands to manipulate the vehicle lifecycle demo. These commands are implemented using the HyperLedger Composer client API.

# initializing the client application

Before running any command provided by this application the first time, you need to invoke: 'npm run install' to download the NodeJS dependencies. 

# initializing the Vehicle Lifecycle application

To populate the vehicle lifecycle business network deployed in the Blockchain with initial data, you need to submit a 'setup' transaction. The initial data are created in the transaction processor of this transaction.

You can submit such transaction using:

'composer transaction submit -p hlfv1 -n vehicle-lifecycle -i admin -s adminpw -d '{"$class": "org.acme.vehicle.lifecycle.SetupDemo"}'
or 
'npm run setup'

You can list the vehicles managed in this application with:
'npm run listVehicles'

You can clean-up the data model of this application using:
'npm run clean'

# deploying XOM

Provided the XOM has been generated in ../vehicle-lifecycle-decision-service/output, the following command deploy it to the RES:
'npm run deployXom'

WARNING: the deployment feature added to this demo to deploy the XOM to the RES via a Blockchain transaction is not completely safe. If the deployXom transaction is rolled-back, the side effect on the RES has still been made and further executions will use the deployed XOM. The mechanism needs to be enhanced to handle the case where the deployXom transaction is rolled-back. 

# deploying a Decision Service

The rules of a Decision Service needs to be deployed to the Rule Execution Server running in each peer node of the Blockchain. 

Provided the Decision Service (Ruleapp) has been generated in ../vehicle-lifecycle-decision-service/output, the following command deploy it to the RES:
'npm run deployRuleapp'

This demo features a capability to deploy the XOM and the Decision Services through the Blockchain itself. 
Here is what is implemented: 
    - a client send the new version of a RuleApp as a com.ibm.rules.RuleAppUpdated transaction, passing:
      o String resDeployerURL <== URL of the Ruleapp Deplyer facade: 'http://odm-deployer:1880/deploy'
      o String ruleAppName <== ruleapp name should be of the form <ruleapp>/<ruleset>
      o String ruleapp_version
      o String ruleset_version
      o String archive <== the archive binary encoded in base64
      o String managedXomURI <== the URI of the XOM library used by the decision service (typically 'reslib://vehicle_lifecycle_ds_1.0/1.0')
    - a dedicated Smart Contract receives this transaction and:
        - stores the current version information in an asset com.ibm.rules.RuleAppCurrentVersion
        - stores the RuleApp itself in an asset com.ibm.rules.RuleApp
        - sends the archive to the Ruleapp Deployer service that will deploy it to the RES
    - The Smart Contracts that need to invoke a decision service need to read the current version information from the RuleAppCurrentVersion asset
    - If the deployment transaction is rolled-back, the RuleAppCurrentVersion information is rolled-back too and the Smart Contract
      still uses the previous version. The new version has been pushed to the RES but will not been used. 

# demo scenario: submitting suspicious transactions

Three suspicious transactions have been implemented in this client application. Refer to the 'makeSuspiciousTransfer' functions in 'vehicle-lifecycle.js'.

You submit a suspicious transaction with:
'npm run makeSuspiciousTransfer1'

You can run 'npm run listVehicles' to see that the suspicious message of vehicle 156478954 is now "Cross Border Suspicious Transfer: please double check buyer regulation"

├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤
│ 156478954  │ anthony   │ Arium  │ Nova    │ Cross Border Suspicious Transfer: please double check buyer regulation │
├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤

'npm run makeSuspiciousTransfer2'

├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤
│ 123456789  │ anthony   │ Porsche │ Cayenne │ Suspicious sale: Expensive car identified as special                   │
├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤

'npm run makeSuspiciousTransfer3'

├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤
│ 234567890  │ anthony   │ BMW    │ X5      │ REJECTED: Can't sell Emergency Vehicle in California                   │
├────────────┼───────────┼────────┼─────────┼────────────────────────────────────────────────────────────────────────┤

You can sequentially submit these 3 suspicious transactions by executing: 'npm run transfers'

# Extension of the scenario: show rules life-cycle

- In Rule Designer, change the Rules
    - for instance, change the message in 'suspicious rules/Cross Border Transfer'
    - in the deployer file, change the base version of the Ruleset to 1.1
    - From Rule Designer, generate the new Ruleapp Archive in the 'output' directory
- deploy the new version through the Blockhchain:
    - 'npm run deployRuleapp ../vehicle-lifecycle-decision-service/output/vehicle_lifecycle_ds.jar 1.0 1.1'
- re-run the first suspicious transaction: 
    - 'npm run makeSuspiciousTransfer1 ; npm run listVehicles'  
    - ==> The message for vehicle 156478954 should have changed

# Summary

Assuming that you have deployed the Composer 'vehicle-lifecycle' application and the XOM and the Ruleapp to the RES, the following command run the full demo:
    - 'npm run demo'

It cleans up the data, initializes the data and submits the suspicious transactions. The results are displayed in the standard output.  

Assumming that you have deployed the Composer 'vehicle-lifecycle' application and you have generated the XOM and the Ruleapp in
'../vehicle-lifecycle-decision-service/output' from Rule Designer, the following command run the full demo:
    - 'npm run deployAndRunDemo'

It cleans up the data, deploys the XOM and the Ruleapp, initializes the data and submits the suspicious transactions. The results are displayed in the standard output.  
    

