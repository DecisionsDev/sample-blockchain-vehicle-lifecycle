package com.ibm.rules.blockchain.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class PostRuleAppResponse {

    @JsonProperty("succeeded")
    public boolean succeeded;

    public ArrayList<PostRuleAppResourceResponse> resource;
}
