In a fresh location on your disk, Install Composer Samples from GIT
- git clone https://github.com/hyperledger/composer-sample-applications.git
- git clone https://github.com/hyperledger/composer-sample-applications-hlfv1.git

To clean up your Docker environment
docker ps -aq | xargs docker rm -f
docker images -aq | xargs docker rmi -f

To create a Docker image from the dockerfile in this directory:
docker build -t stephanemery/sample-rest-service .

To deploy a Fabric 0.6 + 0.7.3 Composer:
- cd composer-fabric-1.0
- see instructions in https://hyperledger.github.io/composer/tutorials/getting-started-playground.html#installdocker
    - launch Fabric v0.6 + sample-rest-service
    docker pull hyperledger/fabric-baseimage:x86_64-0.1.0
    docker tag hyperledger/fabric-baseimage:x86_64-0.1.0 hyperledger/fabric-baseimage:latest
    docker-compose up -d
    - launch Composer 0.7.3
    docker run -d -p 8080:8080 hyperledger/composer-playground
    http://localhost:8080
    - import and deploy http-post-network@0.0.1.bna from Composer UI
    - play with it
        in 'Test' section of Composer Playground, create an asset with assetID: "a1" and value: "10"
        Submit a 'PostTransaction' with assetId: "a1", a: "3", b: "4"
        Check the value in the Asset, it should now be "Count is 7" => 7 has been calculated by the sample-rest-service


To deploy Fabric 1.0 alpha + 0.7.3 Composer:
- launch Fabric 1.0
    - cd ~/composer-sample-applications-hlfv1/packages/getting-started/scripts
        start-hyperledger.sh
- launch Composer 0.7.3 + sample-rest-service
- cd ~/sample-rest-service/composer-fabric-1.0
        docker-compose up -d
<<DOES NOT WORK AT THIS POINT: Composer cannot connect to Fabric>>

Alternative way to deploy Fabric 1.0 alpha + 0.7.3 Composer: 
- run the following script to start Fabric & Composer: 
  curl -sSL http://hyperledger.github.io/composer/install-hlfv1.sh | bash
- run the following command to add the sample-rest-service: 
  docker run --network composer_default --name sample-rest-service --publish 1880:1880 --detach stephanemery/sample-rest-service
- import and deploy http-post-network@0.0.1.bna from Composer UI
- play with it
        in 'Test' section of Composer Playground, create an asset with assetID: "a1" and value: "10"
        Submit a 'PostTransaction' with assetId: "a1", a: "3", b: "4"
        Check the value in the Asset, it should now be "Count is 7" => 7 has been calculated by the sample-rest-service

Code snippet to call a REST service: 
/**
 * Handle a POST transaction, calling OpenWhisk
 * @param {org.acme.sample.PostTransaction} postTransaction - the transaction to be processed
 * @transaction
 */
function handlePost(postTransaction) {

  var url = 'http://sample-rest-service:1880/compute';

  return post( url, postTransaction)
      .then(function (result) {
        // alert(JSON.stringify(result));
        postTransaction.asset.value = 'Count is ' + result.body.sum;
        return getAssetRegistry('org.acme.sample.SampleAsset')
          .then(function (assetRegistry) {
            return assetRegistry.update(postTransaction.asset);
          })
          .then(function (assetRegistry) {
            return "Asset updated with value " + result.body.sum;
          });
      });
}
