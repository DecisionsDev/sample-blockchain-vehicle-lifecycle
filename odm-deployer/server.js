'use strict';

var express = require('express');

var app = express();
var http = require('http');

var bodyParser = require('body-parser');
app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));

// parse application/vnd.api+json as json
app.use(bodyParser.json({ type: 'application/vnd.api+json' }))

/*
This entry point deploy the ruleapp archive given as argument along with information about 
ruleapp version. User has to make sure that the ruleapp version in the archive matches the
version information received from Composer
*/

app.post('/deploy', function (req, res) {
    console.log("receiving a deploy request " + JSON.stringify(req.body));
    var ruleAppUpdatedTx = req.body.transaction;

    var ruleAppNameElements = ruleAppUpdatedTx.ruleAppName.split('/');

    var ruleappName = ruleAppNameElements[0];
    var rulesetName = ruleAppNameElements[1];
    var ruleappVersion = ruleAppUpdatedTx.ruleapp_version;
    var rulesetVersion = ruleAppUpdatedTx.ruleset_version;
    var archive = ruleAppUpdatedTx.archive;
    var managedXomURI = ruleAppUpdatedTx.managedXomURI;

    var authorization = "Basic " + new Buffer("resAdmin:resAdmin").toString('base64');

    // deploy to RES
    var header = {
      'Content-Type': 'application/octet-stream',
      'Accept': 'application/json',
      'Authorization': authorization
    };

    var buf = Buffer.from(archive, 'base64');
    var body = buf.toString('binary');
    header['Content-Length'] = buf.length;

    var options = {
      host: 'odmruntime_odm-runtime_1',
      port: '9060',
      path: '/res/api/v1/ruleapps?merging=REPLACE_MERGING_POLICY',
      method: 'POST',
      headers: header
    };

    console.log("Deploying '" + ruleappName + "/" + ruleappVersion + "/" + rulesetName + "/" + rulesetVersion + "' to RES ");

    console.log("Ready to send POST request to RES: ");
    console.log("--> options:");
    console.log(options);
    console.log("--> body: ");

    var resreq = http.request(options, function(resResponse) {
      resResponse.setEncoding('utf-8');

      var responseString = '';

      resResponse.on('data', function(d) {
        responseString += d;
      });

      resResponse.on('end', function() {
        console.log("Completed POST to the RES: " + responseString);
        var responseObject = null;
        if (responseString && responseString.length > 0) {
          try {
            responseObject = JSON.parse(responseString);
            console.log(responseObject);
          } catch(err) {
            console.log("response is not JSON");
          }
        }

        updateArchiveXom(ruleappName, rulesetName, ruleappVersion, rulesetVersion, managedXomURI)
        .then(function (xomUpdated) {
          // prepare response to send to the Smart Contract
          var data = {
            'response' : 'deployment to RES OK',
            'ruleapp' : ruleAppUpdatedTx.ruleAppName,
            'ruleapp_version' : ruleAppUpdatedTx.ruleapp_version,
            'ruleset_version' : ruleAppUpdatedTx.ruleset_version,
          };
          if (responseObject && responseObject['succeeded'] != null && responseObject['succeeded'] == false) {
            data.response = 'deployment to RES failed: ' + responseObject.message;
          }
          if (xomUpdated == false) {
            data.response = data.response + " - XOM URI update failed";
          }
          res.setHeader('Content-Type', 'application/json');
          res.send(JSON.stringify(data));
        })
        .catch(function (err) {
          console.error("cannot update managedXomURI " + err);
        });        
      });
    });

    resreq.on('error', function(err) {
      console.error("communication error");
      // This prints the error message and stack trace to `stderr`.
      console.error(err.stack);
    });

    // send deploy request to the RES
    resreq.write(buf);
    resreq.end();

    console.log("Done");


});

var updateArchiveXom = function(ruleappName, rulesetName, ruleappVersion, rulesetVersion, managedXomURI) 
{
  return new Promise(function (resolve, reject) 
  {

    if (managedXomURI == null) {
      console.log("Provided managedXomUri is null");
      resolve(false);
    }

    var authorization = "Basic " + new Buffer("resAdmin:resAdmin").toString('base64');

    var header = {
      'Content-Type': 'text/plain',
      'Accept': 'application/json',
      'Authorization': authorization
    };

    var body = managedXomURI;
    header['Content-Length'] = body.length;

    var options = {
      host: 'odmruntime_odm-runtime_1',
      port: '9060',
      path: '/res/api/v1/ruleapps/' + ruleappName + '/' + ruleappVersion + '/' + rulesetName + '/' + rulesetVersion + '/properties/ruleset.managedxom.uris',
      method: 'POST',
      headers: header
    };
    var resreq = http.request(options, function(resResponse) {
      resResponse.setEncoding('utf-8');

      var responseString = '';
      resResponse.on('data', function(d) {
        responseString += d;
      });
      resResponse.on('end', function() {
        var responseObject = null;
        if (responseString && responseString.length > 0) {
          try {
            responseObject = JSON.parse(responseString);
            console.log(responseObject);
          } catch(err) {
            console.log("response is not JSON");
          }
        }
        var result = false;
        if (responseObject != null && responseObject['succeeded'] && responseObject['succeeded'] == true) {
          result = true;
        }
        resolve(result);
      });
    });
    resreq.on('error', function(err) {      
      console.error("communication error");
      // This prints the error message and stack trace to `stderr`.
      console.error(err.stack);
      reject(err);
    });

    // send update request to the RES
    resreq.write(body);
    resreq.end();

    console.log("Done Update");

  });
};

var server = app.listen(1880, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("ODM Deployer REST Service listening at http://%s:%s", host, port)

})