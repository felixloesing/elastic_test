package com.floesing;


import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        System.out.println("Hello Elasticsearch!");

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")
                )
        );

        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("fullName", "Felix")
                    .field("dateOfBirth", new Date())
                    .field("age", "10")
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexRequest indexRequest = new IndexRequest("people");
        indexRequest.source(builder);
        // If ID is not unique, it will update the existing record and increase the version by 1.
        // If ID is not set, it will create and use a random ID
        indexRequest.id("Felix");

        IndexResponse response = null;
        try {
            response = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            System.out.println(response.getResult());
            System.out.println(response);
            System.out.println();
        }

        // Get by ID
        GetRequest getRequest = new GetRequest("people", "Felix");
        GetResponse getResponse = null;
        try {
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(getResponse);
        System.out.println();

        // EXAMPLE: Delete data
        /*
        DeleteRequest deleteRequest = new DeleteRequest("people");
        deleteRequest.id("Felix");

        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(deleteResponse);
        */

        // Example Search Query
        // LOG4j2 is missing; will throw an error and use SimpleLogger instead.
        /*
        QueryBuilder matchSpecificFieldQuery= QueryBuilders
                .matchQuery("fullName", "Felix");

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchSpecificFieldQuery);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(searchResponse);
        System.out.println();
        */

        System.exit(0);
    }
}
