# vehicle-lifecycle-decision-service

This directory contains the rule project that implements the vehicle lifecycle decision service. 

Use Rule Desiger to edit it. 

# first time

- launch Rule Designer from the ODM 8.9 installation
- create an empty workspace
- import vehicle-lifecycle-xom and vehicle-lifecycle-decision-service into this workspace

# generating the XOM

In Rule Designer, perform the following action to generate the XOM:
- right-click on the 'deployer' file in the 'deployment' directory
- select 'Rule Execution Server/Deploy XOM'

vehicle-lifecycle-xom.jar and vehicle-lifecycle-xom.zip are generated in the 'output' directory. 

# generating the Ruleapp

In Rule Designer, perform the following action to generate the Ruleapp:
- right-click on the 'deployer' file in the 'deployment' directory
- select 'Rule Execution Server/Deploy...'

vehicle_lifecycle_ds.jar is generated in the 'output' directory. 