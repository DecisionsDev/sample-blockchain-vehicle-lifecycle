# Sample Hyperledger Composer Business Network Definition
# Vehicle Lifecycle

This sample is derived from the HyperLedger Composer sample (https://github.com/hyperledger/composer-sample-networks/tree/master/packages/vehicle-lifecycle-network) 
and shows how the Business Logic in the transaction processing callbacks can be 
externalized in an ODM Decision Service implemented by a set of Business Rules.

# creating and deploying the business network archive for vehicle-lifecycle 

Please refer to https://hyperledger.github.io/composer/reference/commands.html for more documentation about
the commands to use.

To create the Business Network archive, launch the following command:
composer archive create --sourceType dir --sourceName .

The first time you deploy your business network, you need to do this command:
composer network deploy -a vehicle-lifecycle@1.0.0.bna -p hlfv1 -i PeerAdmin -s adminpw 

If you want to update the business network with a new version, you need to use:
composer network update -a vehicle-lifecycle@1.0.0.bna -p hlfv1 -i admin -s adminpw 

The following command list a business network and all its data
composer network list -n vehicle-lifecycle -p hlfv1 -i admin -s adminpw

Note that you can use the following commands:
npm run deploy
npm run update
npm run list
