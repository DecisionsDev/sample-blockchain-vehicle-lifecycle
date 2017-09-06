package com.ibm.rules.blockchain.model;


import javax.ws.rs.FormParam;

/**
 * Data received when /deployxom is send.
 */
public class DeployXomRequest {

    @FormParam("xomName")
    public String xomName;

    @FormParam("libraryName")
    public String libraryName;

    @FormParam("library_version")
    public String library_version;

    @FormParam("xom")
    public byte[] xom;

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


    public DeployXomRequest() {}
    public DeployXomRequest(String d) {}
}
