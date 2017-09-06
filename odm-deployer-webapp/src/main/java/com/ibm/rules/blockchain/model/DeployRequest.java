package com.ibm.rules.blockchain.model;


import javax.ws.rs.FormParam;

/**
 * Data received when /deploy is send.
 */
public class DeployRequest {
    @FormParam("ruleAppName")
    public String ruleAppName;

    @FormParam("ruleapp_version")
    public String ruleapp_version;

    @FormParam("ruleset_version")
    public String ruleset_version;

    @FormParam("archive")
    public byte[] archive;

    @FormParam("managedXomURI")
    public String managedXomURI;

    //START Unused parameters;
    @FormParam("$class")
    public String $class;

    @FormParam("resDeployerURL")
    public String resDeployerURL;

    @FormParam("transactionId")
    public String transactionId;

    @FormParam("timestamp")
    public String timestamp;
    //END Unused parameters;

    public DeployRequest() {}
    public DeployRequest(String d) {}
}
