'use strict';


const BusinessNetworkConnection = require('composer-client').BusinessNetworkConnection;
const Table = require('cli-table');
const winston = require('winston');
let config = require('config').get('vehicle-lifecycle');

// these are the credentials to use to connect to the Hyperledger Fabric
let participantId = config.get('participantId');
let participantPwd = config.get('participantPwd');
const LOG = winston.loggers.get('application');


/** Class for the VehicleLifecycle application*/
class VehicleLifecycle {

  /**
   * Need to have the mapping from bizNetwork name to the URLs to connect to.
   * bizNetwork nawme will be able to be used by Composer to get the suitable model files.
   *
   */
    constructor() {

        this.bizNetworkConnection = new BusinessNetworkConnection();
        this.CONNECTION_PROFILE_NAME = config.get('connectionProfile');
        this.businessNetworkIdentifier = config.get('businessNetworkIdentifier');
    }

  /** @description Initalizes the VehicleLifecycle by making a connection to the Composer runtime
   * @return {Promise} A promise whose fullfillment means the initialization has completed
   */
    init() {

        return this.bizNetworkConnection.connect(this.CONNECTION_PROFILE_NAME, this.businessNetworkIdentifier, participantId, participantPwd)
      .then((result) => {
          this.businessNetworkDefinition = result;
          LOG.info('VehicleLifecycle:<init>', 'businessNetworkDefinition obtained', this.businessNetworkDefinition.getIdentifier());
      })
      // and catch any exceptions that are triggered
      .catch(function (error) {
          throw error;
      });

    }

  /**
   * List the vehicles that are stored in the Registry
   * @return {Promise} resolved when fullfiled will have listed out the vehicles to stdout
   */
    listVehicles() {
        const METHOD = 'listVehicles';

        let vehicleRegistry;

        LOG.info(METHOD, 'Getting the asset registry');
        return this.bizNetworkConnection.getAssetRegistry('org.vda.Vehicle')
        .then((registry) => {
          vehicleRegistry = registry;

          LOG.info(METHOD, 'Getting all vehicles from the registry.');
          return vehicleRegistry.resolveAll();

        })

        .then((aResources) => {
        // instantiate
        let table = new Table({
            head: ['VIN', 'Owner', 'Make', 'Model', 'Message']
        });
        let arrayLength = aResources.length;
        for(let i = 0; i < arrayLength; i++) {

            let tableLine = [];
            tableLine.push(aResources[i].vin);
            tableLine.push(aResources[i].owner.ssn);
            tableLine.push(aResources[i].vehicleDetails.make?aResources[i].vehicleDetails.make:"<unknown make>");
            tableLine.push(aResources[i].vehicleDetails.modelType?aResources[i].vehicleDetails.modelType:"<unknown model>");
            tableLine.push(aResources[i].suspiciousMessage?aResources[i].suspiciousMessage:"none");
            table.push(tableLine);
        }

        // Put to stdout - as this is really a command line app
        return(table);
        })


        // and catch any exceptions that are triggered
        .catch(function (error) {
            console.log(error);
            /* potentially some code for generating an error specific message here */
            this.log.error(METHOD, 'uh-oh', error);
        });
    }

    setup() 
    {
        const METHOD = 'setup';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('org.acme.vehicle.lifecycle','SetupDemo');		
        LOG.info(METHOD, 'Submitting setup transaction');
        return this.bizNetworkConnection.submitTransaction(transaction);
    }


    makeSuspiciousTransfer() 
    {
        const METHOD = 'makeSuspiciousTransfer';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('org.vda','PrivateVehicleTransfer');		
		transaction.seller  = factory.newRelationship('composer.base', 'Person', 'dan');
		transaction.buyer  = factory.newRelationship('composer.base', 'Person', 'anthony');
		transaction.vehicle = factory.newRelationship('org.vda', 'Vehicle', '156478954'); 	
        transaction.specialNotes = "Dan selling a car to Anthony Michel";
        
        var serializer = this.businessNetworkDefinition.getSerializer();
        var transactionObject = serializer.toJSON(transaction);
        var personRegistry;

        var cnx = this.bizNetworkConnection;

        return cnx.getParticipantRegistry('composer.base.Person')
        .then(function (registry){
            personRegistry = registry;
            return personRegistry.get('dan');
        }).then(function (dan) {
            transactionObject.seller = serializer.toJSON(dan);
        }).then(function () {
            return personRegistry.get('anthony');
        }).then(function (anthony) {
            transactionObject.buyer = serializer.toJSON(anthony);
        }).then(function () {
            return cnx.getAssetRegistry('org.vda.Vehicle');
        }).then(function (registry) {
            return registry.get('156478954');
        }).then(function (vehicle) {
            transactionObject.vehicle = serializer.toJSON(vehicle);
        }).then(function () {
            LOG.info(METHOD, 'Submitting transaction');
            LOG.info(METHOD, JSON.stringify(transactionObject, null, 4));        
            return cnx.submitTransaction(transaction);
        });
    }

    /**
     * This method cleans all resources from the Business Network
     */
    clean()
    {
        const METHOD = 'clean';
        
        let cnx = this.bizNetworkConnection;
        let vehicleRegistry;
        let personRegistry;
        let manufacturerRegistry;
        let regulatorRegistry;

        LOG.info(METHOD, 'Cleaning all resources');
        return cnx.getAssetRegistry('org.vda.Vehicle')
        .then((registry) => {
          vehicleRegistry = registry;
          return vehicleRegistry.getAll();
        }).then((resources) => {
            return vehicleRegistry.removeAll(resources);
        }).then(function() {
            return cnx.getParticipantRegistry('composer.base.Person');
        }).then((registry) => {
            personRegistry = registry;
            return personRegistry.getAll();
        }).then((resources) => {
            return personRegistry.removeAll(resources);
        }).then(function () {
            return cnx.getParticipantRegistry('org.acme.vehicle.lifecycle.manufacturer.Manufacturer');
        }).then ((registry) => {
            manufacturerRegistry = registry;
            return manufacturerRegistry.getAll();
        }).then((resources) => {
            return manufacturerRegistry.removeAll(resources);
        }).then(function () {
            return cnx.getParticipantRegistry('org.acme.vehicle.lifecycle.Regulator');
        }).then ((registry) => {
            regulatorRegistry = registry;
            return regulatorRegistry.getAll();
        }).then((resources) => {
            return regulatorRegistry.removeAll(resources);
        });
    }

  /**
   * @description - run the listVehicles command
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static listCmd(args) {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.listVehicles();
        })
        .then((results) => {        
            if (results != null) {
                LOG.info('List of Vehicles');
                LOG.info('\n'+results.toString());
            } else {
                LOG.info('No Vehicles found');
            }
        })
        .catch(function (error) {
            /* potentially some code for generating an error specific message here */
            throw error;
        });
    }

    /**
   * @description - makeSuspiciousTransfer
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static makeSuspiciousTransferCmd(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.makeSuspiciousTransfer();
        })
        .then((results) => {
            LOG.info('Transaction Submitted');
        })
        .catch(function (error) {
            /* potentially some code for generating an error specific message here */
            throw error;
        });
    }
    /**
   * @description - clean
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static cleanCmd(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.clean();
        })
        .then((results) => {
            LOG.info('All data has been removed from Vehicle Lifecycle Business Network');
        })
        .catch(function (error) {
            /* potentially some code for generating an error specific message here */
            throw error;
        });
    }

    /**
   * @description - setup
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static setupCmd(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.setup();
        })
        .then((results) => {
            LOG.info('Vehicle Lifecycle Business Network has been populated with data');
        })
        .catch(function (error) {
            /* potentially some code for generating an error specific message here */
            throw error;
        });
    }

}

module.exports = VehicleLifecycle;