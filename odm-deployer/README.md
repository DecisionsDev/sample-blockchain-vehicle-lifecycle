# ODM Deployer

This REST service allows to deploy a Ruleapp to the RES. It is used as a facade to the RES, called by 
HyperLedger Composer when ruleapps are deployed through specific transactions. 

# setting up the deployer 

- Launch a terminal window and go the odm-deployer project directory
- enter: 'docker-compose up' to build the image and start the service

# stopping the deployer

You need to stop the deployer using 'docker-compose down' from the odm-deployer project directory. 
If you change the code of the service, you need to remove the existing images:
'docker rmi smartcontract/odm-deployer:1.0.0 ; docker rmi smartcontract/odm-deployer:latest'

To create a Docker image from the dockerfile in this directory:
docker build -t smartcontract/odm-deployer .




