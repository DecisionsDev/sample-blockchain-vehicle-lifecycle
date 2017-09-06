#!/bin/bash

# uncomment this variable declaration. This variable must point to the installation directory of IBM ODM 8.9.0
ODM_HOME=/opt/IBM/ODM891/

if [ -z ${ODM_HOME} ]; then
 echo "ODM_HOME is unset, please edit this script, uncomment ODM_HOME variable setting and point to your ODM 8.9 installation"
 exit 1
fi

mkdir -p target/common-bundle/bin
mkdir -p target/liberty-bundle/WLP855
mkdir -p target/tmp
echo "Copying resources from '${ODM_HOME}' to 'target'..."

cp ${ODM_HOME}/executionserver/applicationservers/WLP855/DecisionService.war target/liberty-bundle/WLP855
cp ${ODM_HOME}/executionserver/applicationservers/WLP855/res.war target/liberty-bundle/WLP855

cp ${ODM_HOME}/executionserver/lib/jrules-res-execution.jar target/tmp
cd target/tmp
unzip jrules-res-execution.jar
cp META-INF/default_ra.xml ../common-bundle/bin/ra.xml
cd ../..

echo "Done"
exit 0


