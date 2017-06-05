'use strict';

const VehicleLifecycle = require('./../vehicle-lifecycle.js');
const winston = require('winston');
const LOG = winston.loggers.get('application');

exports.command = 'makeSuspiciousTransfer3';
exports.desc = 'Make a suspicious vehicle transfer3';
exports.builder = {};
exports.handler = function (argv) {


    return VehicleLifecycle.makeSuspiciousTransferCmd3(argv)
  .then(() => {
      LOG.info('Command completed successfully.');
      process.exit(0);
  })
  .catch((error) => {
      LOG.error(error+ '\nCommand failed.');
      process.exit(1);
  });

}