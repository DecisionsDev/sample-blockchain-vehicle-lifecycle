# Sample Hyperledger Composer Business Network Definition
# Vehicle Lifecycle

This sample is derived from the HyperLedger Composer sample (https://github.com/hyperledger/composer-sample-networks/tree/master/packages/vehicle-lifecycle-network) 
and shows how the Business Logic in the transaction processing callbacks can be 
externalized in an ODM Decision Service implemented by a set of Business Rules.

# creating and deploying the business network archive for vehicle-lifecycle 

Please refer to https://hyperledger.github.io/composer/reference/commands.html for more documentation about
the commands to use.

To create the Business Network archive, launch the following command:
`composer archive create --sourceName . --sourceType dir` 


The first time you deploy your business network, you need to perform these commands:

`composer runtime install --card PeerAdmin@hlfv1 --businessNetworkName vehicle-lifecycle`
`composer network start --card PeerAdmin@hlfv1 --networkAdmin admin --networkAdminEnrollSecret adminpw --archiveFile vehicle-lifecycle@1.0.0.bna --file networkadmin.card`
`composer card import --file networkadmin.card` (Note that this last command should be done only once on a composer installation)


If you want to update the business network with a new version, you need to use:
`composer network update --archiveFile vehicle-lifecycle*.bna --card admin@vehicle-lifecycle`

The following command list a business network and all its data
`composer network list --card admin@vehicle-lifecycle`

Note that you can use the following commands:
`npm run deploy`
`npm run update`
`npm run list`

The latest command invoked in `npm run deploy` will fail importing the network admin card in your wallet if it has already been imported. This should not prevent using the deployed composer application.