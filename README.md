# Zillow Developers API

## Synopsis

I'm learning how the Zillow Developer's API works. As such I am creating a series of REST test to tickle the interface.

## Pre-Requisites 
* [API Dev SignUp](https://www.zillow.com/webservice/Registration.htm)
* You will get an email - Thank you for registering with the Zillow API Network! 
* The email contains your ZWSID - needed for REST API communication, you will enter this in your REST API for e.g. zws-id=ZWSID

## My Test Setup
* Postman - I use postman to get going and figure out how to use the api
* Fiddler - This is proxy which you can use to capture the message traffic between Postman and Zillow REST API Server. Its great to see the message content when you are not quite sure what is being sent. Both Chrome and Postman will auto detect Fiddler's presence and route through it. 
* Eclipse - I'm using eclipse for my IDE, but anything will do.
* Rest-Assured - A great framework for testing RESTful interfaces

## Postman
This is a great tool for getting the kinks figured out without having to jump into java or some other programming language. 

### Sample Request
NOTE: using variable substitution as described in the reference below, saves a bunch of time.
`{{base_uri}}/{{base_path}}/GetRegionChildren.htm?zws-id={{ZWSID}}&state=ca&city=sanfrancisco&childtype=neighborhood`

* base_uri: http://www.zillow.com
* base_path: webservice
* ZWSID - your personal id as sent to you (see above)

### Reference
I found the following link to be very helpful to get upto speed on creating collections, environments and variable substitution: [POSTMAN RESTful API testing app demo](https://www.youtube.com/watch?v=O6la-NJYiu8) by Yuriy Kalynovskiy

## Fiddler
[Fiddler](http://www.telerik.com/fiddler) is another indispensable tool when looking at protocols. Its available on Windows, Linux and I believe Mac. I'm using Ubuntu 14.04 for that you need [Mono Fiddler](http://fiddler.wikidot.com/mono)


## Sample Test
* NOTE: This is a slightly trimmed down version, for the most up to date version see src/test
* SEE: [GetRegionChildren API](https://www.zillow.com/howto/api/GetRegionChildren.htm)

    package com.timr.zillow;
    
    import static io.restassured.RestAssured.given;
    import static io.restassured.specification.ProxySpecification.host;
    import static org.hamcrest.Matchers.equalTo;
      
    public class GetRegionChildrenTest {
        
        private static final String ERR_REQ_SUCCESS = "0";
    
        private static String ZWSID;
        private PropertyManager props = null;
        private static String baseURI = null;
        private static String proxyHost = null;
        private static int proxyPort = 0;
        private static Boolean useProxy = new Boolean(false);
    
        @BeforeTest
        public void beforeTest() {
            props = new PropertyManager("test.properties");
            useProxy = Boolean.parseBoolean(props.getProperty("useProxy")); 
            if(useProxy) {
              baseURI = props.getProperty("proxyBaseURI");
              proxyHost = props.getProperty("proxyHost");
              proxyPort = Integer.parseInt(props.getProperty("proxyPort"));
            } else {
              baseURI = props.getProperty("baseURI");
            }
            RestAssured.baseURI = baseURI;
            RestAssured.basePath = "/webservice";
            if(useProxy){
              RestAssured.proxy = host(proxyHost).withPort(proxyPort);
            }
            ZWSID = props.getProperty("ZWSID");
        }
        
        @Test
        public void testStateCityNeighboorhood(){
            given(). 
                param("zws-id", ZWSID). 
                param("state", "ca"). 
                param("city", "sanfrancisco"). 
                param("childtype", "neighborhood"). 
            when().
              get("/GetRegionChildren.htm"). 
            then().
            assertThat().body("regionchildren.message.code", equalTo(ERR_REQ_SUCCESS));
        }


## References
* [Creating data driven API tests with REST Assured and TestNG](http://www.ontestautomation.com/creating-data-driven-api-tests-with-rest-assured-and-testng/) This is a great site to learn about [Rest Assured](http://rest-assured.io/) and [Bas Dijkstra](http://www.ontestautomation.com/about/) has a lot of extremely useful content.

* [Testing REST services with REST Assured](http://www.ontestautomation.com/testing-rest-services-with-rest-assured/) is a good article for handling RESTful interfaces that return XML not JSON. Zillow's API returns XML.
