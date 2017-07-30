/*
 *
 *   Copyright IBM Corp. 2017
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
'use strict';

const BusinessNetworkConnection = require('composer-client').BusinessNetworkConnection;
const Table = require('cli-table');
const winston = require('winston');
let config = require('config').get('vehicle-lifecycle');
const fs = require('fs');

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

    deployRuleapp(filepath, ruleappVersion, rulesetVersion) 
    {
        const METHOD = 'deployRuleapp';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('com.ibm.rules','RuleAppUpdated');
        transaction.ruleAppName = 'vehicle_lifecycle_ds/isSuspiciousEntryPoint';		
        transaction.resDeployerURL = 'http://odm-deployer:1880/deploy';		
        transaction.ruleapp_version = ruleappVersion;
        transaction.ruleset_version = rulesetVersion;
        transaction.managedXomURI = 'reslib://vehicle_lifecycle_ds_1.0/1.0';
        transaction.archive = VehicleLifecycle.base64_encode(filepath);

        LOG.info(METHOD, 'Submitting RuleAppUpdated transaction');
        return this.bizNetworkConnection.submitTransaction(transaction);
    }

    deployXom(filepath, libVersion) 
    {
        const METHOD = 'deployXom';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('com.ibm.rules','XomUpdated');

        var xomName;
        var idx = filepath.lastIndexOf('/');
        if (idx != -1) {
            xomName = filepath.substring(idx+1);
        } else {
            xomName = filepath;
        }
        // make the name compliant
        //xomName = xomName.replace(/-/g, '_');
        // remove extension
        /*idx = xomName.lastIndexOf('.');
        if (idx != -1) {
            xomName = xomName.substring(0, idx);
        }*/

        transaction.xomName = xomName; 
        transaction.resDeployerURL = 'http://odm-deployer:1880/deployXom';		
        transaction.libraryName = 'vehicle_lifecycle_ds_1.0';
        transaction.library_version = libVersion;
        transaction.xom = VehicleLifecycle.base64_encode(filepath);

        LOG.info(METHOD, 'Submitting RuleAppUpdated transaction');
        return this.bizNetworkConnection.submitTransaction(transaction);
    }


    makeSuspiciousTransfer1() 
    {
        const METHOD = 'makeSuspiciousTransfer1';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('org.vda','PrivateVehicleTransfer');		
		transaction.seller  = factory.newRelationship('composer.base', 'Person', 'dan');
		transaction.buyer  = factory.newRelationship('composer.base', 'Person', 'anthony');
		transaction.vehicle = factory.newRelationship('org.vda', 'Vehicle', '156478954'); 	
        transaction.specialNotes = "Dan selling a car to Anthony Smith";
        
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

    makeSuspiciousTransfer2() 
    {
        const METHOD = 'makeSuspiciousTransfer2';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('org.vda','PrivateVehicleTransfer');		
		transaction.seller  = factory.newRelationship('composer.base', 'Person', 'dan');
		transaction.buyer  = factory.newRelationship('composer.base', 'Person', 'anthony');
		transaction.vehicle = factory.newRelationship('org.vda', 'Vehicle', '123456789'); 	
        transaction.specialNotes = "Dan selling a car to Anthony Smith";
        
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
            return registry.get('123456789');
        }).then(function (vehicle) {
            transactionObject.vehicle = serializer.toJSON(vehicle);
        }).then(function () {
            LOG.info(METHOD, 'Submitting transaction');
            LOG.info(METHOD, JSON.stringify(transactionObject, null, 4));        
            return cnx.submitTransaction(transaction);
        });
    }

    makeSuspiciousTransfer3() 
    {
        const METHOD = 'makeSuspiciousTransfer3';
	    let factory        = this.businessNetworkDefinition.getFactory();
	    let transaction    = factory.newTransaction('org.vda','PrivateVehicleTransfer');		
		transaction.seller  = factory.newRelationship('composer.base', 'Person', 'dan');
		transaction.buyer  = factory.newRelationship('composer.base', 'Person', 'anthony');
		transaction.vehicle = factory.newRelationship('org.vda', 'Vehicle', '234567890'); 	
        transaction.specialNotes = "Dan selling a car to Anthony Smith";
        
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
            return registry.get('234567890');
        }).then(function (vehicle) {
            transactionObject.vehicle = serializer.toJSON(vehicle);
        }).then(function () {
            LOG.info(METHOD, 'Submitting transaction');
            LOG.info(METHOD, JSON.stringify(transactionObject, null, 4));        
            return cnx.submitTransaction(transaction);
        });
    }

    /**
     * This method reset the model of the Business Network. It preserves the XOM and Ruleapp resources.
     */
    reset()
    {
        const METHOD = 'reset';
        
        let cnx = this.bizNetworkConnection;
        let vehicleRegistry;
        let personRegistry;
        let manufacturerRegistry;
        let regulatorRegistry;
        let ruleAppCurrentVersionRegistry;
        let ruleAppRegistry;
        let xomRegistry;

        LOG.info(METHOD, 'Reseting vehicle-lifecycle model');
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
        })
        ;
    }


    /**
     * This method cleans all resources from the Business Network, including the XOM and Ruleapp resources.
     */
    clean()
    {
        const METHOD = 'clean';
        
        let cnx = this.bizNetworkConnection;
        let vehicleRegistry;
        let personRegistry;
        let manufacturerRegistry;
        let regulatorRegistry;
        let ruleAppCurrentVersionRegistry;
        let ruleAppRegistry;
        let xomRegistry;

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
        }).then(function () {
            return cnx.getAssetRegistry('com.ibm.rules.RuleAppCurrentVersion');
        }).then ((registry) => {
            ruleAppCurrentVersionRegistry = registry;
            return ruleAppCurrentVersionRegistry.getAll();
        }).then((resources) => {
            return ruleAppCurrentVersionRegistry.removeAll(resources);
        }).then(function () {
            return cnx.getAssetRegistry('com.ibm.rules.RuleApp');
        }).then ((registry) => {
            ruleAppRegistry = registry;
            return ruleAppRegistry.getAll();
        }).then((resources) => {
            return ruleAppRegistry.removeAll(resources);
        }).then(function () {
            return cnx.getAssetRegistry('com.ibm.rules.Xom');
        }).then ((registry) => {
            xomRegistry = registry;
            return xomRegistry.getAll();
        }).then((resources) => {
            return xomRegistry.removeAll(resources);
        })
        ;
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
    static makeSuspiciousTransferCmd1(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
                return lr.makeSuspiciousTransfer1();
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
   * @description - makeSuspiciousTransfer2
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static makeSuspiciousTransferCmd2(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
                return lr.makeSuspiciousTransfer2();
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
   * @description - makeSuspiciousTransfer3
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static makeSuspiciousTransferCmd3(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
                return lr.makeSuspiciousTransfer3();
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
   * @description - reset
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static resetCmd(args) 
    {
        let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.reset();
        })
        .then((results) => {
            LOG.info('All vehicle data has been removed from Vehicle Lifecycle Business Network');
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

    /**
   * @description - deployRuleapp
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static deployRuleappCmd(args) 
    {
        var filepath = null;
        var ruleappVersion = '1.0';
        var rulesetVersion = '1.0';
        var cmdLine = args['_'];
        // console.log(args);
        if (cmdLine.length > 1) {
            filepath = cmdLine[1];
        } else {
            filepath = '../vehicle-lifecycle-decision-service/output/vehicle.jar';
            LOG.warn("File not provide, assuming '" + filepath + "'");
        }
        if (cmdLine.length > 2) {
            ruleappVersion = cmdLine[2];
        } else {
            LOG.warn("ruleappVersion not provided, assuming '" + ruleappVersion + "'");
        }
        if (cmdLine.length > 3) {
            rulesetVersion = cmdLine[3];
        } else {
            LOG.warn("rulesetVersion not provided, assuming '" + rulesetVersion + "'");
        }
        ruleappVersion = ruleappVersion.toString();
        if (ruleappVersion.indexOf('.') == -1) {
            ruleappVersion = ruleappVersion + '.0';
        }
        rulesetVersion = rulesetVersion.toString();
        if (rulesetVersion.indexOf('.') == -1) {
            rulesetVersion = rulesetVersion + '.0';
        }

     let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.deployRuleapp(filepath, ruleappVersion, rulesetVersion);
        })
        .then((results) => {
            LOG.info('Deployed Ruleapp');
        })
        .catch(function (error) {
            //potentially some code for generating an error specific message here 
            throw error;
        });  
    }

    /**
   * @description - deployXom
   * @param {Object} args passed from the command line
   * @return {Promise} resolved when the action is complete
   */
    static deployXomCmd(args) 
    {
        var filepath = null;
        var libVersion = '1.0';
        var cmdLine = args['_'];
        // console.log(args);
        if (cmdLine.length > 1) {
            filepath = cmdLine[1];
        } else {
            filepath = '../vehicle-lifecycle-decision-service/output/vehicle-lifecycle-xom.zip';
            LOG.warn("File not provided, assuming '" + filepath + "'");
        }
        if (cmdLine.length > 2) {
            libVersion = cmdLine[2];
        } else {
            LOG.warn("libVersion not provided, assuming '" + libVersion + "'");
        }
        libVersion = libVersion.toString();
        if (libVersion.indexOf('.') == -1) {
            libVersion = libVersion + '.0';
        }

     let lr = new VehicleLifecycle();
        return lr.init()
        .then(() => {
            return lr.deployXom(filepath, libVersion);
        })
        .then((results) => {
            LOG.info('Deployed XOM');
        })
        .catch(function (error) {
            //potentially some code for generating an error specific message here 
            throw error;
        });  
    }

    // encode file data to base64
    static  base64_encode(file) {
        // read binary data
        var bin;
        try {
            bin = fs.readFileSync(file);
            // convert binary data to base64 encoded string
            return new Buffer(bin).toString('base64');
        } catch(err) {
            LOG.error("File: '" + file + "' cannot be read");
        }
    }

}

module.exports = VehicleLifecycle;