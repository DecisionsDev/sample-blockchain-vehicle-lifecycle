'use strict';

const VehicleLifecycle = require('./../vehicle-lifecycle.js');
const winston = require('winston');
const LOG = winston.loggers.get('application');

exports.command = 'reset';
exports.desc = 'Reset the Vehicle Lifecycle Business Network';
exports.builder = {};
exports.handler = function (argv) {

    return VehicleLifecycle.resetCmd(argv)
  .then(() => {
      LOG.info('Command completed successfully.');
      process.exit(0);
  })
  .catch((error) => {
      LOG.error(error+ '\nCommand failed.');
      process.exit(1);
  });

}