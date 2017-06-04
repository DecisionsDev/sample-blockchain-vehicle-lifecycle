# SmartContractODM

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
-------------------
An odm docker image that contain RES / HTDS and a DB in a docker image.

sample-rest-service
-------------------
a very basic implementation of a REST service in NodeJS called by the http-post-network@0.0.1.bna Composer basic sample.
The README there explain how to deploy a Fabric and Composer topology in Docker and instruction to test the REST
service call from the JS smart contract

# TODO
- XOM
    - add Jackson properties in XOM to avoid using $class fields  
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = "$class", include = JsonTypeInfo.As.PROPERTY)
    - remove $class fields
    - support custom deserialization to support objects graph (see comment in vda.js)
- Decision Service
    - automate build of the XOM and the Ruleapp in scripts
    - script to deploy Ruleapp and XOM to the RES running in Docker
    - code a cto -> XOM generator
- odm-runtime
    - remove the ruleset.jar and the xom from the image
- deployment to Fabric
    - do we really need to embed RES in Fabric Peer? May be not. Everyone running a peer on its 
    own machine would run one instance of RES on this machine. If 2 peers on the same machine 
    have access to the same RES that does not break the blockchain paradigm. 
    If we implement the deployment through a transaction, all peers on the same machine will 
    deploy the rules. Is it a problem? 

    - package RES in peer images: hyperledger/fabric-peer:x86_64-1.0.0-alpha

      when running the installer curl -sSL http://hyperledger.github.io/composer/install-hlfv1.sh | bash
      from a given directory, the necessary files are kept in this directory. 

      The scripts and docker-compose files reference hyperledger/fabric-peer:x86_64-1.0.0-alpha. We could
      replace references to this image by our own version that contains the RES and relaunch the script. 

      However we can't merge the RES image with the fabric-peer one. As RES is build from websphere-liberty:8.5.5.9-webProfile7
      it would mean that we have to create an image from fabric-peer and install ourself, java8 and liberty

      https://github.com/yeasy/docker-hyperledger-fabric-peer/blob/master/v1.0.0-alpha/Dockerfile
      https://github.com/yeasy/docker-hyperledger-fabric-base/blob/master/Dockerfile
