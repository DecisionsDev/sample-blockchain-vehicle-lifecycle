package com.ibm.rules.blockchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRuleAppResourceResponse {
    @JsonProperty("initialPath")
    public String initialPath;

    @JsonProperty("operationType")
    public String operationType;

    @JsonProperty("resultPath")
    public String resultPath;

    @JsonProperty("managedXomGeneratedProperty")
    public String managedXomGeneratedProperty;
}