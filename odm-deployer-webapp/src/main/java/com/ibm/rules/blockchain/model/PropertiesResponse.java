package com.ibm.rules.blockchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertiesResponse {

    @JsonProperty("succeeded")
    public boolean succeeded;

    @JsonProperty("code")
    public String code;

    @JsonProperty("message")
    public String message;
}
