package com.ibm.rules.blockchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.FormParam;
import java.util.List;

public class PostXomLibraryResponse {

    @FormParam("succeeded")
    public boolean succeeded;

    @FormParam("resource")
    public PostXomLibraryResourceResponse resource;

    @JsonProperty("code")
    public String code;

    @JsonProperty("message")
    public String message;


    public PostXomLibraryResponse() {}
    public PostXomLibraryResponse(String d) {}

    public class PostXomLibraryResourceResponse {

        @FormParam("id")
        public String id;

        @FormParam("uri")
        public String uri;

        @FormParam("name")
        public String name;

        @FormParam("version")
        public String version;

        @FormParam("creationDate")
        public String creationDate;

        @FormParam("content")
        public List<String> content;

        public PostXomLibraryResourceResponse() {}
        public PostXomLibraryResourceResponse(String d) {}
    }

}
