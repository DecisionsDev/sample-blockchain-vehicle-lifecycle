
# First time

To clean up your Docker environment
docker ps -aq | xargs docker rm -f
docker images -aq | xargs docker rmi -f

First download res artifact:
mvn clean install

To create a Docker image from the dockerfile in this directory:
docker-compose build

# all time

To run the docker image:
docker-compose run odm-runtime
or docker-compose up (which is better as 'docker-compose run' generates connection errors )


To Generate the sample payload : 
http://localhost:9060/DecisionService/rest/vehicule/1.0/vehicule/1.0/json?trace=false

To invoke the a sample payload with curl : 
curl -H "Content-Type: application/json" -X POST -d @sample_suspicious_transaction.json http://localhost:9060/DecisionService/rest/vehicle/1.0/isSuspiciousEntryPoint/1.0


To launch the RES Console: http://localhost:9060/res
user and passwd are resAdmin

Connecting inside the container: docker exec -it <containerIdOrName> bash
to see the logs: docker-compose logs
