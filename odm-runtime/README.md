# odm-runtime
This directory contains the necessary files to build a Docker image for the ODM RES and run it. 

# First time

To clean up your Docker environment
docker ps -aq | xargs docker rm -f
docker images -aq | xargs docker rmi -f

You need to copy RES binary files from your ODM installation:
  - open the init.sh script file and set the ODM_HOME variable to point to your actual ODM installation
  - save the file and run the init.sh script

To create a Docker image from the dockerfile in this directory:
docker-compose build

# All time

To run the docker image:
docker-compose up -d

You can see the RES logs using the following command: docker-compose logs

# RES Console

You can run the RES Console at the following URL: http://localhost:9060/res
user and passwd are 'resAdmin'

# Testing 

Once the Ruleapp has been deployed to the RES, you can test the Decision Service using the following
sample payload with curl : 
curl -H "Content-Type: application/json" -X POST -d @sample_suspicious_transaction.json http://localhost:9060/DecisionService/rest/vehicle_lifecycle_ds/1.0/isSuspiciousEntryPoint/1.0




