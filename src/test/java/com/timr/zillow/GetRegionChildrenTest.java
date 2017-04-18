package com.timr.zillow;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.host;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.timr.utils.PropertyManager;

import io.restassured.RestAssured;
@SuppressWarnings("unused")

public class GetRegionChildrenTest {
    static final Logger logger = Logger.getLogger(GetRegionChildrenTest.class);
    
    private static final String ERR_REQ_SUCCESS = "0";
    private static final String ERR_SERVER_SIDE = "1";
    private static final String ERR_ZWSID = "2";
    private static final String ERR_WEBSRV_UNAVAIL = "3";
    private static final String ERR_API_CALL_UNAVAIL = "4";
    private static final String ERR_INVAILID_REGION_ID = "500";
    private static final String ERR_INVAILID_PARAMS = "501";
    private static final String ERR_REGION_NOT_FOUND = "502";
    private static final String ERR_INVALID_CHILDTYPE = "503";
    private static final String ERR_INVALID_CHILDTYPE_FOR_PARAMS = "504";

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
          System.out.println("Warning - Using Proxy ...");
          logger.debug("*** using proxy *** ");
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

    @Test
    public void testInvalidZWSID(){
        given(). 
            param("zws-id", ZWSID + "BAD"). 
            param("state", "ca"). 
            param("city", "sanfrancisco"). 
            param("childtype", "neighborhood"). 
        when().
          get("/GetRegionChildren.htm"). 
        then().
          assertThat().body("regionchildren.message.code", equalTo(ERR_ZWSID));
    }

    @DataProvider
    public Object[][] BadParmCaps() {
        return new Object[][] { 
            // state, county, city, child-type
            {"State", "city", "childtype" },  // OK - checks the state for caps...
            {"state", "City", "childtype" },  // @BUG - not checking Caps on City
            {"state", "city", "Childtype" }   // @BUG - not checking Caps on Childtype
        };
    }
    
    @Test(dataProvider = "BadParmCaps")
    public void testInvalidParamsCaps(
            String state, String city, String childType){
        given(). 
            param("zws-id", ZWSID). 
            param(state, "ca"). 
            param(city, "sanfrancisco"). 
            param(childType, "neighborhood"). 
        when().
          get("/GetRegionChildren.htm"). 
        then().
          assertThat().body("regionchildren.message.code", equalTo(ERR_INVAILID_PARAMS));
    }
    
    @DataProvider
    public Object[][] InvalidChildTypes() {
        return new Object[][] { 
            // state, county, city, child-type
            {"badtype" }
        };
    }
    
    @Test(dataProvider = "InvalidChildTypes")
    public void testInvalidChildTypeParams(String childType){
        given(). 
            param("zws-id", ZWSID). 
            param("state", "ca"). 
            param("city", "sanfrancisco"). 
            param("childtype", childType). 
        when().
          get("/GetRegionChildren.htm"). 
        then().
          assertThat().body("regionchildren.message.code", equalTo(ERR_INVALID_CHILDTYPE));
    }

    @DataProvider
    public Object[][] InvalidChildTypeForParam() {
        return new Object[][] { 
            {"state" }
        };
    }
    
    @Test(dataProvider = "InvalidChildTypeForParam")
    public void testInvalidChildTypeForParam(String childType){
        given(). 
            param("zws-id", ZWSID). 
            param("state", "ca"). 
            param("city", "sanfrancisco"). 
            param("childtype", childType). 
        when().
          get("/GetRegionChildren.htm"). 
        then().
          assertThat().body("regionchildren.message.code", equalTo(ERR_INVALID_CHILDTYPE_FOR_PARAMS));
    }

    @Test
    public void testChangeParamOrder(){
        given(). 
            param("zws-id", ZWSID). 
            param("city", "sanfrancisco"). 
            param("state", "ca"). 
            param("childtype", "neighborhood"). 
        when().
          get("/GetRegionChildren.htm"). 
        then().
          assertThat().body("regionchildren.message.code", equalTo(ERR_REQ_SUCCESS));
    }
}
