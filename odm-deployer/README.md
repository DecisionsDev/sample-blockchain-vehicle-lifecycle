# odm-deployer
This REST service implements a facade to the management API of the RES. It allows to deploy 
XOM and Ruleapps to the RES from dedicated Smart Contracts involved on specific deployment transactions. 

# setting up the deployer 

- Launch a terminal window and go the `odm-deployer` project directory
- enter: `docker-compose up -d` to build the image and start the service

# stopping the deployer

You need to stop the deployer using `docker-compose down` from the `odm-deployer` project directory. 

If you change the code of the service, you need to remove the existing images so that a new one can be created with the new code. 
You can remove the image by invoking the following Docker command:
`docker rmi smartcontract/odm-deployer:1.0.0`

To create a Docker image from the dockerfile in this directory use the following docker command:
`docker-compose build`

Note that `docker-compose up` will build the images if it does not exist yet.





