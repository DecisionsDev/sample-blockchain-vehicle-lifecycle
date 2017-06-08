'use strict';

const VehicleLifecycle = require('./../vehicle-lifecycle.js');
const winston = require('winston');
const LOG = winston.loggers.get('application');

exports.command = 'deployRuleapp';
exports.desc = 'Deploy ruleapp';
exports.builder = {};
exports.handler = function (argv) {

    return VehicleLifecycle.deployRuleappCmd(argv)
  .then(() => {
      LOG.info('Command completed successfully.');
      process.exit(0);
  })
  .catch((error) => {
      LOG.error(error+ '\nCommand failed.');
      process.exit(1);
  });

}