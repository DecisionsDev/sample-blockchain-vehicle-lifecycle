package com.ibm.rules.blockchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.FormParam;
import java.util.ArrayList;
import java.util.List;

public class PostXomResponse {

    @JsonProperty("succeeded")
    public boolean succeeded;

    @JsonProperty("resource")
    public PostXomResourceResponse resource;


    public class PostXomResourceResponse {

        @JsonProperty("id")
        public String id;

        @JsonProperty("uri")
        public String uri;

        @JsonProperty("name")
        public String name;

        @JsonProperty("version")
        public String version;

        @JsonProperty("creationDate")
        public String creationDate;

        @JsonProperty("sha1")
        public String sha1;
    }
}
