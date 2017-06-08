/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Store Ruleapps in world state
 * @param {com.ibm.rules.RuleAppUpdated} ruleAppUpdatedTransaction
 * @transaction
 */
function processRuleAppUpdatedTransaction(ruleAppUpdatedTransaction) 
{
    console.log("Receiving RuleAppUpdated transaction " + ruleAppUpdatedTransaction.ruleAppName);
    var factory = getFactory();
    var NS = 'com.ibm.rules';

    var ruleapp = factory.newResource(NS, 'RuleApp', ruleAppUpdatedTransaction.transactionId);
    ruleapp.ruleAppName = ruleAppUpdatedTransaction.ruleAppName;
    ruleapp.major_version = ruleAppUpdatedTransaction.major_version;
    ruleapp.minor_version = ruleAppUpdatedTransaction.minor_version;
    ruleapp.qualifier_version = ruleAppUpdatedTransaction.qualifier_version;
    ruleapp.ruleApp = ruleAppUpdatedTransaction.ruleApp;

    var ruleappTx = factory.newResource(NS, 'RuleAppUpdatedWrapper', ruleAppUpdatedTransaction.transactionId);
    ruleappTx.transaction = ruleAppUpdatedTransaction;

    var currentVersion;
    var currentVersionRegistry;
    
    console.log("Initialization completed");

    getAssetRegistry(NS + '.' + 'RuleAppCurrentVersion')
      .then(function (registry) 
      {
          console.log("OK 1");
          currentVersionRegistry = registry;
          return currentVersion = registry.get(ruleapp.ruleAppName);
      })
      .catch(function () {
        console.log("Error 1");
        console.log(currentVersion);
        currentVersion = null;
      })
      .then(function () {
          console.log("OK 2");
          var add = false;
          if (!currentVersion) {
          	console.log("OK 2.1");
              currentVersion = factory.newResource(NS, 'RuleAppCurrentVersion', ruleapp.ruleAppName);
              add = true;
          }
          currentVersion.major_version = ruleapp.major_version;
          currentVersion.minor_version = ruleapp.minor_version;
          currentVersion.qualifier_version = ruleapp.qualifier_version;
          if (add) {
            return currentVersionRegistry.add(currentVersion);  
          } else {
            return currentVersionRegistry.update(currentVersion);
          }
      })
      .catch(function (error) {
        console.log("Error 2");
        console.log(error);
      })
      .then(function () {
        console.log("OK 3");
         /*return post(ruleAppUpdatedTransaction.resDeployerURL, ruleappTx)      
          .then(function (result) {
            console.log("Receiving answer from ODM Decision Service Deployer: " + JSON.stringify(result));
          }).catch(function (error) {
            console.log("Error calling out the decision service deployer");
            console.log(error);
          }); */ 
      })
      .then(function () {
        console.log("OK 4");
        return getAssetRegistry(ruleapp.getFullyQualifiedType());            
      })
      .then(function(registry) {
        console.log("OK 5");
        return registry.add(ruleapp);
      })
      .catch(function () {
        console.log("Error 1");
      });
}